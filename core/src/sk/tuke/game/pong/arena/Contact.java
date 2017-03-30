package sk.tuke.game.pong.arena;

import sk.tuke.game.pong.arena.actors.PlayerActor;

/**
 * Created by otara on 7.3.2017.
 */
public interface Contact {
	void contact(PlayerActor player);

	void endGame();
}
