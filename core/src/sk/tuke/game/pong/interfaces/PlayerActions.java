package sk.tuke.game.pong.interfaces;

import sk.tuke.game.pong.arena.Direction;

import java.util.ArrayList;

/**
 * Created by otara on 24.2.2017.
 */
public interface PlayerActions {
	Direction getNextDirection(Point point, Player actor);

	boolean turnBack(Player actor, ArrayList<Enemy> enemies);
}
