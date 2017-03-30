package sk.tuke.game.pong.student;

import com.badlogic.gdx.math.Vector2;
import sk.tuke.game.pong.arena.Direction;
import sk.tuke.game.pong.arena.GameInfo;
import sk.tuke.game.pong.arena.actors.EnemyActor;
import sk.tuke.game.pong.interfaces.Enemy;
import sk.tuke.game.pong.interfaces.PlayerActions;
import sk.tuke.game.pong.interfaces.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by otara on 24.2.2017.
 */
public class Player implements PlayerActions {

	private final int columnDistance = GameInfo.GAME_WIDTH / 4;

	@Override
	public Direction getNextDirection(Point point, sk.tuke.game.pong.interfaces.Player player) {
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

	private boolean isUp(float pointY) {
		return GameInfo.GAME_HEIGHT / 2 < pointY;
	}

	@Override
	public boolean turnBack(sk.tuke.game.pong.interfaces.Player actor, ArrayList<Enemy> enemies) {
		HashMap<Float, Enemy> hmap = new HashMap<Float, Enemy>();
		float playerX = actor.getPlayerX();
		float playerY = actor.getPlayerY();
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
					return needChangeNearEnemy(entry.getValue(), actor);
				return needChange(entry.getValue(), actor);
			} else return false;
		}
		return false;
	}

	private boolean needChange(Enemy enemy, sk.tuke.game.pong.interfaces.Player actor) {
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

	private boolean needChangeNearEnemy(Enemy enemy, sk.tuke.game.pong.interfaces.Player actor) {
		float enemyX = enemy.getEnemyX();
		float enemyY = enemy.getEnemyY();
		EnemyActor.StartSide enemyDirection = enemy.getEnemyDirection();
		float playerX = actor.getPlayerX();
		float playerY = actor.getPlayerY();
		Direction playerDirection = actor.getDirection();
		if (Direction.UP_LEFT == playerDirection && (enemyX - playerX < 100) && enemyY > playerY && EnemyActor.StartSide.LEFT == enemyDirection) {
			return true;
		}
		if (Direction.UP_RIGHT == playerDirection && (playerX - enemyX < 100) && enemyY > playerY && EnemyActor.StartSide.RIGHT == enemyDirection) {
			return true;
		}
		if (Direction.DOWN_LEFT == playerDirection && (enemyX - playerX < 100) && enemyY < playerY && EnemyActor.StartSide.LEFT == enemyDirection) {
			return true;
		}
		if (Direction.DOWN_RIGHT == playerDirection && (playerX - enemyX < 100) && enemyY < playerY && EnemyActor.StartSide.LEFT == enemyDirection) {
			return true;
		}
		return false;
	}


}
