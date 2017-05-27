package sk.tuke.game.pongcomplex.arena;

import com.badlogic.gdx.physics.box2d.*;
import sk.tuke.game.pongcomplex.arena.actors.EnemyActor;
import sk.tuke.game.pongcomplex.arena.actors.PlayerActor;
import sk.tuke.game.pongcomplex.arena.actors.TrampolineActor;
import sk.tuke.game.pongcomplex.arena.screens.PlayScreen;

/**
 * Resolve contacts during game.
 */
public class PongContactListener implements ContactListener {
	private PlayScreen game;

	/**
	 * Create one listener to appropriate physics world.
	 *
	 * @param game Watched game.
	 */
	public PongContactListener(PlayScreen game) {
		this.game = game;
	}

	/**
	 * Called when each contact between fixtures begin. In our game contact can happen between Player, Trampoline and Enemy.
	 * @param contact Concrete contact of A and B fixtures.
	 */
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

	/**
	 * Check if fixture is from enemy.
	 * @param fixture Contain all fixturelist of actor.
	 * @return Whether fixture is from enemy.
	 */
	private boolean isEnemy(Fixture fixture) {
		for (Fixture fix : fixture.getBody().getFixtureList()) {
			if (fix.getUserData() instanceof EnemyActor) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check if fixture is from player.
	 * @param fixture Contain all fixturelist of actor.
	 * @return Whether fixture is from player.
	 */
	private boolean isPlayer(Fixture fixture) {
		for (Fixture fix : fixture.getBody().getFixtureList()) {
			if (fix.getUserData() instanceof PlayerActor) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check if fixture is from trampoline.
	 * @param fixture Contain all fixturelist of actor.
	 * @return Whether fixture is from trampoline.
	 */
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
