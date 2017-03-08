package sk.tuke.game.pong.arena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by otara on 22.1.2017.
 */
public abstract class BodyTemplate extends Actor{

	protected Body physicsBody;
	protected Fixture physicsFixture;
	protected Sprite sprite;
	protected Texture image;

	protected BodyEditorLoader loader;
	protected Vector2 bodyVector;
	protected int size = 1;
	protected String jsonFile;
	protected String name;
	protected int width;



	public Body createBody(World world){
		physicsBody = world.createBody(getBodyDef());
		FixtureDef fixtureDef = getFixtureDef();
		if (fixtureDef != null) {
			if (fixtureDef.shape == null) {
				throw new RuntimeException("Shape is not defined in FixtureDef");
			}
			physicsFixture = physicsBody.createFixture(fixtureDef);
			fixtureDef.shape.dispose(); //needs to be called when shape is no longer needed

			physicsFixture.setUserData(this);
		}
		return physicsBody;
	}

	public Body createJsonBody(World world) {
		BodyDef bd = getBodyDef();
		FixtureDef fd = getFixtureDef();
		physicsBody = world.createBody(bd);

		loader = new BodyEditorLoader(Gdx.files.internal("trampoline.json"));
		loader.attachFixture(physicsBody, "trampolineBodyJson", fd, (width * size) / GameInfo.PPM);
		bodyVector = loader.getOrigin("trampolineBodyJson", (width * size) / GameInfo.PPM).cpy();

		physicsBody.createFixture(fd).setUserData(this);
		return physicsBody;
	}

	protected abstract BodyDef getBodyDef();
	protected abstract FixtureDef getFixtureDef();

}