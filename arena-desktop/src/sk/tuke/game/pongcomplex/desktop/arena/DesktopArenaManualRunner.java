package sk.tuke.game.pongcomplex.desktop.arena;

import kpi.openlab.arena.StartArena;
import sk.tuke.game.pongcomplex.bot.Bot1;
import sk.tuke.game.pongcomplex.bot.Bot2;

/**
 * Run the game by Arena runner
 */
public class DesktopArenaManualRunner {
	/**
	 * Run the game with student player class.
	 *
	 * @param student Class of student player.
	 */
	public static void run(Class student) {
		if (student == null) {
			return;
		}
		kpi.openlab.arena.StartArena startArena = new StartArena();
		startArena.start(PongArenaMain.class, student, Bot1.class, Bot2.class);
	}
}
