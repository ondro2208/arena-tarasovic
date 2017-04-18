package sk.tuke.game.pong.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import kpi.openlab.arena.impl.BotImpl;
import kpi.openlab.arena.interfaces.Bot;
import sk.tuke.game.pong.arena.GameInfo;
import sk.tuke.game.pong.arena.PongGame;
import sk.tuke.game.pong.interfaces.PlayerActions;
import sk.tuke.game.pong.student.StudentPlayer;
import sk.tuke.game.pong.student.StudentPlayer1;
import sk.tuke.game.pong.student.StudentPlayer2;

import java.util.ArrayList;
import java.util.List;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = GameInfo.GAME_NAME;
		config.width = GameInfo.GAME_WIDTH;
		config.height = GameInfo.GAME_HEIGHT;
		config.forceExit = false;

		BotImpl<PlayerActions> player1 = new BotImpl<>(1, "Janko", new StudentPlayer());
		BotImpl<PlayerActions> player2 = new BotImpl<>(2, "Hrasko", new StudentPlayer1());
		BotImpl<PlayerActions> player3 = new BotImpl<>(3, "Kapusta", new StudentPlayer2());
		List<Bot<PlayerActions>> playerBots = new ArrayList<>();
		playerBots.add(player1);
		playerBots.add(player2);
		playerBots.add(player3);
		new LwjglApplication(new PongGame(playerBots), config);
	}
}
