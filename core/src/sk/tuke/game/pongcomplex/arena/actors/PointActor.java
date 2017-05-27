package sk.tuke.game.pongcomplex.arena.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import sk.tuke.game.pongcomplex.arena.GameInfo;
import sk.tuke.game.pongcomplex.interfaces.Point;

import java.util.Random;

/**
 * Represent point in game.
 */
public class PointActor extends BodyTemplate implements Point {
	/**
	 * Width and height of point sprite.
	 */
	private final int POINT_SIZE = GameInfo.GAME_WIDTH / 125;
	/**
	 * Radius of point body.
	 */
	private final int RADIUS = (GameInfo.GAME_WIDTH / 125) / 2;

	/**
	 * All position where point can be generated.
	 */
	public static float[][] pointsPositions = new float[][]{
			{GameInfo.GAME_WIDTH / 4, GameInfo.GAME_HEIGHT - (GameInfo.GAME_HEIGHT / 4) - GameInfo.POINT_OFFSET},
			{GameInfo.GAME_WIDTH / 2, GameInfo.GAME_HEIGHT - (GameInfo.GAME_HEIGHT / 4) - GameInfo.POINT_OFFSET},
			{GameInfo.GAME_WIDTH - (GameInfo.GAME_WIDTH / 4), GameInfo.GAME_HEIGHT - (GameInfo.GAME_HEIGHT / 4) - GameInfo.POINT_OFFSET},
			{GameInfo.GAME_WIDTH / 4, GameInfo.GAME_HEIGHT / 4 + GameInfo.POINT_OFFSET},
			{GameInfo.GAME_WIDTH / 2, GameInfo.GAME_HEIGHT / 4 + GameInfo.POINT_OFFSET},
			{GameInfo.GAME_WIDTH - (GameInfo.GAME_WIDTH / 4), GameInfo.GAME_HEIGHT / 4 + GameInfo.POINT_OFFSET}
	};

	/**
	 * Set image and generate new position for point.
	 * @param vector Position vector of last generated point.
	 */
	public PointActor(Vector2 vector) {
		image = new Texture(Gdx.files.internal("point.png"));
		sprite = new Sprite(image);
		int n = generateNewPointPosition(vector.x, vector.y);
		setPosition(pointsPositions[n][0], pointsPositions[n][1]);
		sprite.setSize(POINT_SIZE, POINT_SIZE);
	}

	/**
	 * Draw point sprite, center and fit it to actual position.
	 */
	@Override
	public void draw(Batch batch, float parentAlpha) {
		sprite.setPosition(physicsBody.getPosition().x - sprite.getWidth() / 2, physicsBody.getPosition().y - sprite.getHeight() / 2);
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		sprite.draw(batch);
	}

	/**
	 * Create body definition with position and type. Used during creation of body.
	 */
	@Override
	protected BodyDef getBodyDef() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.StaticBody;
		bodyDef.position.set(getX() / GameInfo.PPM, getY() / GameInfo.PPM);
		return bodyDef;
	}

	/**
	 * Create fixture definition with circle shape and collision filter.  Used during creation of body.
	 */
	@Override
	protected FixtureDef getFixtureDef() {
		CircleShape shape = new CircleShape();
		shape.setRadius(RADIUS);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.filter.categoryBits = GameInfo.FILTER_POINT_BIT;
		fixtureDef.filter.maskBits = GameInfo.FILTER_POINT_BIT;
		fixtureDef.filter.groupIndex = 0;
		return fixtureDef;
	}

	/**
	 * New point is generated on different position as last.
	 * @param oldX x coordinate of last generated point
	 * @param oldY y coordinate of last generated point
	 * @return number of position from positions array
	 */
	private int generateNewPointPosition(float oldX, float oldY) {
		int n;
		float x, y;
		n = generateRandomNumberOfPositions();
		x = pointsPositions[n][0];
		y = pointsPositions[n][1];
		while (oldX == x && oldY == y) {
			n = generateRandomNumberOfPositions();
			x = pointsPositions[n][0];
			y = pointsPositions[n][1];
		}
		return n;
	}

	/**
	 * Generate pseudorandom number from positions.
	 *
	 * @return Generated number.
	 */
	private int generateRandomNumberOfPositions() {
		Random rand = new Random();
		return rand.nextInt(6);
	}

	/**
	 * Information for student about x coordinate of actual point position.
	 * @return Actual x coordinate point.
	 */
	@Override
	public float getPointX() {
		return physicsBody.getPosition().x * GameInfo.PPM;
	}

	/**
	 * Information for student about y coordinate of actual point position.
	 * @return Actual y coordinate point.
	 */
	@Override
	public float getPointY() {
		return physicsBody.getPosition().y * GameInfo.PPM;
	}

}
