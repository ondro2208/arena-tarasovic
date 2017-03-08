package sk.tuke.game.pong.arena;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import sk.tuke.game.pong.arena.actors.PlayerActor;
import sk.tuke.game.pong.arena.actors.TrampolineActor;

/**
 * Created by DeeL on 09.09.2016.
 */
public class PongContactListener implements ContactListener {
	private sk.tuke.game.pong.arena.Contact contact;

	public PongContactListener(sk.tuke.game.pong.arena.Contact contact) {
		this.contact = contact;
	}

    @Override
    public void beginContact(Contact contact) {
		if (contact.getFixtureA().getUserData() instanceof TrampolineActor &&
				contact.getFixtureB().getUserData() instanceof PlayerActor) {
			this.contact.contact((PlayerActor) contact.getFixtureB().getUserData());
		}
	}

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
