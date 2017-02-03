package sk.tuke.game.pong.arena;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by DeeL on 09.09.2016.
 */
public class PongContactListener implements ContactListener {
    private PlayerActor playerActor;
    private Stage stage;

    public PongContactListener(PlayerActor playerActor,Stage stage)
    {
        this.playerActor = playerActor;
        this.stage = stage;
    }

    @Override
    public void beginContact(Contact contact) {
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
