package sk.tuke.game.pong.interfaces;

import sk.tuke.game.pong.arena.Direction;

/**
 * Created by otara on 3.3.2017.
 */
public interface Player {
	float getPlayerX();

	float getPlayerY();

	Direction getDirection();

	void setDirection(Direction direction);
}
