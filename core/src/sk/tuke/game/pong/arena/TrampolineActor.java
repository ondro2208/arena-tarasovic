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
public class TrampolineActor extends BodyTemplate {

	private final int x;
	private final int y;
	private Texture image;
	private Sprite sprite;
	public static final int TRAMPOLINE_WIDTH = 200;
	public static final int TRAMPOLINE_HEIGHT = 38;
	private Body trampolineBody;
	private Vector2 trampolineBodyVector;
	private BodyEditorLoader loader;

	public TrampolineActor(int x, int y) {
		image = new Texture(Gdx.files.internal("trampoline.png"));
		sprite = new Sprite(image);

		this.x = x;
		this.y = y;
		sprite.setPosition(x-sprite.getWidth()/2,y);
		setPosition(x,y);
		//sprite.setPosition(Gdx.graphics.getWidth() / 2 - sprite.getWidth() / 2,
		//		Gdx.graphics.getHeight()-(Gdx.graphics.getHeight() / 4));
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		//batch.draw(image,0,0);
		Vector2 vector = trampolineBody.getPosition().sub(trampolineBodyVector);
		sprite.setPosition(vector.x,vector.y);
		sprite.setOrigin(trampolineBodyVector.x, trampolineBodyVector.y);
		sprite.setRotation(trampolineBody.getAngle() * MathUtils.radiansToDegrees);

		//playerBody.setLinearVelocity(0f, 400f);
		batch.draw(sprite, sprite.getX(),sprite.getY(),TRAMPOLINE_WIDTH,TRAMPOLINE_HEIGHT);
	}

	@Override
	public Body createBody(World world)
	{
		BodyDef bd = getBodyDef();
		FixtureDef fd = getFixtureDef();
		trampolineBody = world.createBody(bd);

		loader = new BodyEditorLoader(Gdx.files.internal("trampoline.json"));
		loader.attachFixture(trampolineBody, "trampolineBodyJson", fd, TRAMPOLINE_WIDTH);
		trampolineBodyVector = loader.getOrigin("trampolineBodyJson", TRAMPOLINE_WIDTH).cpy();

		trampolineBody.createFixture(fd).setUserData(this);
		return trampolineBody;
	}

	@Override
	protected BodyDef getBodyDef() {
		BodyDef bd = new BodyDef();
		bd.position.set(getX(),getY());
		bd.type = BodyDef.BodyType.StaticBody;
		return bd;
	}

	@Override
	protected FixtureDef getFixtureDef() {
		FixtureDef fd = new FixtureDef();
		fd.density = 100f;
		fd.friction = 0.001f;
		//fd.restitution = 1.06f;
		return fd;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public Body getTrampolineBody() {
		return trampolineBody;
	}

	public Vector2 getTrampolineBodyVector() {
		return trampolineBodyVector;
	}

	public BodyEditorLoader getLoader() {
		return loader;
	}
}
