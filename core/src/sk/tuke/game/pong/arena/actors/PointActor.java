package sk.tuke.game.pong.arena.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import sk.tuke.game.pong.arena.BodyTemplate;
import sk.tuke.game.pong.arena.GameInfo;
import sk.tuke.game.pong.interfaces.Point;

import java.util.Random;

/**
 * Created by otara on 22.1.2017.
 */
public class PointActor extends BodyTemplate implements Point {

	private final int POINT_SIZE = 20;
	private static final int POINT_OFFSET = 20;
	private final int RADIUS = 10;

	public static int[][] pointsPositions = new int[][]{
			{GameInfo.GAME_WIDTH / 4, GameInfo.GAME_HEIGHT - (GameInfo.GAME_HEIGHT / 4) - POINT_OFFSET},
			{GameInfo.GAME_WIDTH / 2, GameInfo.GAME_HEIGHT - (GameInfo.GAME_HEIGHT / 4) - POINT_OFFSET},
			{GameInfo.GAME_WIDTH - (GameInfo.GAME_WIDTH / 4), GameInfo.GAME_HEIGHT - (GameInfo.GAME_HEIGHT / 4) - POINT_OFFSET},
			{GameInfo.GAME_WIDTH / 4, GameInfo.GAME_HEIGHT / 4 + POINT_OFFSET},
			{GameInfo.GAME_WIDTH / 2, GameInfo.GAME_HEIGHT / 4 + POINT_OFFSET},
			{GameInfo.GAME_WIDTH - (GameInfo.GAME_WIDTH / 4), GameInfo.GAME_HEIGHT / 4 + POINT_OFFSET}
	};

	public PointActor(int x, int y) {
		image = new Texture(Gdx.files.internal("point.jpg"));
		sprite = new Sprite(image);
		setPosition(x,y);
		sprite.setSize(POINT_SIZE, POINT_SIZE);
	}

	public PointActor(Vector2 vector) {
		image = new Texture(Gdx.files.internal("point.jpg"));
		sprite = new Sprite(image);
		int n = generateNewPointPosition(vector.x, vector.y);
		setPosition(pointsPositions[n][0], pointsPositions[n][1]);
		sprite.setSize(POINT_SIZE,POINT_SIZE);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		sprite.setPosition(physicsBody.getPosition().x * GameInfo.PPM - sprite.getWidth() / 2, physicsBody.getPosition().y * GameInfo.PPM - sprite.getHeight() / 2);
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		sprite.draw(batch);
	}

	@Override
	protected BodyDef getBodyDef(){
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.StaticBody;
		bodyDef.position.set(getX() / GameInfo.PPM, getY() / GameInfo.PPM);
		return bodyDef;
	}

	@Override
	protected FixtureDef getFixtureDef() {
		CircleShape shape = new CircleShape();
		shape.setRadius(RADIUS / GameInfo.PPM);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.restitution = 1.00f;
		fixtureDef.friction = 0.001f;
		fixtureDef.density = 1f;
		fixtureDef.filter.categoryBits = GameInfo.FILTER_POINT_BIT;
		fixtureDef.filter.maskBits = GameInfo.FILTER_POINT_BIT;
		fixtureDef.filter.groupIndex = 0;
		return fixtureDef;
	}

	private PointActor generateNewPoint(float oldX, float oldY) {
		int n, x, y;
		n = generateRandomPosition();
		x = pointsPositions[n][0];
		y = pointsPositions[n][1];

		while (oldX == x && oldY == y) {
			n = generateRandomPosition();
			x = pointsPositions[n][0];
			y = pointsPositions[n][1];
		}

		PointActor point = new PointActor(x,y);
		return point;
	}

	private int generateNewPointPosition(float oldX, float oldY) {
		int n, x, y;
		n = generateRandomPosition();
		x = pointsPositions[n][0];
		y = pointsPositions[n][1];
		while (oldX == x && oldY == y) {
			n = generateRandomPosition();
			x = pointsPositions[n][0];
			y = pointsPositions[n][1];
		}
		return n;
	}

	private int generateRandomPosition() {
		Random rand = new Random();
		int n = rand.nextInt(56) + 6;
		System.out.println("before round: " + n);
		n = Math.round(n / (float)10);
		System.out.println("after round: " + n);
		return n-1;
		/*int n = rand.nextInt(2);
		if(n==0){
			return 2-1;
		}
		else
			return 5-1;*/
	}

	@Override
	public float getPointX() {
		return physicsBody.getPosition().x * GameInfo.PPM;
	}

	@Override
	public float getPointY() {
		return physicsBody.getPosition().y * GameInfo.PPM;
	}

}
