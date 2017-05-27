package sk.tuke.game.pongcomplex.bot;

import com.badlogic.gdx.math.Vector2;
import sk.tuke.game.pongcomplex.arena.Direction;
import sk.tuke.game.pongcomplex.arena.actors.EnemyActor;
import sk.tuke.game.pongcomplex.interfaces.Enemy;
import sk.tuke.game.pongcomplex.interfaces.PlayerActions;
import sk.tuke.game.pongcomplex.interfaces.PlayerInfo;
import sk.tuke.game.pongcomplex.interfaces.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Bot with worse AI. Tries to go through playfield over predefined path.
 */
public class Bot2 implements PlayerActions {
	private int countOfBounce = 0;
	/**
	 * defined path for player move
	 */
	private Direction[] path = new Direction[]{
			Direction.DOWN_LEFT,
			Direction.UP,
			Direction.DOWN_RIGHT,
			Direction.UP,
			Direction.DOWN_RIGHT,
			Direction.UP,
			Direction.DOWN_LEFT,
			Direction.UP
	};

	/**
	 * Find next direction from path.
	 * @return Next direction form path which might be set.
	 */
	@Override
	public Direction getNextDirection(Point point, PlayerInfo player) {
		Direction nextDirection;
		if (countOfBounce > 7)
			countOfBounce = 0;
		switch (countOfBounce) {
			case 0: {
				nextDirection = path[0];
				break;
			}
			case 1: {
				nextDirection = path[1];
				break;
			}
			case 2: {
				nextDirection = path[2];
				break;
			}
			case 3: {
				nextDirection = path[3];
				break;
			}
			case 4: {
				nextDirection = path[4];
				break;
			}
			case 5: {
				nextDirection = path[5];
				break;
			}
			case 6: {
				nextDirection = path[6];
				break;
			}
			case 7: {
				nextDirection = path[7];
				break;
			}
			default: {
				nextDirection = path[1];
			}
		}
		countOfBounce++;
		return nextDirection;
	}

	/**
	 * Make decision if player should avoid enemy. Sort enemies by distance between player and enemy. Takes into account conditions of player's and enemy's distance.
	 * Decide only if distance is closer than 200 or 100.
	 * @return Output of auxiliary methods.
	 */
	@Override
	public boolean turnBack(PlayerInfo player, ArrayList<Enemy> enemies) {
		HashMap<Float, Enemy> hmap = new HashMap<Float, Enemy>();
		float playerX = player.getPlayerX();
		float playerY = player.getPlayerY();
		for (Enemy enemy : enemies) {
			float enemyX = enemy.getEnemyX();
			float enemyY = enemy.getEnemyY();
			float distance = Vector2.dst(playerX, playerY, enemyX, enemyY);
			hmap.put(distance, enemy);
		}
		Map<Float, Enemy> map = new TreeMap<Float, Enemy>(hmap);
		if (map.size() > 0) {
			Map.Entry<Float, Enemy> entry = map.entrySet().iterator().next();
			if (entry.getKey() < 200) {
				if (entry.getKey() < 100)
					return needChangeNearEnemy(entry.getValue(), player);
				return needChange(entry.getValue(), player);
			} else return false;
		}
		return false;
	}

	/**
	 * Consider directions and check if enemy and player go towards each other.
	 * @return True when enemy and player go towards each other.
	 */
	private boolean needChange(Enemy enemy, PlayerInfo actor) {
		float enemyX = enemy.getEnemyX();
		float enemyY = enemy.getEnemyY();
		EnemyActor.StartSide enemyDirection = enemy.getEnemyDirection();
		float playerX = actor.getPlayerX();
		float playerY = actor.getPlayerY();
		Direction playerDirection = actor.getDirection();
		if (Direction.UP_LEFT == playerDirection && enemyX < playerX && enemyY > playerY && EnemyActor.StartSide.RIGHT == enemyDirection) {
			countOfBounce--;
			return true;
		}
		if (Direction.UP_RIGHT == playerDirection && enemyX > playerX && enemyY > playerY && EnemyActor.StartSide.LEFT == enemyDirection) {
			countOfBounce--;
			return true;
		}
		if (Direction.DOWN_LEFT == playerDirection && enemyX < playerX && enemyY < playerY && EnemyActor.StartSide.RIGHT == enemyDirection) {
			countOfBounce--;
			return true;
		}
		if (Direction.DOWN_RIGHT == playerDirection && enemyX > playerX && enemyY < playerY && EnemyActor.StartSide.LEFT == enemyDirection) {
			countOfBounce--;
			return true;
		}
		if (Direction.UP == playerDirection && enemyY > playerY) {
			if (enemyX < playerX && EnemyActor.StartSide.RIGHT == enemyDirection) {
				countOfBounce--;
				return true;
			}
			if (enemyX > playerX && EnemyActor.StartSide.LEFT == enemyDirection) {
				countOfBounce--;
				return true;
			}
		}
		if (Direction.DOWN == playerDirection && enemyY < playerY) {
			if (enemyX < playerX && EnemyActor.StartSide.RIGHT == enemyDirection) {
				countOfBounce--;
				return true;
			}
			if (enemyX > playerX && EnemyActor.StartSide.LEFT == enemyDirection) {
				countOfBounce--;
				return true;
			}
		}
		return false;
	}

	/**
	 * Consider directions, check if enemy and player go to same side and if enemy is close (150 +/- from player x).
	 * @return True if enemy is close (150 +/- from player x) and both go to same side.
	 */
	private boolean needChangeNearEnemy(Enemy enemy, PlayerInfo actor) {
		float enemyX = enemy.getEnemyX();
		float enemyY = enemy.getEnemyY();
		EnemyActor.StartSide enemyDirection = enemy.getEnemyDirection();
		float playerX = actor.getPlayerX();
		float playerY = actor.getPlayerY();
		Direction playerDirection = actor.getDirection();
		if (Direction.UP_LEFT == playerDirection && (enemyX - playerX < 150) && enemyY > playerY && EnemyActor.StartSide.LEFT == enemyDirection) {
			countOfBounce--;
			return true;
		}
		if (Direction.UP_RIGHT == playerDirection && (playerX - enemyX < 150) && enemyY > playerY && EnemyActor.StartSide.RIGHT == enemyDirection) {
			countOfBounce--;
			return true;
		}
		if (Direction.DOWN_LEFT == playerDirection && (enemyX - playerX < 150) && enemyY < playerY && EnemyActor.StartSide.LEFT == enemyDirection) {
			countOfBounce--;
			return true;
		}
		if (Direction.DOWN_RIGHT == playerDirection && (playerX - enemyX < 150) && enemyY < playerY && EnemyActor.StartSide.LEFT == enemyDirection) {
			countOfBounce--;
			return true;
		}
		return false;
	}


}