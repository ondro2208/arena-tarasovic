package sk.tuke.game.pong.arena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by otara on 22.1.2017.
 */
public class PlayerActor extends BodyTemplate {

	private Texture image;
	private Sprite sprite;
	public static final int PLAYER_WIDTH = 80;
	private Body playerBody;
	private Vector2 playerBodyVector;
	private BodyEditorLoader loader;

	public PlayerActor() {
		image = new Texture(Gdx.files.internal("TU.jpg"));
		sprite = new Sprite(image);

		sprite.setPosition(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() / 2);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		//batch.draw(image,0,0);
		Vector2 vector = playerBody.getPosition().sub(playerBodyVector);
		sprite.setPosition(vector.x,vector.y);
		sprite.setOrigin(playerBodyVector.x, playerBodyVector.y);
		sprite.setRotation(playerBody.getAngle() * MathUtils.radiansToDegrees);

		//playerBody.setLinearVelocity(0f, 400f);
		batch.draw(sprite, sprite.getX(),sprite.getY(),PLAYER_WIDTH,PLAYER_WIDTH);
	}

	@Override
	public Body createBody(World world)
	{
		BodyDef bd = getBodyDef();
		FixtureDef fd = getFixtureDef();
		playerBody = world.createBody(bd);

		loader = new BodyEditorLoader(Gdx.files.internal("TU.json"));
		loader.attachFixture(playerBody, "playerBodyJson", fd, PLAYER_WIDTH);
		playerBodyVector = loader.getOrigin("playerBodyJson", PLAYER_WIDTH).cpy();

		return playerBody;
	}

	@Override
	protected BodyDef getBodyDef() {
		BodyDef bd = new BodyDef();
		bd.position.set(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() / 2);
		bd.type = BodyDef.BodyType.DynamicBody;
		return bd;
	}

	@Override
	protected FixtureDef getFixtureDef() {
		FixtureDef fd = new FixtureDef();
		fd.density = 100f;
		fd.friction = 0.001f;
		fd.restitution = 1.06f;
		return fd;
	}

	@Override
	public void act(float delta) {
		if (playerBody !=null) {
			//playerBody.setLinearVelocity(0.0f,4f);
			//playerBody.getPosition().set(1,1);
			updateVector();
		}
	}

	public void updateVector(){
		float mass = playerBody.getMass();
		float targetVelocity = 6.6667f; //For 60kmph simulated
		Vector2 targetPosition = new Vector2(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()-(Gdx.graphics.getHeight()/4));

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
		// the body if it is sleeping
		float x = Gdx.graphics.getWidth()/2;
		float y = Gdx.graphics.getHeight()/2;
		Vector2 worldcenter= new Vector2(x,y);
		playerBody.applyLinearImpulse(impulse,worldcenter, true);
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
}
