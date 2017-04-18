package sk.tuke.game.pong.arena;

import sk.tuke.game.pong.arena.actors.PlayerActor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by otara on 16.4.2017.
 */
public class MoveHelper {

	public static List<Direction> getAllowedNextDirections(Direction direction) {
		List<Direction> allowedDirections = new ArrayList<Direction>();
		switch (direction) {
			case UP_LEFT: {
				allowedDirections.add(Direction.DOWN);
				allowedDirections.add(Direction.DOWN_LEFT);
				break;
			}
			case UP: {
				allowedDirections.add(Direction.DOWN);
				allowedDirections.add(Direction.DOWN_LEFT);
				allowedDirections.add(Direction.DOWN_RIGHT);
				break;
			}
			case UP_RIGHT: {
				allowedDirections.add(Direction.DOWN);
				allowedDirections.add(Direction.DOWN_RIGHT);
				break;
			}
			case DOWN_LEFT: {
				allowedDirections.add(Direction.UP);
				allowedDirections.add(Direction.UP_LEFT);
				break;
			}
			case DOWN: {
				allowedDirections.add(Direction.UP);
				allowedDirections.add(Direction.UP_LEFT);
				allowedDirections.add(Direction.UP_RIGHT);
				break;
			}
			case DOWN_RIGHT: {
				allowedDirections.add(Direction.UP);
				allowedDirections.add(Direction.UP_RIGHT);
				break;
			}

		}
		return allowedDirections;
	}

	public static void turnBack(PlayerActor player) {
		switch (player.getDirection()) {
			case UP_LEFT: {
				if (isTargetCoordinates(player.getTargetVector(), GameInfo.positions[0][0], GameInfo.positions[0][1] - GameInfo.POINT_OFFSET)) {
					player.updateVector(GameInfo.positions[4][0], GameInfo.positions[4][1] + GameInfo.POINT_OFFSET);
					player.setDirection(Direction.DOWN_RIGHT);
					break;
				}
				if (isTargetCoordinates(player.getTargetVector(), GameInfo.positions[1][0], GameInfo.positions[1][1] - GameInfo.POINT_OFFSET)) {
					player.updateVector(GameInfo.positions[5][0], GameInfo.positions[5][1] + GameInfo.POINT_OFFSET);
					player.setDirection(Direction.DOWN_RIGHT);
					break;
				}
			}
			case UP: {
				if (isTargetCoordinates(player.getTargetVector(), GameInfo.positions[0][0], GameInfo.positions[0][1] - GameInfo.POINT_OFFSET)) {
					player.updateVector(GameInfo.positions[3][0], GameInfo.positions[3][1] + GameInfo.POINT_OFFSET);
					player.setDirection(Direction.DOWN);
					break;
				}
				if (isTargetCoordinates(player.getTargetVector(), GameInfo.positions[1][0], GameInfo.positions[1][1] - GameInfo.POINT_OFFSET)) {
					player.updateVector(GameInfo.positions[4][0], GameInfo.positions[4][1] + GameInfo.POINT_OFFSET);
					player.setDirection(Direction.DOWN);
					break;
				}
				if (isTargetCoordinates(player.getTargetVector(), GameInfo.positions[2][0], GameInfo.positions[2][1] - GameInfo.POINT_OFFSET)) {
					player.updateVector(GameInfo.positions[5][0], GameInfo.positions[5][1] + GameInfo.POINT_OFFSET);
					player.setDirection(Direction.DOWN);
					break;
				}
			}
			case UP_RIGHT: {
				if (isTargetCoordinates(player.getTargetVector(), GameInfo.positions[1][0], GameInfo.positions[1][1] - GameInfo.POINT_OFFSET)) {
					player.updateVector(GameInfo.positions[3][0], GameInfo.positions[3][1] + GameInfo.POINT_OFFSET);
					player.setDirection(Direction.DOWN_LEFT);
				}
				if (isTargetCoordinates(player.getTargetVector(), GameInfo.positions[2][0], GameInfo.positions[2][1] - GameInfo.POINT_OFFSET)) {
					player.updateVector(GameInfo.positions[4][0], GameInfo.positions[4][1] + GameInfo.POINT_OFFSET);
					player.setDirection(Direction.DOWN_LEFT);
				}
				break;
			}
			case DOWN_LEFT: {
				if (isTargetCoordinates(player.getTargetVector(), GameInfo.positions[3][0], GameInfo.positions[3][1] + GameInfo.POINT_OFFSET)) {
					player.updateVector(GameInfo.positions[1][0], GameInfo.positions[1][1] - GameInfo.POINT_OFFSET);
					player.setDirection(Direction.UP_RIGHT);
				}
				if (isTargetCoordinates(player.getTargetVector(), GameInfo.positions[4][0], GameInfo.positions[4][1] + GameInfo.POINT_OFFSET)) {
					player.updateVector(GameInfo.positions[2][0], GameInfo.positions[2][1] - GameInfo.POINT_OFFSET);
					player.setDirection(Direction.UP_RIGHT);
				}
				break;
			}
			case DOWN: {
				if (isTargetCoordinates(player.getTargetVector(), GameInfo.positions[3][0], GameInfo.positions[3][1] + GameInfo.POINT_OFFSET)) {
					player.updateVector(GameInfo.positions[1][0], GameInfo.positions[1][1] - GameInfo.POINT_OFFSET);
					player.setDirection(Direction.UP);
					break;
				}
				if (isTargetCoordinates(player.getTargetVector(), GameInfo.positions[4][0], GameInfo.positions[4][1] + GameInfo.POINT_OFFSET)) {
					player.updateVector(GameInfo.positions[1][0], GameInfo.positions[1][1] - GameInfo.POINT_OFFSET);
					player.setDirection(Direction.UP);
					break;
				}
				if (isTargetCoordinates(player.getTargetVector(), GameInfo.positions[5][0], GameInfo.positions[5][1] + GameInfo.POINT_OFFSET)) {
					player.updateVector(GameInfo.positions[2][0], GameInfo.positions[2][1] - GameInfo.POINT_OFFSET);
					player.setDirection(Direction.UP);
					break;
				}
			}
			case DOWN_RIGHT: {
				if (isTargetCoordinates(player.getTargetVector(), GameInfo.positions[4][0], GameInfo.positions[4][1] + GameInfo.POINT_OFFSET)) {
					player.updateVector(GameInfo.positions[0][0], GameInfo.positions[0][1] - GameInfo.POINT_OFFSET);
					player.setDirection(Direction.UP_LEFT);
					break;
				}
				if (isTargetCoordinates(player.getTargetVector(), GameInfo.positions[5][0], GameInfo.positions[5][1] + GameInfo.POINT_OFFSET)) {
					player.updateVector(GameInfo.positions[1][0], GameInfo.positions[1][1] - GameInfo.POINT_OFFSET);
					player.setDirection(Direction.UP_LEFT);
					break;
				}
			}
		}
	}

	private static boolean isTargetCoordinates(float[] player, float pointX, float pointY) {
		return (player[0] == pointX && player[1] == pointY);
	}

	public static void setTarget(PlayerActor playerActor, Direction newDirection) {
		int playerColumn = inColumn(playerActor);
		if (playerColumn == -1)
			return;
		float x, y;
		switch (newDirection) {
			case UP_LEFT: {
				x = GameInfo.positions[playerColumn - 1][0];
				y = GameInfo.positions[playerColumn - 1][1] - GameInfo.POINT_OFFSET;
				break;
			}
			case UP: {
				x = GameInfo.positions[playerColumn][0];
				y = GameInfo.positions[playerColumn][1] - GameInfo.POINT_OFFSET;
				break;
			}
			case UP_RIGHT: {
				x = GameInfo.positions[playerColumn + 1][0];
				y = GameInfo.positions[playerColumn + 1][1] - GameInfo.POINT_OFFSET;
				break;
			}
			case DOWN_LEFT: {
				x = GameInfo.positions[playerColumn + 2][0];
				y = GameInfo.positions[playerColumn + 2][1] + GameInfo.POINT_OFFSET;
				break;
			}
			case DOWN: {
				x = GameInfo.positions[playerColumn + 3][0];
				y = GameInfo.positions[playerColumn + 3][1] + GameInfo.POINT_OFFSET;
				break;
			}
			case DOWN_RIGHT: {
				x = GameInfo.positions[playerColumn + 4][0];
				y = GameInfo.positions[playerColumn + 4][1] + GameInfo.POINT_OFFSET;
				break;
			}
			default: {
				x = GameInfo.positions[playerColumn][0];
				y = GameInfo.positions[playerColumn][1] - GameInfo.POINT_OFFSET;
			}
		}
		playerActor.updateVector(x, y);
	}

	private static int inColumn(PlayerActor playerActor) {
		int offset = 50;
		if (playerActor.getPlayerX() > GameInfo.positions[0][0] - offset && playerActor.getPlayerX() < GameInfo.positions[0][0] + offset)
			return 0;
		if (playerActor.getPlayerX() > GameInfo.positions[1][0] - offset && playerActor.getPlayerX() < GameInfo.positions[1][0] + offset)
			return 1;
		if (playerActor.getPlayerX() > GameInfo.positions[2][0] - offset && playerActor.getPlayerX() < GameInfo.positions[2][0] + offset)
			return 2;
		return -1;
	}
}
