package sk.tuke.game.pongcomplex.arena.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import sk.tuke.game.pongcomplex.arena.Direction;
import sk.tuke.game.pongcomplex.arena.GameInfo;
import sk.tuke.game.pongcomplex.interfaces.PlayerActions;
import sk.tuke.game.pongcomplex.interfaces.PlayerInfo;

/**
 * Represent player in game.
 */
public class PlayerActor extends BodyTemplate implements PlayerInfo {

	private static final int RADIUS = GameInfo.GAME_WIDTH / 50;
	private static final int PLAYER_SIZE = 2 * RADIUS;
	private Direction direction;
	private float[] targetVector;
	private int score;
	private String name;
	private long id;
	private PlayerActions student;

	/**
	 * Set image and position for player.
	 *
	 * @param id      Id of the player.
	 * @param name    Name of the player shown in the game.
	 * @param student Solution from student.
	 */
	public PlayerActor(long id, String name, PlayerActions student) {
		this.id = id;
		this.name = name;
		this.student = student;
		long index = (id - 1) % 3;
		direction = Direction.UP;
		image = new Texture(Gdx.files.internal("player.png"));
		sprite = new Sprite(image);
		sprite.setSize(PLAYER_SIZE / GameInfo.PPM, PLAYER_SIZE / GameInfo.PPM);
		float columnOffset = GameInfo.positions[0][0];
		setPosition(columnOffset * (index + 1), (GameInfo.GAME_HEIGHT / 2));
		targetVector = new float[2];
		targetVector[0] = GameInfo.positions[(int) index][0];
		targetVector[1] = GameInfo.positions[(int) index][1];
	}

	/**
	 * Draw player sprite, center and fit it to actual position.
	 */
	@Override
	public void draw(Batch batch, float parentAlpha) {
		sprite.setPosition((physicsBody.getPosition().x - sprite.getWidth() / 2), (physicsBody.getPosition().y - sprite.getHeight() / 2));
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		sprite.draw(batch);
	}

	/**
	 * Create body definition with position and type. Used during creation of body.
	 */
	@Override
	protected BodyDef getBodyDef() {
		BodyDef bd = new BodyDef();
		bd.position.set(getX() / GameInfo.PPM, getY() / GameInfo.PPM);
		bd.type = BodyDef.BodyType.DynamicBody;
		return bd;
	}

	/**
	 * Create fixture definition with circle shape and collision filter.  Used during creation of body.
	 */
	@Override
	protected FixtureDef getFixtureDef() {
		CircleShape shape = new CircleShape();
		shape.setRadius(RADIUS / GameInfo.PPM);
		FixtureDef fd = new FixtureDef();
		fd.shape = shape;
		fd.density = 100f;
		fd.friction = 0.00f;
		fd.restitution = 1.0f;
		fd.filter.categoryBits = GameInfo.FILTER_PLAYER_BIT;
		fd.filter.maskBits = GameInfo.FILTER_TRAMPOLINE_BIT | GameInfo.FILTER_ENEMY_BIT;
		fd.filter.groupIndex = 0;
		return fd;
	}

	/**
	 * Direct player to position in parameters.
	 * @param x x coordinate of next position
	 * @param y y coordinate of next position
	 */
	public void updateVector(float x, float y) {
		targetVector[0] = x;
		targetVector[1] = y;
		float mass = physicsBody.getMass();
		float targetVelocity = 20;
		Vector2 targetPosition = new Vector2(x / GameInfo.PPM, y / GameInfo.PPM);
		float impulseMag = mass * targetVelocity;
		Vector2 impulse = new Vector2();
		impulse.set(targetPosition).sub(physicsBody.getPosition());
		impulse.scl(impulseMag);
		physicsBody.applyLinearImpulse(impulse, physicsBody.getWorldCenter(), true);
	}

	/**
	 * Information for student about x coordinate of actual player position.
	 * @return Actual x coordinate player.
	 */
	@Override
	public float getPlayerX() {
		return physicsBody.getPosition().x * GameInfo.PPM;
	}

	/**
	 * Information for student about y coordinate of actual player position.
	 * @return Actual y coordinate player.
	 */
	@Override
	public float getPlayerY() {
		return physicsBody.getPosition().y * GameInfo.PPM;
	}

	/**
	 * Information for student about actual direction of player.
	 * @return Actual player's direction.
	 */
	@Override
	public Direction getDirection() {
		return direction;
	}

	/**
	 * Set proper direction after bounce and turn back.
	 * @param direction New direction.
	 */
	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	/**
	 * Vector of the current target where player go.
	 * @return Array with target coordinates.
	 */
	public float[] getTargetVector() {
		return targetVector;
	}

	/**
	 * Score to show in game and in summarize tab.
	 *
	 * @return Value of actual score.
	 */
	public int getScore() {
		return score;
	}

	/**
	 * Increment score after grab of the point.
	 */
	public void incrementScore() {
		score++;
	}

	/**
	 * Player's name shown in game.
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Player's id to store results after collision with enemy or move out of playfield.
	 *
	 * @return Value of player's id.
	 */
	public long getId() {
		return id;
	}

	/**
	 * Get student's solution called in game iterations.
	 *
	 * @return Student's solution.
	 */
	public PlayerActions getStudent() {
		return student;
	}
}
