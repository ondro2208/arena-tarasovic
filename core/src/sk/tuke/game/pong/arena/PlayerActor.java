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

/**
 * Created by otara on 22.1.2017.
 */
public class PlayerActor extends BodyTemplate {

	private Texture image;
	private Sprite sprite;
	public static final int PLAYER_WIDTH = 80;
	private int score;
	private Body playerBody;
	private Vector2 playerBodyVector;
	private BodyEditorLoader loader;
	private World world;
	protected Fixture fixture;
	private Texture image2 = new Texture(Gdx.files.internal("TD.png"));

	public PlayerActor() {
		image = new Texture(Gdx.files.internal("TU.jpg"));
		sprite = new Sprite(image);
		sprite.setPosition(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() / 2);

		setPosition(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() / 2);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		//batch.draw(image,0,0);
		/*Vector2 vector = playerBody.getPosition().sub(playerBodyVector);
		sprite.setPosition(vector.x,vector.y);
		sprite.setOrigin(playerBodyVector.x, playerBodyVector.y);
		sprite.setRotation(playerBody.getAngle() * MathUtils.radiansToDegrees);*/

		//setPosition(vector.x,vector.y);
		Vector2 tmpVector = playerBody.getPosition().sub(playerBodyVector);
		setPosition(tmpVector.x,tmpVector.y);
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		sprite.setPosition(tmpVector.x,tmpVector.y);
		sprite.rotate(playerBody.getAngularVelocity());
		sprite.setSize(PLAYER_WIDTH,PLAYER_WIDTH); //nastavenie velkosti podla pozadovanej velkosti
		//sprite.draw(batch);

		//playerBody.setLinearVelocity(0f, 400f);
		batch.draw(sprite, sprite.getX(),sprite.getY(),sprite.getOriginX(),sprite.getOriginX(),PLAYER_WIDTH,PLAYER_WIDTH,sprite.getScaleX(),sprite.getScaleY(),sprite.getRotation());
//	public void draw (TextureRegion region, float x, float y, float originX, float originY, float width, float height,
//					  float scaleX, float scaleY, float rotation);
		//sprite.draw(batch);
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
		/*PolygonShape shape = new PolygonShape();
		Vector2 vector1 = new Vector2(getX(),getY());
		Vector2 vector2 = new Vector2(getX()+80,getY());
		Vector2 vector3 = new Vector2(getX()+40,getY()+80);
		Vector2 vectors[] = new Vector2[]{vector1,vector2,vector3};
		shape.set(vectors);*/

		FixtureDef fd = new FixtureDef();
		//fd.shape = shape;
		fd.density = 100f;
		fd.friction = 0.001f;
		fd.restitution = 1.0f;
		return fd;
	}

	@Override
	public void act(float delta) {
		if (playerBody !=null) {
			if(Gdx.input.isKeyJustPressed(Input.Keys.UP))
				updateVector(200);
		}
	}

	public void updateVector(float value){
		float mass = playerBody.getMass();
		float targetVelocity = value;//200f; //For 60kmph simulated
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
		// the body if it is sleeping*/
		playerBody.applyLinearImpulse(impulse,playerBody.getWorldCenter(), true);
	}

	public void contact(){
		playerBody.setAngularVelocity(15);
		playerBody.setAngularDamping(5);
	}

	public void grabPoint(PointActor point){
		point.destroy();
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
}
