package sk.tuke.game.pong.arena;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.*;
import sk.tuke.game.pong.arena.actors.EnemyActor;
import sk.tuke.game.pong.arena.actors.PlayerActor;
import sk.tuke.game.pong.arena.actors.TrampolineActor;

/**
 * Created by DeeL on 09.09.2016.
 */
public class PongContactListener implements ContactListener {
	private sk.tuke.game.pong.arena.Contact contact;
	private boolean secondTrampolineFixture = false;

	public PongContactListener(sk.tuke.game.pong.arena.Contact contact) {
		this.contact = contact;
	}

    @Override
    public void beginContact(Contact contact) {

	}

	private boolean isEnemy(Fixture fixture) {
		for (Fixture fix : fixture.getBody().getFixtureList()) {
			if (fix.getUserData() instanceof EnemyActor) {
				return true;
			}
		}
		return false;
	}

	private boolean isPlayer(Fixture fixture) {
		for (Fixture fix : fixture.getBody().getFixtureList()) {
			if (fix.getUserData() instanceof PlayerActor) {
				return true;
			}
		}
		return false;
	}

	private boolean isTrampoline(Fixture fixture) {
		for (Fixture fix : fixture.getBody().getFixtureList()) {
			if (fix.getUserData() instanceof TrampolineActor && secondTrampolineFixture) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void endContact(Contact contact) {
		Fixture A = contact.getFixtureA();
		Fixture B = contact.getFixtureB();
		if (isTrampoline(A) && isPlayer(B)) {
			this.contact.contact((PlayerActor) B.getUserData());
		}
		if (isPlayer(A) && isEnemy(B) || isPlayer(B) && isEnemy(A)) {
			this.contact.endGame();
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}
}
