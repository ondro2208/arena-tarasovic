package sk.tuke.game.pong.student;

import sk.tuke.game.pong.arena.DirAndPos;
import sk.tuke.game.pong.arena.GameInfo;
import sk.tuke.game.pong.interfaces.PlayerActions;
import sk.tuke.game.pong.interfaces.Point;

/**
 * Created by otara on 24.2.2017.
 */
public class Player implements PlayerActions {

	private final int columnDistance = GameInfo.GAME_WIDTH / 4;

	@Override
	public DirAndPos getPathToPoint(DirAndPos dir, Point point, sk.tuke.game.pong.interfaces.Player player) {
		float playerX = player.getPlayerX();
		float playerY = player.getPlayerY();
		float pointX = point.getPointX();
		float pointY = point.getPointY();
		DirAndPos nextDirection = DirAndPos.UP;

		float offset = 50;

		if (isUp(pointY)) {
			if (playerX - offset < pointX && playerX + offset > pointX) {
				nextDirection = DirAndPos.UP;
			}
			if (playerX - offset - columnDistance < pointX && playerX + offset - columnDistance > pointX) {
				if (playerY + offset > pointY && playerY - offset < pointY)
					nextDirection = DirAndPos.DOWN_LEFT;
				else
					nextDirection = DirAndPos.UP_LEFT;
			}
			if (playerX - offset + columnDistance < pointX && playerX + offset + columnDistance > pointX) {
				if (playerY + offset > pointY && playerY - offset < pointY)
					nextDirection = DirAndPos.DOWN_RIGHT;
				else
					nextDirection = DirAndPos.UP_RIGHT;
			}
			if (playerX - offset - 2 * columnDistance < pointX && playerX + offset - 2 * columnDistance > pointX) {
				if (playerY + offset > pointY && playerY - offset < pointY)
					nextDirection = DirAndPos.DOWN_LEFT;
				else
					nextDirection = DirAndPos.UP_LEFT;
			}
			if (playerX - offset + 2 * columnDistance < pointX && playerX + offset + 2 * columnDistance > pointX) {
				if (playerY + offset > pointY && playerY - offset < pointY)
					nextDirection = DirAndPos.DOWN_RIGHT;
				else
					nextDirection = DirAndPos.UP_RIGHT;
			}
		} else {
			if (playerX - offset < pointX && playerX + offset > pointX) {
				nextDirection = DirAndPos.DOWN;
			}
			if (playerX - offset - columnDistance < pointX && playerX + offset - columnDistance > pointX) {
				if (playerY + offset > pointY && playerY - offset < pointY)
					nextDirection = DirAndPos.UP_LEFT;
				else
					nextDirection = DirAndPos.DOWN_LEFT;
			}
			if (playerX - offset + columnDistance < pointX && playerX + offset + columnDistance > pointX) {
				if (playerY + offset > pointY && playerY - offset < pointY)
					nextDirection = DirAndPos.UP_RIGHT;
				else
					nextDirection = DirAndPos.DOWN_RIGHT;
			}
			if (playerX - offset - 2 * columnDistance < pointX && playerX + offset - 2 * columnDistance > pointX) {
				if (playerY + offset > pointY && playerY - offset < pointY)
					nextDirection = DirAndPos.UP_LEFT;
				else
					nextDirection = DirAndPos.DOWN_LEFT;
			}
			if (playerX - offset + 2 * columnDistance < pointX && playerX + offset + 2 * columnDistance > pointX) {
				if (playerY + offset > pointY && playerY - offset < pointY)
					nextDirection = DirAndPos.UP_RIGHT;
				else
					nextDirection = DirAndPos.DOWN_RIGHT;
			}
		}

		return nextDirection;
	}

	private boolean isUp(float pointY) {
		return GameInfo.GAME_HEIGHT / 2 < pointY;
	}
}
