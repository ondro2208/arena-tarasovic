package sk.tuke.game.pong.student;

import sk.tuke.game.pong.arena.Direction;
import sk.tuke.game.pong.interfaces.Enemy;
import sk.tuke.game.pong.interfaces.PlayerActions;
import sk.tuke.game.pong.interfaces.PlayerInfo;
import sk.tuke.game.pong.interfaces.Point;

import java.util.ArrayList;

/**
 * Created by otara on 16.4.2017.
 */
public class StudentPlayer2 implements PlayerActions {
	int count = 0;

	@Override
	public Direction getNextDirection(Point point, PlayerInfo player) {
		Direction nextDirection;
		if (count % 2 == 0) {
			nextDirection = Direction.DOWN;
		} else
			nextDirection = Direction.UP;
		return nextDirection;
	}

	@Override
	public boolean turnBack(PlayerInfo player, ArrayList<Enemy> enemies) {
		return false;
	}
}
