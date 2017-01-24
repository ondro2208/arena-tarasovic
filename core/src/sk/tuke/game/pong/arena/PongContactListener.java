package sk.tuke.game.pong.arena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by DeeL on 09.09.2016.
 */
public class PongContactListener implements ContactListener {
    private final PlayerActor playerActor;

    public PongContactListener(PlayerActor playerActor) {
        this.playerActor = playerActor;
    }

    @Override
    public void beginContact(Contact contact) {
        if(contact.getFixtureB().getUserData() instanceof PointActor)
            playerActor.grabPoint(((PointActor) contact.getFixtureB().getUserData()));
        else if(contact.getFixtureA().getUserData() instanceof PointActor)
                playerActor.grabPoint(((PointActor) contact.getFixtureA().getUserData()));
            else
                playerActor.contact();
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
