package sk.tuke.game.pongcomplex.arena;

import sk.tuke.game.pongcomplex.arena.actors.PlayerActor;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains logic of moving player to proper directions.
 */
public class MoveHelper {
	/**
	 * Takes into account current direction and produce directions in which can player continue.
	 *
	 * @param direction Actual direction.
	 * @return List of directions in which player can continue.
	 */
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

	/**
	 * Set previous target with according direction.
	 * @param player Player with actual position and direction.
	 */
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

	/**
	 * Check if point coordinates is player target.
	 */
	private static boolean isTargetCoordinates(float[] playerTarget, float pointX, float pointY) {
		return (playerTarget[0] == pointX && playerTarget[1] == pointY);
	}

	/**
	 * Set target where player will move according to player position and next direction.
	 * @param playerActor Player with actual position.
	 * @param newDirection direction in which player will continue.
	 */
	public static void setTarget(PlayerActor playerActor, Direction newDirection) {
		if (getCurrentColumn(playerActor) == -1)
			return;
		float x;
		switch (newDirection) {
			case UP_LEFT: {
				x = (GameInfo.GAME_WIDTH / 4) * (getCurrentColumn(playerActor) - 1);
				break;
			}
			case UP: {
				x = (GameInfo.GAME_WIDTH / 4) * getCurrentColumn(playerActor);
				break;
			}
			case UP_RIGHT: {
				x = (GameInfo.GAME_WIDTH / 4) * (getCurrentColumn(playerActor) + 1);
				break;
			}
			case DOWN_LEFT: {
				x = (GameInfo.GAME_WIDTH / 4) * (getCurrentColumn(playerActor) - 1);
				break;
			}
			case DOWN: {
				x = (GameInfo.GAME_WIDTH / 4) * getCurrentColumn(playerActor);
				break;
			}
			case DOWN_RIGHT: {
				x = (GameInfo.GAME_WIDTH / 4) * (getCurrentColumn(playerActor) + 1);
				break;
			}
			default: {
				x = (GameInfo.GAME_WIDTH / 4) * (getCurrentColumn(playerActor) - 1);
			}
		}
		playerActor.updateVector(x, getNextY(playerActor));
	}

	/**
	 * Locate player's y coordinate on game field.
	 * @param playerActor Player with actual position.
	 * @return The nearest point's y coordinate to player's y coordinate.
	 */
	private static float getNextY(PlayerActor playerActor) {
		boolean isUp = playerActor.getPlayerY() > (GameInfo.GAME_HEIGHT / 2);
		if (!isUp) {
			return GameInfo.GAME_HEIGHT - (GameInfo.GAME_HEIGHT / 4) - GameInfo.POINT_OFFSET;
		} else {
			return (GameInfo.GAME_HEIGHT / 4) + GameInfo.POINT_OFFSET;
		}
	}

	/**
	 * Locate player's x coordinate on game field.
	 * @param playerActor Player with actual position.
	 * @return Order of column in which player is located.
	 */
	private static int getCurrentColumn(PlayerActor playerActor) {
		int offset = GameInfo.GAME_WIDTH / 8;
		if (playerActor.getPlayerX() >= GameInfo.positions[0][0] - offset && playerActor.getPlayerX() <= GameInfo.positions[0][0] + offset)
			return 1;
		if (playerActor.getPlayerX() >= GameInfo.positions[1][0] - offset && playerActor.getPlayerX() <= GameInfo.positions[1][0] + offset)
			return 2;
		if (playerActor.getPlayerX() > GameInfo.positions[2][0] - offset && playerActor.getPlayerX() < GameInfo.positions[2][0] + offset)
			return 3;
		return -1;
	}

}
