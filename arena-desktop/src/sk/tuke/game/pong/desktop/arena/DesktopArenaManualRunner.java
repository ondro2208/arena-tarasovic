package sk.tuke.game.pong.desktop.arena;

import kpi.openlab.arena.StartArena;
import sk.tuke.game.pong.student.StudentPlayer;
import sk.tuke.game.pong.student.StudentPlayer1;
import sk.tuke.game.pong.student.StudentPlayer2;

/**
 * Created by otara on 1.4.2017.
 */
public class DesktopArenaManualRunner {
	public static void main(String[] args) {
		kpi.openlab.arena.StartArena startArena = new StartArena();
		//PlayerActions playerActions = new StudentPlayer();
		//BotImpl<PlayerActions> player1 = new BotImpl<>(1, "StudentName", playerActions);
		//PongGame game = new PongGame(player1);
		//PongArenaMain arena = new PongArenaMain();
		//startArena.start(arena, playerActions);
		startArena.start(PongArenaMain.class, StudentPlayer.class, StudentPlayer1.class, StudentPlayer2.class);
	}
}
