package sk.tuke.game.pongcomplex.bot;

import sk.tuke.game.pongcomplex.arena.Direction;
import sk.tuke.game.pongcomplex.interfaces.Enemy;
import sk.tuke.game.pongcomplex.interfaces.PlayerActions;
import sk.tuke.game.pongcomplex.interfaces.PlayerInfo;
import sk.tuke.game.pongcomplex.interfaces.Point;

import java.util.ArrayList;

/**
 * Bot without any AI. Only goes up an down. Bot does not take to account point and enemies.
 */
public class Bot3 implements PlayerActions {
	int count = 0;

	/**
	 * Check if count is even-numbered.
	 *
	 * @return Direction.DOWN if count is even-numbered else Direction.UP.
	 */
	@Override
	public Direction getNextDirection(Point point, PlayerInfo player) {
		Direction nextDirection;
		if (count % 2 == 0) {
			nextDirection = Direction.DOWN;
		} else
			nextDirection = Direction.UP;
		return nextDirection;
	}

	/**
	 * No meaningful implementation.
	 * @return Always return false.
	 */
	@Override
	public boolean turnBack(PlayerInfo player, ArrayList<Enemy> enemies) {
		return false;
	}
}
