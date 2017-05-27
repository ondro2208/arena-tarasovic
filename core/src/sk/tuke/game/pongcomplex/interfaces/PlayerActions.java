package sk.tuke.game.pongcomplex.interfaces;

import sk.tuke.game.pongcomplex.arena.Direction;

import java.util.ArrayList;

/**
 * PlayerActions interface uses for methods which bot have to implement
 */
public interface PlayerActions {
	/**
	 * This method is calling when player collides with trampoline. Method returns direction in which player will continue.
	 * @param point which player might collect
	 * @param player contains info about player
	 * @return following direction
	 */
	Direction getNextDirection(Point point, PlayerInfo player);

	/**
	 * This method is calling each game iteration.
	 * Student might sort list of enemies by distance to player.
	 * After using information about player and enemies, there should make the decision if player can continue in hsi direction or have to turn back.
	 * @param player contains info about player
	 * @param enemies list of enemies
	 * @return decision about change direction
	 */
	boolean turnBack(PlayerInfo player, ArrayList<Enemy> enemies);
}
