package sk.tuke.game.pong.arena;

import com.badlogic.gdx.physics.box2d.*;
import sk.tuke.game.pong.arena.actors.EnemyActor;
import sk.tuke.game.pong.arena.actors.PlayerActor;
import sk.tuke.game.pong.arena.actors.TrampolineActor;
import sk.tuke.game.pong.arena.screens.PlayScreen;

/**
 * Created by DeeL on 09.09.2016.
 */
public class PongContactListener implements ContactListener {
	private PlayScreen game;

	public PongContactListener(PlayScreen game) {
		this.game = game;
	}

	@Override
	public void beginContact(Contact contact) {
		Fixture A = contact.getFixtureA();
		Fixture B = contact.getFixtureB();
		if (isTrampoline(A) && isPlayer(B)) {
			this.game.bounce((PlayerActor) B.getUserData());
		}
		if (isPlayer(A) && isEnemy(B)) {
			this.game.actorsToRemove((PlayerActor) A.getUserData(), (EnemyActor) B.getUserData());
		}
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
			if (fix.getUserData() instanceof TrampolineActor) {
				return true;
			}
		}
		return false;
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
