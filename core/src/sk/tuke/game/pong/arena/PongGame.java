package sk.tuke.game.pong.arena;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import sk.tuke.game.pong.arena.actors.EnemyActor;
import sk.tuke.game.pong.arena.actors.PlayerActor;
import sk.tuke.game.pong.arena.actors.PointActor;
import sk.tuke.game.pong.arena.actors.TrampolineActor;
import sk.tuke.game.pong.interfaces.Enemy;
import sk.tuke.game.pong.student.Player;

import java.util.ArrayList;
import java.util.List;

public class PongGame extends ApplicationAdapter implements Contact {

	private Box2DDebugRenderer debugRenderer;
	private OrthographicCamera camera;
	private Stage gameStage;
	private TrampolineActor[] trampolines;
	private ArrayList<PointActor> points;
	private ArrayList<PlayerActor> players;
	private ArrayList<EnemyActor> enemies;
	private World world;
	private Player student;

	private Texture backgroundImage;
	private Label scoreText;
	private int score = 0;

	private int generateEnemy;

	public static float[][] positions = new float[][]{
			{GameInfo.GAME_WIDTH / 4, GameInfo.GAME_HEIGHT - (GameInfo.GAME_HEIGHT / 4)},
			{GameInfo.GAME_WIDTH / 2, GameInfo.GAME_HEIGHT - (GameInfo.GAME_HEIGHT / 4)},
			{GameInfo.GAME_WIDTH - (GameInfo.GAME_WIDTH / 4), GameInfo.GAME_HEIGHT - (GameInfo.GAME_HEIGHT / 4)},
			{GameInfo.GAME_WIDTH / 4, GameInfo.GAME_HEIGHT / 4},
			{GameInfo.GAME_WIDTH / 2, GameInfo.GAME_HEIGHT / 4},
			{GameInfo.GAME_WIDTH - (GameInfo.GAME_WIDTH / 4), GameInfo.GAME_HEIGHT / 4}
	};

	@Override
	public void create() {
		camera = new OrthographicCamera();
		debugRenderer = new Box2DDebugRenderer();
		camera.setToOrtho(false, GameInfo.GAME_WIDTH / GameInfo.PPM, GameInfo.GAME_HEIGHT / GameInfo.PPM);
		world = new World(new Vector2(0, 0), true);
		backgroundImage = new Texture(Gdx.files.internal("bg.png"));
		gameStage = new Stage(new ScreenViewport());
		student = new Player();
		enemies = new ArrayList<EnemyActor>();

		playerInitialize();
		trampolineInitialize();
		pointInitialize();
		createScore();
		gameStage.addActor(scoreText);
		world.setContactListener(new PongContactListener(this));
	}


	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}
		gameStage.act();
		update();
		gameStage.getBatch().begin();
		gameStage.getBatch().draw(backgroundImage, 0, 0, GameInfo.GAME_WIDTH, GameInfo.GAME_HEIGHT);
		gameStage.getBatch().end();
		gameStage.draw();
		debugRenderer.render(world, camera.combined);
		world.step(1 / 60f, 6, 2);/*Gdx.graphics.getDeltaTime()*/
	}

	@Override
	public void dispose() {
		backgroundImage.dispose();
		gameStage.dispose();
	}

	private void update() {
		generateEnemy++;
		if (generateEnemy % 200 == 0) {
			generateEnemies();
			generateEnemy = 0;
		}
		if (student.turnBack(players.get(0), convertListToEnemy())) {
			turnBack();
		}
		for (Actor actor : gameStage.getActors()) {
			boolean isPlayer = actor instanceof PlayerActor;
			if (actor instanceof EnemyActor || isPlayer) {
				float x = ((BodyTemplate) actor).getPhysicsBody().getPosition().x * GameInfo.PPM;
				float y = ((BodyTemplate) actor).getPhysicsBody().getPosition().y * GameInfo.PPM;
				if (x < 0 || x > GameInfo.GAME_WIDTH && y < 0 || y > GameInfo.GAME_HEIGHT) {
					world.destroyBody(((BodyTemplate) actor).getPhysicsBody());
					actor.remove();
					if (isPlayer) {
						endGame();
					}
				}
			}
		}
		if (points.size() > 0) {
			for (int i = 0; i < points.size(); i++) {
				Vector2 pointPosition = new Vector2(points.get(i).getPointX(), points.get(i).getPointY());
				for (int j = 0; j < points.size(); j++) {
					Vector2 playerPosition = new Vector2(players.get(i).getPlayerX(), players.get(i).getPlayerY());
					if (pointPosition.dst(playerPosition) < 50) {
						world.destroyBody(points.get(i).getPhysicsBody());
						points.get(i).remove();
						points.remove(points.get(i));
						updateScore();
						PointActor newPoint = new PointActor(pointPosition);
						newPoint.createBody(world);
						gameStage.addActor(newPoint);
						this.points.add(0, newPoint);
						return;
					}
				}
			}
		}
	}

	private void turnBack() {
		for (PlayerActor player : players) {
			switch (player.getDirection()) {
				case DOWN_LEFT: {
					if (isTargetCoordinates(player.getTargetVector(), PointActor.pointsPositions[3])) {
						player.updateVector(PointActor.pointsPositions[1][0], PointActor.pointsPositions[1][1]);
						player.setDirection(Direction.UP_RIGHT);
					}
					if (isTargetCoordinates(player.getTargetVector(), PointActor.pointsPositions[4])) {
						player.updateVector(PointActor.pointsPositions[2][0], PointActor.pointsPositions[2][1]);
						player.setDirection(Direction.UP_RIGHT);
					}
					break;
				}
				case DOWN: {
					if (isTargetCoordinates(player.getTargetVector(), PointActor.pointsPositions[3])) {
						player.updateVector(PointActor.pointsPositions[0][0], PointActor.pointsPositions[0][1]);
						player.setDirection(Direction.UP);
					}
					if (isTargetCoordinates(player.getTargetVector(), PointActor.pointsPositions[4])) {
						player.updateVector(PointActor.pointsPositions[1][0], PointActor.pointsPositions[1][1]);
						player.setDirection(Direction.UP);
					}
					if (isTargetCoordinates(player.getTargetVector(), PointActor.pointsPositions[5])) {
						player.updateVector(PointActor.pointsPositions[2][0], PointActor.pointsPositions[2][1]);
						player.setDirection(Direction.UP);
					}
					break;
				}
				case DOWN_RIGHT: {
					if (isTargetCoordinates(player.getTargetVector(), PointActor.pointsPositions[4])) {
						player.updateVector(PointActor.pointsPositions[0][0], PointActor.pointsPositions[0][1]);
						player.setDirection(Direction.UP_LEFT);
					}
					if (isTargetCoordinates(player.getTargetVector(), PointActor.pointsPositions[5])) {
						player.updateVector(PointActor.pointsPositions[1][0], PointActor.pointsPositions[1][1]);
						player.setDirection(Direction.UP_LEFT);
					}
					break;
				}
				case UP_LEFT: {
					if (isTargetCoordinates(player.getTargetVector(), PointActor.pointsPositions[0])) {
						player.updateVector(PointActor.pointsPositions[4][0], PointActor.pointsPositions[4][1]);
						player.setDirection(Direction.DOWN_RIGHT);
					}
					if (isTargetCoordinates(player.getTargetVector(), PointActor.pointsPositions[1])) {
						player.updateVector(PointActor.pointsPositions[5][0], PointActor.pointsPositions[5][1]);
						player.setDirection(Direction.DOWN_RIGHT);
					}
					break;
				}
				case UP: {
					if (isTargetCoordinates(player.getTargetVector(), PointActor.pointsPositions[0])) {
						player.updateVector(PointActor.pointsPositions[3][0], PointActor.pointsPositions[3][1]);
						player.setDirection(Direction.DOWN);
					}
					if (isTargetCoordinates(player.getTargetVector(), PointActor.pointsPositions[1])) {
						player.updateVector(PointActor.pointsPositions[4][0], PointActor.pointsPositions[4][1]);
						player.setDirection(Direction.DOWN);
					}
					if (isTargetCoordinates(player.getTargetVector(), PointActor.pointsPositions[2])) {
						player.updateVector(PointActor.pointsPositions[5][0], PointActor.pointsPositions[5][1]);
						player.setDirection(Direction.DOWN);
					}
					break;
				}
				case UP_RIGHT: {
					if (isTargetCoordinates(player.getTargetVector(), PointActor.pointsPositions[1])) {
						player.updateVector(PointActor.pointsPositions[3][0], PointActor.pointsPositions[3][1]);
						player.setDirection(Direction.DOWN_LEFT);
					}
					if (isTargetCoordinates(player.getTargetVector(), PointActor.pointsPositions[2])) {
						player.updateVector(PointActor.pointsPositions[4][0], PointActor.pointsPositions[4][1]);
						player.setDirection(Direction.DOWN_LEFT);
					}
					break;
				}
			}
		}
	}

	private boolean isTargetCoordinates(float[] player, float[] point) {
		return (player[0] == point[0] && player[1] == point[1]);
	}

	private void playerInitialize() {
		players = new ArrayList<PlayerActor>();
		PlayerActor player = new PlayerActor();
		player.createBody(world);
		gameStage.addActor(player);
		players.add(0, player);
	}


	private void pointInitialize() {
		points = new ArrayList<PointActor>();
		PointActor point = new PointActor(PointActor.pointsPositions[1][0], PointActor.pointsPositions[1][1]);
		point.createBody(world);
		gameStage.addActor(point);
		points.add(0, point);
	}

	private void trampolineInitialize() {
		trampolines = new TrampolineActor[]{
				new TrampolineActor(positions[0][0], positions[0][1]),
				new TrampolineActor(positions[1][0], positions[1][1]),
				new TrampolineActor(positions[2][0], positions[2][1]),
				new TrampolineActor(positions[3][0], positions[3][1]),
				new TrampolineActor(positions[4][0], positions[4][1]),
				new TrampolineActor(positions[5][0], positions[5][1])
		};
		for (int i = 0; i < 6; i++) {
			gameStage.addActor(trampolines[i]);
			trampolines[i].createBody(world);
		}
	}

	private void generateEnemies() {
		EnemyActor enemy = new EnemyActor();
		gameStage.addActor(enemy);
		enemy.createBody(world);
		enemies.add(enemy);
		enemy.setImpulse(enemy.getEnemyX());
	}

	private Label createScore() {
		Label.LabelStyle textStyle;
		BitmapFont font = new BitmapFont();

		textStyle = new Label.LabelStyle();
		textStyle.font = font;

		scoreText = new Label(GameInfo.SCORE_TEXT + score, textStyle);
		scoreText.setBounds(Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 80, 90, 70);
		scoreText.setFontScale(1f, 1f);
		return scoreText;
	}

	private void updateScore() {
		this.score++;
		scoreText.setText(GameInfo.SCORE_TEXT + score);
	}

	private ArrayList<Enemy> convertListToEnemy() {
		ArrayList<Enemy> enemies = new ArrayList<Enemy>();
		for (EnemyActor enemy : this.enemies) {
			enemies.add(enemy);
		}
		return enemies;
	}

	private void setTarget(PlayerActor player, Direction newDirection) {
		float x = player.getPlayerX();
		float y = player.getPlayerY();
		int offset = 50;
		switch (newDirection) {
			case UP_LEFT: {
				if (x >= PointActor.pointsPositions[1][0] - offset && x <= PointActor.pointsPositions[1][0] + offset) {
					x = PointActor.pointsPositions[0][0];
					y = PointActor.pointsPositions[0][1];
				} else if (x >= PointActor.pointsPositions[2][0] - offset && x <= PointActor.pointsPositions[2][0] + offset) {
					x = PointActor.pointsPositions[1][0];
					y = PointActor.pointsPositions[1][1];
				}
				player.updateVector(x, y);
				break;
			}
			case UP: {
				if (x >= PointActor.pointsPositions[0][0] - offset && x <= PointActor.pointsPositions[0][0] + offset) {
					x = PointActor.pointsPositions[0][0];
					y = PointActor.pointsPositions[0][1];
				} else if (x >= PointActor.pointsPositions[1][0] - offset && x <= PointActor.pointsPositions[1][0] + offset) {
					x = PointActor.pointsPositions[1][0];
					y = PointActor.pointsPositions[1][1];
				} else if (x >= PointActor.pointsPositions[2][0] - offset && x <= PointActor.pointsPositions[2][0] + offset) {
					x = PointActor.pointsPositions[2][0];
					y = PointActor.pointsPositions[2][1];
				}
				player.updateVector(x, y);
				break;
			}
			case UP_RIGHT: {
				if (x >= PointActor.pointsPositions[0][0] - offset && x <= PointActor.pointsPositions[0][0] + offset) {
					x = PointActor.pointsPositions[1][0];
					y = PointActor.pointsPositions[1][1];
				} else if (x >= PointActor.pointsPositions[1][0] - offset && x <= PointActor.pointsPositions[1][0] + offset) {
					x = PointActor.pointsPositions[2][0];
					y = PointActor.pointsPositions[2][1];
				}
				player.updateVector(x, y);
				break;
			}
			case DOWN_LEFT: {
				if (x >= PointActor.pointsPositions[1][0] - 100 && x <= PointActor.pointsPositions[1][0] + 100) {
					x = PointActor.pointsPositions[3][0];
					y = PointActor.pointsPositions[3][1];
				} else if (x >= PointActor.pointsPositions[2][0] - offset && x <= PointActor.pointsPositions[2][0] + offset) {
					x = PointActor.pointsPositions[4][0];
					y = PointActor.pointsPositions[4][1];
				}
				player.updateVector(x, y);
				break;
			}
			case DOWN: {
				if (x >= PointActor.pointsPositions[0][0] - offset && x <= PointActor.pointsPositions[0][0] + offset) {
					x = PointActor.pointsPositions[3][0];
					y = PointActor.pointsPositions[3][1];
				} else if (x >= PointActor.pointsPositions[1][0] - offset && x <= PointActor.pointsPositions[1][0] + offset) {
					x = PointActor.pointsPositions[4][0];
					y = PointActor.pointsPositions[4][1];
				} else if (x >= PointActor.pointsPositions[2][0] - offset && x <= PointActor.pointsPositions[2][0] + offset) {
					x = PointActor.pointsPositions[5][0];
					y = PointActor.pointsPositions[5][1];
				}
				player.updateVector(x, y);
				break;
			}
			case DOWN_RIGHT: {
				if (x >= PointActor.pointsPositions[0][0] - offset && x <= PointActor.pointsPositions[0][0] + offset) {
					x = PointActor.pointsPositions[4][0];
					y = PointActor.pointsPositions[4][1];
				} else if (x >= PointActor.pointsPositions[1][0] - offset && x <= PointActor.pointsPositions[1][0] + offset) {
					x = PointActor.pointsPositions[5][0];
					y = PointActor.pointsPositions[5][1];
				}
				player.updateVector(x, y);
				break;
			}
			default: {
				player.updateVector(x, y);
			}
		}
	}

	private List<Direction> getAllowedNextDirections(Direction direction) {
		List<Direction> allowedDirections = new ArrayList<Direction>();
		switch (direction) {
			case UP_LEFT: {
				allowedDirections.add(Direction.DOWN);
				allowedDirections.add(Direction.DOWN_LEFT);
				break;
			}
			case UP: {
				allowedDirections.add(Direction.DOWN);
				allowedDirections.add(Direction.DOWN_LEFT);
				allowedDirections.add(Direction.DOWN_RIGHT);
				break;
			}
			case UP_RIGHT: {
				allowedDirections.add(Direction.DOWN);
				allowedDirections.add(Direction.DOWN_RIGHT);
				break;
			}
			case DOWN_LEFT: {
				allowedDirections.add(Direction.UP);
				allowedDirections.add(Direction.UP_LEFT);
				break;
			}
			case DOWN: {
				allowedDirections.add(Direction.UP);
				allowedDirections.add(Direction.UP_LEFT);
				allowedDirections.add(Direction.UP_RIGHT);
				break;
			}
			case DOWN_RIGHT: {
				allowedDirections.add(Direction.UP);
				allowedDirections.add(Direction.UP_RIGHT);
				break;
			}

		}
		return allowedDirections;
	}

	@Override
	public void contact(PlayerActor player) {
		Direction newDirection = student.getNextDirection(points.get(0), player);
		List<Direction> allowedDir = getAllowedNextDirections(player.getDirection());
		if (allowedDir.contains(newDirection)) {
			setTarget(player, newDirection);
			player.setDirection(newDirection);
		} else {
			setTarget(player, allowedDir.get(0));
			player.setDirection(allowedDir.get(0));
		}
	}

	@Override
	public void endGame() {
		Gdx.app.exit();
	}
}
