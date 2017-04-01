package sk.tuke.game.pong.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import kpi.openlab.arena.impl.BotImpl;
import sk.tuke.game.pong.arena.GameInfo;
import sk.tuke.game.pong.arena.PongGame;
import sk.tuke.game.pong.interfaces.PlayerActions;
import sk.tuke.game.pong.student.StudentPlayer;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = GameInfo.GAME_NAME;
		config.width = GameInfo.GAME_WIDTH;
		config.height = GameInfo.GAME_HEIGHT;
		config.forceExit = false;

		BotImpl<PlayerActions> player1 = new BotImpl<>(1, "StudentName", new StudentPlayer());
		new LwjglApplication(new PongGame(player1), config);
	}
}
