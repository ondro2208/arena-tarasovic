package sk.tuke.game.pong.desktop.arena;

import kpi.openlab.arena.StartArena;
import sk.tuke.game.pong.student.StudentPlayer;

/**
 * Created by otara on 1.4.2017.
 */
public class DesktopArenaManualRunner {
	public static void main(String[] args) {
		kpi.openlab.arena.StartArena startArena = new StartArena();
		startArena.start(PongArenaMain.class, StudentPlayer.class);
	}
}
