package sk.tuke.game.pong.interfaces;

import sk.tuke.game.pong.arena.actors.EnemyActor;

/**
 * Created by otara on 3.3.2017.
 */
public interface Enemy {
	float getEnemyX();

	float getEnemyY();

	EnemyActor.StartSide getEnemyDirection();
}
