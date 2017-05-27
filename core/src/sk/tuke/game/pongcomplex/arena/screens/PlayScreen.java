package sk.tuke.game.pongcomplex.arena.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import kpi.openlab.arena.impl.BotResultImpl;
import kpi.openlab.arena.interfaces.Bot;
import kpi.openlab.arena.interfaces.BotResult;
import sk.tuke.game.pongcomplex.arena.*;
import sk.tuke.game.pongcomplex.arena.actors.EnemyActor;
import sk.tuke.game.pongcomplex.arena.actors.PlayerActor;
import sk.tuke.game.pongcomplex.arena.actors.PointActor;
import sk.tuke.game.pongcomplex.arena.actors.TrampolineActor;
import sk.tuke.game.pongcomplex.arena.scene.PlayHUDScene;
import sk.tuke.game.pongcomplex.interfaces.Enemy;
import sk.tuke.game.pongcomplex.interfaces.PlayerActions;

import java.util.ArrayList;
import java.util.List;

/**
 * Second screen after start game. Whole gameplay was executing here.
 */
public class PlayScreen implements Screen {

	private final List<Bot<PlayerActions>> bots;
	private World world;
	private Box2DDebugRenderer debugRenderer;

	private final OrthographicCamera gameCam;
	private FitViewport gamePort;
	private PongGame game;
	private Stage gameStage;
	private Texture backgroundImage;
	private PlayHUDScene hud;

	private ArrayList<PlayerActor> players;
	private List<BotResult> results;
	private ArrayList<EnemyActor> enemies;
	private ArrayList<PointActor> points;
	private TrampolineActor[] trampolines;
	private PointActor point;

	private PlayerActor playerActorToRemove;
	private EnemyActor enemyActorToRemove;

	private int generateEnemy;
	private int generateEnemyDelay = 200;

	/**
	 * Prepare playfield with game objects.
	 *
	 * @param game reference to main game, send to PlayScreen
	 * @param bots list contains all players with student solutions.
	 */
	public PlayScreen(PongGame game, List<Bot<PlayerActions>> bots) {
		this.game = game;
		this.bots = bots;

		gameCam = new OrthographicCamera();
		gamePort = new FitViewport(GameInfo.GAME_WIDTH / GameInfo.PPM, GameInfo.GAME_HEIGHT / GameInfo.PPM, gameCam);
		gameStage = new Stage(gamePort);
		backgroundImage = new Texture(Gdx.files.internal("bg.png"));
		gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

		world = new World(new Vector2(0, 0), true);
		world.setContactListener(new PongContactListener(this));
		debugRenderer = new Box2DDebugRenderer();

		results = new ArrayList<>();
		enemies = new ArrayList<>();
		pointInitialize();
		playersInitialize(bots);
		trampolineInitialize();

		hud = new PlayHUDScene((SpriteBatch) gameStage.getBatch(), players);
	}

	/**
	 * Set first point into game.
	 */
	private void pointInitialize() {
		points = new ArrayList<>();
		point = new PointActor(new Vector2(0, 0));
		point.createBody(world);
		gameStage.addActor(point);
		points.add(point);
	}

	/**
	 * Prepare each player and set first target.
	 */
	private void playersInitialize(List<Bot<PlayerActions>> bots) {
		players = new ArrayList<>();
		for (Bot<PlayerActions> bot : bots) {
			players.add(new PlayerActor(bot.getId(), bot.getName(), bot.getBotInstance()));
			players.get(players.size() - 1).createBody(world);
			gameStage.addActor(players.get(players.size() - 1));
		}
		for (PlayerActor player : players) {
			MoveHelper.setTarget(player, Direction.UP);
		}
	}

	/**
	 * Generate enemy after delay.
	 */
	private void generateEnemies() {
		generateEnemy++;
		if (generateEnemy % generateEnemyDelay == 0) {
			EnemyActor enemy = new EnemyActor(world);
			gameStage.addActor(enemy);
			enemies.add(enemy);
			enemy.setImpulse(enemy.getEnemyX());
			generateEnemy = 0;
		}
	}

	/**
	 * Prepare trampolines into predefined positions.
	 */
	private void trampolineInitialize() {
		trampolines = new TrampolineActor[]{
				new TrampolineActor(GameInfo.positions[0][0], GameInfo.positions[0][1]),
				new TrampolineActor(GameInfo.positions[1][0], GameInfo.positions[1][1]),
				new TrampolineActor(GameInfo.positions[2][0], GameInfo.positions[2][1]),
				new TrampolineActor(GameInfo.positions[3][0], GameInfo.positions[3][1]),
				new TrampolineActor(GameInfo.positions[4][0], GameInfo.positions[4][1]),
				new TrampolineActor(GameInfo.positions[5][0], GameInfo.positions[5][1])
		};
		for (int i = 0; i < 6; i++) {
			gameStage.addActor(trampolines[i]);
			trampolines[i].createBody(world);
		}
	}

	/**
	 * Draw objects according to game state.
	 */
	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}
		update();
		gameStage.getBatch().setProjectionMatrix(hud.stage.getCamera().combined);
		gameStage.getBatch().begin();
		gameStage.getBatch().draw(backgroundImage, 0, 0, GameInfo.GAME_WIDTH, GameInfo.GAME_HEIGHT);
		gameStage.getBatch().end();
		hud.getStage().draw();
		gameStage.draw();
	}

	/**
	 * Execute changes.
	 */
	private void update() {
		for (PlayerActor player : players) {
			if (player.getStudent().turnBack(player, convertListToEnemy())) {
				MoveHelper.turnBack(player);
			}
		}
		checkOutOfField();
		generateEnemies();
		removePlayerAndEnemy();
		collectPoint();
		world.step(1 / 60f, 6, 2);
		gameCam.update();
	}

	/**
	 * Check if some player collect point. If is needed, than generate new point and update score.
	 */
	private void collectPoint() {
		if (points.size() > 0) {
			for (int i = 0; i < points.size(); i++) {
				Vector2 pointPosition = new Vector2(points.get(i).getPointX(), points.get(i).getPointY());
				for (PlayerActor playerActor : players) {
					Vector2 playerPosition = new Vector2(playerActor.getPlayerX(), playerActor.getPlayerY());
					if (pointPosition.dst(playerPosition) < 30) {
						world.destroyBody(points.get(i).getPhysicsBody());
						points.get(i).remove();
						points.remove(points.get(i));
						PointActor newPoint = new PointActor(pointPosition);
						newPoint.createBody(world);
						gameStage.addActor(newPoint);
						this.points.add(0, newPoint);
						checkOtherPlayersPositions(pointPosition);
						hud.update();
						return;
					}
				}
			}
		}
	}

	/**
	 * Check if more players are located over point.
	 */
	private void checkOtherPlayersPositions(Vector2 pointPosition) {
		for (PlayerActor playerActor : players) {
			Vector2 playerPosition = new Vector2(playerActor.getPlayerX(), playerActor.getPlayerY());
			if (pointPosition.dst(playerPosition) < 30) {
				playerActor.incrementScore();
			}
		}
	}

	/**
	 * Check if actors are out of field.
	 */
	private void checkOutOfField() {
		int index = -1;
		for (int i = 0; i < players.size(); i++) {
			PlayerActor playerActor = players.get(i);
			float x = playerActor.getPlayerX();
			float y = playerActor.getPlayerY();
			if (x < 0 || x > GameInfo.GAME_WIDTH || y < 0 || y > GameInfo.GAME_HEIGHT) {
				index = i;
				removePlayer(playerActor);
				break;
			}
		}
		isLastPlayer(index);
		index = -1;
		for (int i = 0; i < enemies.size(); i++) {
			EnemyActor enemy = enemies.get(i);
			float x = enemy.getEnemyX();
			float y = enemy.getEnemyY();
			if (x < 0 || x > GameInfo.GAME_WIDTH || y < 0 || y > GameInfo.GAME_HEIGHT) {
				index = i;
				world.destroyBody(enemy.getPhysicsBody());
				enemy.remove();
				break;
			}
		}
		if (index != -1) {
			enemies.remove(index);
		}
	}

	/**
	 * Remove proper enemy and player after collisions.
	 */
	private void removePlayerAndEnemy() {
		if (playerActorToRemove == null || enemyActorToRemove == null)
			return;
		int index = -1;
		for (int i = 0; i < players.size(); i++) {
			PlayerActor player = players.get(i);
			if (playerActorToRemove == player) {
				index = i;
				removePlayer(player);
				break;
			}
		}

		if (isLastPlayer(index))
			playerActorToRemove = null;
		index = -1;
		for (int i = 0; i < enemies.size(); i++) {
			if (enemyActorToRemove == enemies.get(i)) {
				index = i;
				world.destroyBody(enemyActorToRemove.getPhysicsBody());
				enemyActorToRemove.remove();
				enemyActorToRemove = null;
			}
		}
		if (index != -1) {
			enemies.remove(index);
		}
	}

	/**
	 * Remove player and save score.
	 */
	private void removePlayer(PlayerActor player) {
		results.add(getIndexToResults(player.getScore()), new BotResultImpl(player.getId(), player.getScore()));
		player.remove();
		world.destroyBody(player.getPhysicsBody());
	}

	/**
	 * Find positions according to score.
	 * @param score Acquired score of player.
	 * @return Index of final positions.
	 */
	private int getIndexToResults(int score) {
		for (int i = 0; i < results.size(); i++) {
			BotResult result = results.get(i);
			if (result.getScore() < score) {
				return i;
			}
		}
		return results.size();
	}

	/**
	 * Remove player on index.
	 * @param index Index of player which should be removed.
	 * @return State if removed player was last.
	 */
	private boolean isLastPlayer(int index) {
		if (index != -1) {
			players.remove(index);
			hud.update();
			if (players.size() < 1) {
				game.setResults(results);
				game.setScreen(new GameOverScreen(results, bots));
			}
			return true;
		}
		return false;
	}

	/**
	 * Create list of Enemy from list of EnemyActor for student.
	 */
	private ArrayList<Enemy> convertListToEnemy() {
		ArrayList<Enemy> enemies = new ArrayList<Enemy>();
		for (EnemyActor enemy : this.enemies) {
			enemies.add(enemy);
		}
		return enemies;
	}

	/**
	 * Set actors to remove in iteration.
	 * @param playerActor Player which should be removed.
	 * @param enemyActor Enemy which should be removed.
	 */
	public void actorsToRemove(PlayerActor playerActor, EnemyActor enemyActor) {
		playerActorToRemove = playerActor;
		enemyActorToRemove = enemyActor;
	}

	/**
	 * @param player Actor which bounce.
	 */
	public void bounce(PlayerActor player) {
		PlayerActions student = player.getStudent();
		Direction newDirection = student.getNextDirection(points.get(0), player);
		List<Direction> allowedDir = MoveHelper.getAllowedNextDirections(player.getDirection());
		if (allowedDir.contains(newDirection)) {
			MoveHelper.setTarget(player, newDirection);
			player.setDirection(newDirection);
		} else {
			MoveHelper.setTarget(player, allowedDir.get(0));
			player.setDirection(allowedDir.get(0));
		}
	}
	/**
	 * Dispose screen.
	 */
	@Override
	public void dispose() {
		backgroundImage.dispose();
		gameStage.dispose();
	}

	@Override
	public void show() {

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}
}
