package sk.tuke.game.pong.arena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by otara on 22.1.2017.
 */
public class PlayerActor extends BodyTemplate {

	private Sprite sprite;
	public static final int RADIUS = 40;
	public static final short BIT_PLAYER = 2;
	private static final int PLAYER_SIZE = 80;

	private Direction direction;
	private Body playerBody;


	public PlayerActor() {
		direction = Direction.UP;
		Texture image = new Texture(Gdx.files.internal("player.png"));
		sprite = new Sprite(image);
		sprite.setPosition(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() / 2);
		sprite.setSize(PLAYER_SIZE, PLAYER_SIZE);

		setPosition(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() / 2);

	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		sprite.setPosition(playerBody.getPosition().x - sprite.getWidth() / 2, playerBody.getPosition().y - sprite.getHeight() / 2);
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		sprite.draw(batch);
	}

	@Override
	public Body createBody(World world)
	{
		BodyDef bd = getBodyDef();
		FixtureDef fd = getFixtureDef();
		playerBody = world.createBody(bd);

		if (fd != null) {
			if (fd.shape == null) {
				throw new RuntimeException("Shape is not defined in FixtureDef");
			}
			physicsFixture = playerBody.createFixture(fd);
			fd.shape.dispose();
			physicsFixture.setUserData(this);
		}
		return playerBody;
	}

	@Override
	protected BodyDef getBodyDef() {
		BodyDef bd = new BodyDef();
		bd.position.set(getX(),getY());
		bd.type = BodyDef.BodyType.DynamicBody;
		return bd;
	}

	@Override
	protected FixtureDef getFixtureDef() {
		CircleShape shape = new CircleShape();
		shape.setRadius(RADIUS);
		FixtureDef fd = new FixtureDef();
		fd.shape = shape;
		fd.density = 100f;
		fd.friction = 0.00f;
		fd.restitution = 1.0f;
		fd.filter.categoryBits = BIT_PLAYER;
		fd.filter.maskBits = TrampolineActor.BIT_TRAMPOLINE;
		fd.filter.groupIndex = 0;
		return fd;
	}

	@Override
	public void act(float delta) {
		if (playerBody !=null) {
			if(Gdx.input.isKeyJustPressed(Input.Keys.UP))
				updateVector(400,Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()-(Gdx.graphics.getHeight()/4));
		}

	}

	private void updateVector(float value, int x, int y) {
		float mass = playerBody.getMass();
		float targetVelocity = value;//200f; //For 60kmph simulated
		Vector2 targetPosition = new Vector2(x,y);
		float impulseMag = mass * targetVelocity;
		Vector2 impulse = new Vector2();
		impulse.set(targetPosition).sub(playerBody.getPosition());
		impulse.nor();
		impulse.scl(impulseMag);
		playerBody.applyLinearImpulse(impulse,playerBody.getWorldCenter(), true);
	}

	public void contact() {
		setDirectionAndTarget();
	}

	public void setDirectionAndTarget(){
		switch (direction){
			case UP:{
				setDirection(Direction.DOWN);
				updateVector(400,Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/4);
				break;
			}
			case DOWN:{
				setDirection(Direction.UP);
				updateVector(400,Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()-(Gdx.graphics.getHeight()/4));
				break;
			}
			default:{
				setDirection(Direction.UP);
				updateVector(400,Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()-(Gdx.graphics.getHeight()/4));
			}
		}
	}

	public Sprite getSprite() {
		return sprite;
	}

	public Body getPlayerBody() {
		return playerBody;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}
}
