package sk.tuke.game.pong.arena.screens;

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
import sk.tuke.game.pong.arena.*;
import sk.tuke.game.pong.arena.actors.EnemyActor;
import sk.tuke.game.pong.arena.actors.PlayerActor;
import sk.tuke.game.pong.arena.actors.PointActor;
import sk.tuke.game.pong.arena.actors.TrampolineActor;
import sk.tuke.game.pong.arena.scenes.PlayHUDScene;
import sk.tuke.game.pong.interfaces.Enemy;
import sk.tuke.game.pong.interfaces.PlayerActions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by otara on 10.4.2017.
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

	private void pointInitialize() {
		points = new ArrayList<>();
		point = new PointActor(new Vector2(0, 0));
		point.createBody(world);
		gameStage.addActor(point);
		points.add(point);
	}

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
		//debugRenderer.render(world, gameCam.combined);
	}

	public void update() {
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

	private void checkOtherPlayersPositions(Vector2 pointPosition) {
		for (PlayerActor playerActor : players) {
			Vector2 playerPosition = new Vector2(playerActor.getPlayerX(), playerActor.getPlayerY());
			if (pointPosition.dst(playerPosition) < 30) {
				playerActor.incrementScore();
			}
		}
	}

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

	public void removePlayerAndEnemy() {
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

	private void removePlayer(PlayerActor player) {
		results.add(getIndexToResults(player.getScore()), new BotResultImpl(player.getId(), player.getScore()));
		player.remove();
		world.destroyBody(player.getPhysicsBody());
	}

	private int getIndexToResults(int score) {
		for (int i = 0; i < results.size(); i++) {
			BotResult result = results.get(i);
			if (result.getScore() < score) {
				return i;
			}
		}
		return results.size();
	}


	private boolean isLastPlayer(int index) {
		if (index != -1) {
			players.remove(index);
			hud.update();
			if (players.size() < 1)
				game.setScreen(new GameOverScreen(game, results, bots));
			return true;
		}
		return false;
	}

	private ArrayList<Enemy> convertListToEnemy() {
		ArrayList<Enemy> enemies = new ArrayList<Enemy>();
		for (EnemyActor enemy : this.enemies) {
			enemies.add(enemy);
		}
		return enemies;
	}

	public void actorsToRemove(PlayerActor playerActor, EnemyActor enemyActor) {
		playerActorToRemove = playerActor;
		enemyActorToRemove = enemyActor;
	}

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

	@Override
	public void dispose() {
		gameStage.dispose();
		backgroundImage.dispose();
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
