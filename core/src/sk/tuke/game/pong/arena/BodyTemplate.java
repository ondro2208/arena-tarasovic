package sk.tuke.game.pong.arena;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by otara on 22.1.2017.
 */
public abstract class BodyTemplate extends Actor {

	protected Body physicsBody;
	protected Fixture physicsFixture;
	protected Sprite sprite;
	protected Texture image;

	public Body createBody(World world) {
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

	protected abstract BodyDef getBodyDef();

	protected abstract FixtureDef getFixtureDef();

	public Body getPhysicsBody() {
		return this.physicsBody;
	}

}