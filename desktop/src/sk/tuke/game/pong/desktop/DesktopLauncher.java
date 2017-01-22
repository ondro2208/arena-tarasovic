package sk.tuke.game.pong.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import sk.tuke.game.pong.arena.PongGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = PongGame.GAME_NAME;
		config.width = PongGame.GAME_WIDTH;
		config.height = PongGame.GAME_HEIGHT;
		new LwjglApplication(new PongGame(), config);
	}
}
