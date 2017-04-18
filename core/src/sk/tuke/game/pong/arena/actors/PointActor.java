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

	private final int POINT_SIZE = GameInfo.GAME_WIDTH / 125;
	private static final int POINT_OFFSET = GameInfo.GAME_WIDTH / 125;
	private final int RADIUS = (GameInfo.GAME_WIDTH / 125) / 2;

	public static float[][] pointsPositions = new float[][]{
			{GameInfo.GAME_WIDTH / 4, GameInfo.GAME_HEIGHT - (GameInfo.GAME_HEIGHT / 4) - POINT_OFFSET},
			{GameInfo.GAME_WIDTH / 2, GameInfo.GAME_HEIGHT - (GameInfo.GAME_HEIGHT / 4) - POINT_OFFSET},
			{GameInfo.GAME_WIDTH - (GameInfo.GAME_WIDTH / 4), GameInfo.GAME_HEIGHT - (GameInfo.GAME_HEIGHT / 4) - POINT_OFFSET},
			{GameInfo.GAME_WIDTH / 4, GameInfo.GAME_HEIGHT / 4 + POINT_OFFSET},
			{GameInfo.GAME_WIDTH / 2, GameInfo.GAME_HEIGHT / 4 + POINT_OFFSET},
			{GameInfo.GAME_WIDTH - (GameInfo.GAME_WIDTH / 4), GameInfo.GAME_HEIGHT / 4 + POINT_OFFSET}
	};

	public PointActor(Vector2 vector) {
		image = new Texture(Gdx.files.internal("point.jpg"));
		sprite = new Sprite(image);
		int n = generateNewPointPosition(vector.x, vector.y);
		setPosition(pointsPositions[n][0], pointsPositions[n][1]);
		sprite.setSize(POINT_SIZE, POINT_SIZE);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		sprite.setPosition(physicsBody.getPosition().x - sprite.getWidth() / 2, physicsBody.getPosition().y - sprite.getHeight() / 2);
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		sprite.draw(batch);
	}

	@Override
	protected BodyDef getBodyDef() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.StaticBody;
		bodyDef.position.set(getX() / GameInfo.PPM, getY() / GameInfo.PPM);
		return bodyDef;
	}

	@Override
	protected FixtureDef getFixtureDef() {
		CircleShape shape = new CircleShape();
		shape.setRadius(RADIUS /*/ GameInfo.PPM*/);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.filter.categoryBits = GameInfo.FILTER_POINT_BIT;
		fixtureDef.filter.maskBits = GameInfo.FILTER_POINT_BIT;
		fixtureDef.filter.groupIndex = 0;
		return fixtureDef;
	}
	private int generateNewPointPosition(float oldX, float oldY) {
		int n;
		float x, y;
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
		n = Math.round(n / (float) 10);
		return n - 1;
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
