package sk.tuke.game.pong.interfaces;

import sk.tuke.game.pong.arena.DirAndPos;

/**
 * Created by otara on 3.3.2017.
 */
public interface Player {
	float getPlayerX();

	float getPlayerY();

	DirAndPos getDirection();

	void setDirection(DirAndPos direction);
}
