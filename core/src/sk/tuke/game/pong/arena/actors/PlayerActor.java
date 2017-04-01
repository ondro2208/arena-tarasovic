package sk.tuke.game.pong.arena.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import sk.tuke.game.pong.arena.BodyTemplate;
import sk.tuke.game.pong.arena.Direction;
import sk.tuke.game.pong.arena.GameInfo;
import sk.tuke.game.pong.interfaces.PlayerInfo;

/**
 * Created by otara on 22.1.2017.
 */
public class PlayerActor extends BodyTemplate implements PlayerInfo {

	private static final int RADIUS = 40;
	private static final int PLAYER_SIZE = 80;
	private Direction direction;
	private float[] targetVector;

	public PlayerActor() {
		direction = Direction.UP;
		image = new Texture(Gdx.files.internal("player.png"));
		sprite = new Sprite(image);
		sprite.setSize(PLAYER_SIZE, PLAYER_SIZE);
		setPosition(GameInfo.GAME_WIDTH / 2, GameInfo.GAME_HEIGHT / 2);
		targetVector = new float[2];
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		sprite.setPosition((physicsBody.getPosition().x * GameInfo.PPM - sprite.getWidth() / 2), (physicsBody.getPosition().y * GameInfo.PPM - sprite.getHeight() / 2));
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		sprite.draw(batch);
	}

	@Override
	protected BodyDef getBodyDef() {
		BodyDef bd = new BodyDef();
		bd.position.set(getX() / GameInfo.PPM, getY() / GameInfo.PPM);
		bd.type = BodyDef.BodyType.DynamicBody;
		return bd;
	}

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

	@Override
	public void act(float delta) {
		if (physicsBody != null) {
			if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
				updateVector(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - (Gdx.graphics.getHeight() / 4));
		}

	}

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

	@Override
	public Direction getDirection() {
		return direction;
	}

	@Override
	public float getPlayerX() {
		return physicsBody.getPosition().x * GameInfo.PPM;
	}

	@Override
	public float getPlayerY() {
		return physicsBody.getPosition().y * GameInfo.PPM;
	}

	@Override
	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public float[] getTargetVector() {
		return targetVector;
	}
}
