package sk.tuke.game.pong.interfaces;

import sk.tuke.game.pong.arena.actors.EnemyActor;

/**
 * Enemy interface uses for getting information about enemy.
 */
public interface Enemy {
	/**
	 * Get x position of enemy
	 *
	 * @return x position
	 */
	float getEnemyX();

	/**
	 * Get y position of enemy
	 * @return y position
	 */
	float getEnemyY();

	/**
	 * Get enemy direciton in which continues
	 * @return enemy direction
	 */
	EnemyActor.StartSide getEnemyDirection();
}
