package sk.tuke.game.pong.interfaces;

import sk.tuke.game.pong.arena.Direction;

/**
 * PlayerInfo interface uses for getting information about player.
 */
public interface PlayerInfo {
	/**
	 * Get x position of player
	 *
	 * @return x position
	 */
	float getPlayerX();

	/**
	 * Get x position of player
	 * @return x position
	 */
	float getPlayerY();

	/**
	 * Get direction of player
	 * @return direction
	 */
	Direction getDirection();
}
