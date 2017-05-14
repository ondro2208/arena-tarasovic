package sk.tuke.game.pong.student;

import com.badlogic.gdx.math.Vector2;
import sk.tuke.game.pong.arena.Direction;
import sk.tuke.game.pong.arena.GameInfo;
import sk.tuke.game.pong.arena.actors.EnemyActor;
import sk.tuke.game.pong.interfaces.Enemy;
import sk.tuke.game.pong.interfaces.PlayerActions;
import sk.tuke.game.pong.interfaces.PlayerInfo;
import sk.tuke.game.pong.interfaces.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by otara on 14.4.2017.
 */
public class StudentPlayer1 implements PlayerActions {

	private final int columnDistance = GameInfo.GAME_WIDTH / 4;
	private int countOfBounce = 0;
	private Direction[] flow = new Direction[]{
			Direction.DOWN_LEFT,
			Direction.UP,
			Direction.DOWN_RIGHT,
			Direction.UP,
			Direction.DOWN_RIGHT,
			Direction.UP,
			Direction.DOWN_LEFT,
			Direction.UP
	};

	@Override
	public Direction getNextDirection(Point point, PlayerInfo player) {
		Direction nextDirection;
		if (countOfBounce > 7)
			countOfBounce = 0;
		switch (countOfBounce) {
			case 0: {
				nextDirection = flow[0];
				break;
			}
			case 1: {
				nextDirection = flow[1];
				break;
			}
			case 2: {
				nextDirection = flow[2];
				break;
			}
			case 3: {
				nextDirection = flow[3];
				break;
			}
			case 4: {
				nextDirection = flow[4];
				break;
			}
			case 5: {
				nextDirection = flow[5];
				break;
			}
			case 6: {
				nextDirection = flow[6];
				break;
			}
			case 7: {
				nextDirection = flow[7];
				break;
			}
			default: {
				nextDirection = flow[1];
			}
		}
		countOfBounce++;
		return nextDirection;
	}

	private boolean isUp(float pointY) {
		return GameInfo.GAME_HEIGHT / 2 < pointY;
	}

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