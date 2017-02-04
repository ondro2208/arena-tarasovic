package sk.tuke.game.pong.arena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by otara on 22.1.2017.
 */
public class PlayerActor extends BodyTemplate {

	private Texture image;
	private Sprite sprite;
	public static final int PLAYER_WIDTH = 80;
	public static final short BIT_PLAYER = 2;
	private int score;
	private Direction direction;
	private Body playerBody;
	private Vector2 playerBodyVector;
	private BodyEditorLoader loader;
	private World world;
	protected Fixture fixture;
	private Texture image2 = new Texture(Gdx.files.internal("TD.png"));

	public PlayerActor() {
		direction = Direction.UP;
		image = new Texture(Gdx.files.internal("TU.jpg"));
		sprite = new Sprite(image);
		sprite.setPosition(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() / 2);

		setPosition(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() / 2);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		Vector2 tmpVector = playerBody.getPosition().sub(playerBodyVector);
		setPosition(tmpVector.x,tmpVector.y);
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		sprite.setPosition(tmpVector.x,tmpVector.y);
		sprite.rotate(playerBody.getAngularVelocity());
		sprite.setSize(PLAYER_WIDTH,PLAYER_WIDTH); //nastavenie velkosti podla pozadovanej velkosti
		batch.draw(sprite, sprite.getX(),sprite.getY(),sprite.getOriginX(),sprite.getOriginX(),PLAYER_WIDTH,PLAYER_WIDTH,sprite.getScaleX(),sprite.getScaleY(),sprite.getRotation());
	}

	@Override
	public Body createBody(World world)
	{
		this.world = world;
		BodyDef bd = getBodyDef();
		FixtureDef fd = getFixtureDef();
		playerBody = world.createBody(bd);

		loader = new BodyEditorLoader(Gdx.files.internal("TU.json"));
		loader.attachFixture(playerBody, "playerBodyJson", fd, PLAYER_WIDTH);
		playerBodyVector = loader.getOrigin("playerBodyJson", PLAYER_WIDTH).cpy();

		fd.shape.dispose();
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
		FixtureDef fd = new FixtureDef();
		//fd.shape = shape;
		fd.density = 100f;
		fd.friction = 0.001f;
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

	public void updateVector(float value, int x, int y){
		float mass = playerBody.getMass();
		float targetVelocity = value;//200f; //For 60kmph simulated
		Vector2 targetPosition = new Vector2(x,y);
		// Now calculate the impulse magnitude and use it to scale
		// a direction (because its 2D movement)
		float impulseMag = mass * targetVelocity;
		// Point the cannon towards the touch point
		Vector2 impulse = new Vector2();
		// Point the impulse from the cannon ball to the target
		impulse.set(targetPosition).sub(playerBody.getPosition());
		// Normalize the direction (to get a constant speed)
		impulse.nor();
		// Scale by the calculated magnitude
		impulse.scl(impulseMag);
		// Apply the impulse to the centre so there is no rotation and wake
		// the body if it is sleeping*/
		playerBody.applyLinearImpulse(impulse,playerBody.getWorldCenter(), true);
	}

	public void contact(){
		playerBody.setAngularVelocity(15);
		playerBody.setAngularDamping(5);
	}

	public void endContact(){
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

	public void grabPoint(PointActor point,Stage stage){
		for(Actor actor : stage.getActors()){
			if(actor.equals(point)){
				stage.getActors().removeValue(point,true);
			}
		}
		point.remove();
		this.score++;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public Body getPlayerBody() {
		return playerBody;
	}

	public Vector2 getPlayerBodyVector() {
		return playerBodyVector;
	}

	public BodyEditorLoader getLoader() {
		return loader;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}
}
