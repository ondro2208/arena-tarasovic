package sk.tuke.game.pongcomplex.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import kpi.openlab.arena.impl.BotImpl;
import kpi.openlab.arena.interfaces.Bot;
import sk.tuke.game.pongcomplex.arena.GameInfo;
import sk.tuke.game.pongcomplex.arena.PongGame;
import sk.tuke.game.pongcomplex.bot.Bot1;
import sk.tuke.game.pongcomplex.bot.Bot3;
import sk.tuke.game.pongcomplex.interfaces.PlayerActions;

import java.util.ArrayList;
import java.util.List;

/**
 * Run the game by Desktop runner
 */
public class DesktopLauncher {
	/**
	 * Run the game with student player on specific position.
	 *
	 * @param student  Student implementation.
	 * @param name     Student name.
	 * @param position Number which represent position of student player.
	 */
	public static void run(PlayerActions student, String name, int position) {
		if (student == null || name == null) {
			return;
		}
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = GameInfo.GAME_NAME;
		config.width = GameInfo.GAME_WIDTH;
		config.height = GameInfo.GAME_HEIGHT;
		config.forceExit = false;

		List<Bot<PlayerActions>> players = new ArrayList<>();
		BotImpl<PlayerActions> player1, player2, player3;
		switch (position) {
			case 1: {
				player1 = new BotImpl<>(1, name, student);
				player2 = new BotImpl<>(2, "BOT 1", new Bot1());
				player3 = new BotImpl<>(3, "BOT 2", new Bot3());
				break;
			}
			case 2: {
				player1 = new BotImpl<>(1, "BOT 1", new Bot1());
				player2 = new BotImpl<>(2, name, student);
				player3 = new BotImpl<>(3, "BOT 2", new Bot3());
				break;
			}
			case 3: {
				player1 = new BotImpl<>(1, "BOT 1", new Bot1());
				player2 = new BotImpl<>(2, "BOT 2", new Bot3());
				player3 = new BotImpl<>(3, name, student);
				break;
			}
			default: {
				player1 = new BotImpl<>(1, name, student);
				player2 = new BotImpl<>(2, "BOT 1", new Bot1());
				player3 = new BotImpl<>(3, "BOT 2", new Bot3());
			}
		}
		players.add(player1);
		players.add(player2);
		players.add(player3);

		new LwjglApplication(new PongGame(players), config);
	}

	/**
	 * Run the game with student player and chosen bots.
	 *
	 * @param student Student implementation.
	 * @param name    Student name.
	 * @param bot1    Chosen bot.
	 * @param bot2    Chosen bot.
	 */
	public static void run(PlayerActions student, String name, PlayerActions bot1, PlayerActions bot2) {
		if (student == null || name == null) {
			return;
		}
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = GameInfo.GAME_NAME;
		config.width = GameInfo.GAME_WIDTH;
		config.height = GameInfo.GAME_HEIGHT;
		config.forceExit = false;
		List<Bot<PlayerActions>> players = new ArrayList<>();
		if (bot1 != null) {
			players.add(new BotImpl<>(1, name, student));
			if (bot2 != null) {
				players.add(new BotImpl<>(2, "BOT 1", bot1));
				players.add(new BotImpl<>(3, "BOT 2", bot2));
			} else {
				players.add(new BotImpl<>(3, "BOT 1", bot1));
			}
		} else {
			if (bot2 != null) {
				players.add(new BotImpl<>(1, name, student));
				players.add(new BotImpl<>(3, "BOT 2", bot2));
			} else {
				players.add(new BotImpl<>(1, name, student));
			}
		}
		new LwjglApplication(new PongGame(players), config);
	}
}
