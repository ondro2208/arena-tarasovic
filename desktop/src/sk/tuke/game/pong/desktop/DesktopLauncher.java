package sk.tuke.game.pong.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import sk.tuke.game.pong.arena.GameInfo;
import sk.tuke.game.pong.arena.PongGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = GameInfo.GAME_NAME;
		config.width = GameInfo.GAME_WIDTH;
		config.height = GameInfo.GAME_HEIGHT;
		new LwjglApplication(new PongGame(), config);
	}
}
