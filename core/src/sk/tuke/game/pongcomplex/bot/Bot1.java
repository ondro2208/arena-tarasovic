package sk.tuke.game.pongcomplex.bot;

import com.badlogic.gdx.math.Vector2;
import sk.tuke.game.pongcomplex.arena.Direction;
import sk.tuke.game.pongcomplex.arena.GameInfo;
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
 * Bot with good AI.
 */
public class Bot1 implements PlayerActions {
	/**
	 * Distance between two columns of trampolines.
	 */
	private final int columnDistance = GameInfo.GAME_WIDTH / 4;

	/**
	 * Find the shortest path to point nad proper direction in which player gets there.
	 * @return Proper direction on the shortest way to point.
	 */
	@Override
	public Direction getNextDirection(Point point, PlayerInfo player) {
		float playerX = player.getPlayerX();
		float playerY = player.getPlayerY();
		float pointX = point.getPointX();
		float pointY = point.getPointY();
		Direction nextDirection = Direction.UP;

		float offset = 50;

		if (isUp(pointY)) {
			if (playerX - offset < pointX && playerX + offset > pointX) {
				nextDirection = Direction.UP;
			}
			if (playerX - offset - columnDistance < pointX && playerX + offset - columnDistance > pointX) {
				if (playerY + offset > pointY && playerY - offset < pointY)
					nextDirection = Direction.DOWN_LEFT;
				else
					nextDirection = Direction.UP_LEFT;
			}
			if (playerX - offset + columnDistance < pointX && playerX + offset + columnDistance > pointX) {
				if (playerY + offset > pointY && playerY - offset < pointY)
					nextDirection = Direction.DOWN_RIGHT;
				else
					nextDirection = Direction.UP_RIGHT;
			}
			if (playerX - offset - 2 * columnDistance < pointX && playerX + offset - 2 * columnDistance > pointX) {
				if (playerY + offset > pointY && playerY - offset < pointY)
					nextDirection = Direction.DOWN_LEFT;
				else
					nextDirection = Direction.UP_LEFT;
			}
			if (playerX - offset + 2 * columnDistance < pointX && playerX + offset + 2 * columnDistance > pointX) {
				if (playerY + offset > pointY && playerY - offset < pointY)
					nextDirection = Direction.DOWN_RIGHT;
				else
					nextDirection = Direction.UP_RIGHT;
			}
		} else {
			if (playerX - offset < pointX && playerX + offset > pointX) {
				nextDirection = Direction.DOWN;
			}
			if (playerX - offset - columnDistance < pointX && playerX + offset - columnDistance > pointX) {
				if (playerY + offset > pointY && playerY - offset < pointY)
					nextDirection = Direction.UP_LEFT;
				else
					nextDirection = Direction.DOWN_LEFT;
			}
			if (playerX - offset + columnDistance < pointX && playerX + offset + columnDistance > pointX) {
				if (playerY + offset > pointY && playerY - offset < pointY)
					nextDirection = Direction.UP_RIGHT;
				else
					nextDirection = Direction.DOWN_RIGHT;
			}
			if (playerX - offset - 2 * columnDistance < pointX && playerX + offset - 2 * columnDistance > pointX) {
				if (playerY + offset > pointY && playerY - offset < pointY)
					nextDirection = Direction.UP_LEFT;
				else
					nextDirection = Direction.DOWN_LEFT;
			}
			if (playerX - offset + 2 * columnDistance < pointX && playerX + offset + 2 * columnDistance > pointX) {
				if (playerY + offset > pointY && playerY - offset < pointY)
					nextDirection = Direction.UP_RIGHT;
				else
					nextDirection = Direction.DOWN_RIGHT;
			}
		}

		return nextDirection;
	}

	/**
	 * Check if point is near up row of trampolines.
	 * @return True if point Y coordinate is grater then half of field height.
	 */
	private boolean isUp(float pointY) {
		return GameInfo.GAME_HEIGHT / 2 < pointY;
	}

	/**
	 * Make decision if player should avoid enemy. Sort enemies by distance between player and enemy. Takes into account conditions of player's and enemy's distance.
	 * Decide only if distance is closer than 100 or 50.
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
			if (entry.getKey() < 100) {
				if (entry.getKey() < 50)
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
			return true;
		}
		if (Direction.UP_RIGHT == playerDirection && enemyX > playerX && enemyY > playerY && EnemyActor.StartSide.LEFT == enemyDirection) {
			return true;
		}
		if (Direction.DOWN_LEFT == playerDirection && enemyX < playerX && enemyY < playerY && EnemyActor.StartSide.RIGHT == enemyDirection) {
			return true;
		}
		if (Direction.DOWN_RIGHT == playerDirection && enemyX > playerX && enemyY < playerY && EnemyActor.StartSide.LEFT == enemyDirection) {
			return true;
		}
		if (Direction.UP == playerDirection && enemyY > playerY) {
			if (enemyX < playerX && EnemyActor.StartSide.RIGHT == enemyDirection)
				return true;
			if (enemyX > playerX && EnemyActor.StartSide.LEFT == enemyDirection)
				return true;
		}
		if (Direction.DOWN == playerDirection && enemyY < playerY) {
			if (enemyX < playerX && EnemyActor.StartSide.RIGHT == enemyDirection)
				return true;
			if (enemyX > playerX && EnemyActor.StartSide.LEFT == enemyDirection)
				return true;
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
			return true;
		}
		if (Direction.UP_RIGHT == playerDirection && (playerX - enemyX < 150) && enemyY > playerY && EnemyActor.StartSide.RIGHT == enemyDirection) {
			return true;
		}
		if (Direction.DOWN_LEFT == playerDirection && (enemyX - playerX < 150) && enemyY < playerY && EnemyActor.StartSide.LEFT == enemyDirection) {
			return true;
		}
		if (Direction.DOWN_RIGHT == playerDirection && (playerX - enemyX < 150) && enemyY < playerY && EnemyActor.StartSide.LEFT == enemyDirection) {
			return true;
		}
		return false;
	}


}
