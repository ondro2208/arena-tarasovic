package sk.tuke.game.pong.arena;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 * Created by DeeL on 09.09.2016.
 */
public class PongContactListener implements ContactListener {
    private PlayerActor playerActor;

    public PongContactListener(PlayerActor playerActor)
    {
        this.playerActor = playerActor;
    }

    @Override
    public void beginContact(Contact contact) {
        playerActor.contact();
    }

    @Override
    public void endContact(Contact contact) {
        playerActor.endContact();
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
