package sk.tuke.game.pong.interfaces;

import sk.tuke.game.pong.arena.DirAndPos;

/**
 * Created by otara on 24.2.2017.
 */
public interface PlayerActions {
	DirAndPos getPathToPoint(DirAndPos dir, Point point, Player actor);
}
