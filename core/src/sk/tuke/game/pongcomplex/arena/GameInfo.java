package sk.tuke.game.pongcomplex.arena;

/**
 * Informative class about game.
 */
public class GameInfo {
	public static final String GAME_NAME = "PongComplex";
	public static int GAME_WIDTH = 1280;
	public static int GAME_HEIGHT = 720;
	public static int PPM = 2;
	public static final short FILTER_PLAYER_BIT = 1;
	public static final short FILTER_POINT_BIT = 2;
	public static final short FILTER_TRAMPOLINE_BIT = 4;
	public static final short FILTER_ENEMY_BIT = 8;
	public static final int POINT_OFFSET = GameInfo.GAME_WIDTH / 75;
	public static float[][] positions = new float[][]{
			{GameInfo.GAME_WIDTH / 4, GameInfo.GAME_HEIGHT - (GameInfo.GAME_HEIGHT / 4)},
			{GameInfo.GAME_WIDTH / 2, GameInfo.GAME_HEIGHT - (GameInfo.GAME_HEIGHT / 4)},
			{GameInfo.GAME_WIDTH - (GameInfo.GAME_WIDTH / 4), GameInfo.GAME_HEIGHT - (GameInfo.GAME_HEIGHT / 4)},
			{GameInfo.GAME_WIDTH / 4, GameInfo.GAME_HEIGHT / 4},
			{GameInfo.GAME_WIDTH / 2, GameInfo.GAME_HEIGHT / 4},
			{GameInfo.GAME_WIDTH - (GameInfo.GAME_WIDTH / 4), GameInfo.GAME_HEIGHT / 4}
	};

}
