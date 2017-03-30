package sk.tuke.game.pong.arena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by otara on 14.3.2017.
 */
public abstract class JsonBodyTemplate extends BodyTemplate {

	protected BodyEditorLoader loader;
	protected Vector2 bodyVector;
	protected int size = 1;
	protected String jsonFile;
	protected String name;
	protected int width;

	@Override
	public Body createBody(World world) {
		BodyDef bd = getBodyDef();
		FixtureDef fd = getFixtureDef();
		physicsBody = world.createBody(bd);

		loader = new BodyEditorLoader(Gdx.files.internal(jsonFile));
		loader.attachFixture(physicsBody, name, fd, (width * size) / GameInfo.PPM);
		bodyVector = loader.getOrigin(name, (width * size) / GameInfo.PPM).cpy();

		Fixture fixture = physicsBody.createFixture(fd);//.setUserData(this);
		fd.shape.dispose();
		fixture.setUserData(this);

		return physicsBody;
	}

}
