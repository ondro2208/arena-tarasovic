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
import sk.tuke.game.pong.arena.actors.*;
import sk.tuke.game.pong.arena.scenes.PlayHUDScene;
import sk.tuke.game.pong.interfaces.Enemy;
import sk.tuke.game.pong.interfaces.PlayerActions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by otara on 10.4.2017.
 */
public class PlayScreen implements Screen, Contact {

	private final List<Bot<PlayerActions>> bots;
	private World world;
	private Box2DDebugRenderer debugRenderer;

	private final OrthographicCamera gameCam;
	private FitViewport gamePort;
	private PongGame game;
	private Stage gameStage;
	private Texture backgroundImage;
	private PlayHUDScene hud;

	private ArrayList<ComplexPlayer> complexPlayers;
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

		hud = new PlayHUDScene((SpriteBatch) gameStage.getBatch(), complexPlayers);
	}

	private void pointInitialize() {
		points = new ArrayList<>();
		point = new PointActor(new Vector2(0, 0));
		point.createBody(world);
		gameStage.addActor(point);
		points.add(point);
	}

	private void playersInitialize(List<Bot<PlayerActions>> bots) {
		complexPlayers = new ArrayList<>();
		for (Bot<PlayerActions> bot : bots) {
			complexPlayers.add(new ComplexPlayer(new PlayerActor((bot.getId() - 1) % 3), bot.getBotInstance(), bot.getName(), bot.getId()));
			complexPlayers.get(complexPlayers.size() - 1).getActor().createBody(world);
			gameStage.addActor(complexPlayers.get(complexPlayers.size() - 1).getActor());
		}
		for (ComplexPlayer complexPlayer : complexPlayers) {
			MoveHelper.setTarget(complexPlayer.getActor(), Direction.UP);
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
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			for (ComplexPlayer complexPlayer : complexPlayers) {
				MoveHelper.setTarget(complexPlayer.getActor(), Direction.UP);
			}
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			complexPlayers.get(0).getActor().updateVector(-10, GameInfo.GAME_HEIGHT / 2);
		}
		update();
		gameStage.getBatch().setProjectionMatrix(hud.stage.getCamera().combined);
		gameStage.getBatch().begin();
		gameStage.getBatch().draw(backgroundImage, 0, 0, GameInfo.GAME_WIDTH, GameInfo.GAME_HEIGHT);
		gameStage.getBatch().end();
		hud.getStage().draw();
		gameStage.draw();
		debugRenderer.render(world, gameCam.combined);
	}

	public void update() {
		for (ComplexPlayer complexPlayer : complexPlayers) {
			if (complexPlayer.getStudent().turnBack(complexPlayer.getActor(), convertListToEnemy())) {
				MoveHelper.turnBack(complexPlayer.getActor());
			}
		}
		checkRemovePlayer();
		generateEnemies();
		removePlayer();
		if (points.size() > 0) {
			for (int i = 0; i < points.size(); i++) {
				Vector2 pointPosition = new Vector2(points.get(i).getPointX(), points.get(i).getPointY());
				for (ComplexPlayer complexPlayer : complexPlayers) {
					Vector2 playerPosition = new Vector2(complexPlayer.getActor().getPlayerX(), complexPlayer.getActor().getPlayerY());
					if (pointPosition.dst(playerPosition) < 30) {
						world.destroyBody(points.get(i).getPhysicsBody());
						points.get(i).remove();
						points.remove(points.get(i));
						PointActor newPoint = new PointActor(pointPosition);
						newPoint.createBody(world);
						gameStage.addActor(newPoint);
						this.points.add(0, newPoint);
						complexPlayer.getActor().incrementScore();
						hud.update();
						return;
					}
				}
			}
		}
		world.step(1 / 60f, 6, 2);
		gameCam.update();
	}

	private void checkRemovePlayer() {
		int index = -1;
		for (int i = 0; i < complexPlayers.size(); i++) {
			ComplexPlayer player = complexPlayers.get(i);
			float x = player.getActor().getPlayerX();
			float y = player.getActor().getPlayerY();
			if (x < 0 || x > GameInfo.GAME_WIDTH && y < 0 || y > GameInfo.GAME_HEIGHT) {
				index = i;
				world.destroyBody(player.getActor().getPhysicsBody());
				player.getActor().remove();
				break;
			}
		}
		if (index != -1) {
			complexPlayers.remove(index);
			hud.update();
			if (complexPlayers.size() < 1)
				game.setScreen(new GameOverScreen(game, results, bots));
		}
		for (int i = 0; i < enemies.size(); i++) {
			if (enemyActorToRemove == enemies.get(i)) {
				index = i;
				world.destroyBody(enemyActorToRemove.getPhysicsBody());
				enemyActorToRemove.remove();
			}
		}
		if (index != -1) {
			enemies.remove(index);
		}
	}

	@Override
	public void playerToRemove(PlayerActor playerActor, EnemyActor enemyActor) {
		playerActorToRemove = playerActor;
		enemyActorToRemove = enemyActor;
	}

	public void removePlayer() {
		if (playerActorToRemove == null || enemyActorToRemove == null)
			return;
		int index = -1;
		for (int i = 0; i < complexPlayers.size(); i++) {
			ComplexPlayer player = complexPlayers.get(i);
			if (playerActorToRemove == player.getActor()) {
				results.add(new BotResultImpl(player.getId(), player.getActor().getScore()));
				index = i;
				player.getActor().remove();
				world.destroyBody(player.getActor().getPhysicsBody());
				break;
			}
		}
		if (index != -1) {
			playerActorToRemove = null;
			complexPlayers.remove(index);
			hud.update();
			if (complexPlayers.size() < 1)
				game.setScreen(new GameOverScreen(game, results, bots));
		}
	}

	private ArrayList<Enemy> convertListToEnemy() {
		ArrayList<Enemy> enemies = new ArrayList<Enemy>();
		for (EnemyActor enemy : this.enemies) {
			enemies.add(enemy);
		}
		return enemies;
	}

	@Override
	public void contact(PlayerActor player) {
		PlayerActions student = getStudentToActor(player);
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

	private PlayerActions getStudentToActor(PlayerActor player) {
		PlayerActions student = null;
		for (ComplexPlayer complexPlayer : complexPlayers) {
			if (complexPlayer.getActor() == player) {
				student = complexPlayer.getStudent();
				break;
			}
		}
		return student;
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
