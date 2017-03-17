package sk.tuke.game.pong.student;

import com.badlogic.gdx.math.Vector2;
import sk.tuke.game.pong.arena.Direction;
import sk.tuke.game.pong.arena.GameInfo;
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
		Map.Entry<Float, Enemy> entry;
		if (map.size() > 0) {
			entry = map.entrySet().iterator().next();
			return entry.getKey() < 200;
		} else return false;
	}
}
