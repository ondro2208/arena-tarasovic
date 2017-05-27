package sk.tuke.game.pongcomplex.arena.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Template for create actor with physics body and properties.
 */
public abstract class BodyTemplate extends Actor {

	protected Body physicsBody;
	protected Fixture physicsFixture;
	protected Sprite sprite;
	protected Texture image;

	/**
	 * Create body in current physics world.
	 *
	 * @param world Physics world.
	 * @return New body with properties in physics world.
	 */
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

	/**
	 * Create body definition with position and type. Used during creation of body.
	 * @return Definition of body.
	 */
	protected abstract BodyDef getBodyDef();

	/**
	 * Create fixture definition with shape and collision filter.  Used during creation of body.
	 * @return Definition of fixture.
	 */
	protected abstract FixtureDef getFixtureDef();

	/**
	 * @return Body of actor.
	 */
	public Body getPhysicsBody() {
		return this.physicsBody;
	}

}