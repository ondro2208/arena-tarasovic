package sk.tuke.game.pong.arena.actors;

import kpi.openlab.arena.interfaces.Bot;
import sk.tuke.game.pong.interfaces.PlayerActions;

/**
 * Created by otara on 14.4.2017.
 */
public class ComplexPlayer {
	PlayerActor actor;
	PlayerActions student;
	String name;
	long id;

	public ComplexPlayer(PlayerActor actor, Bot<PlayerActions> student, String name, long id) {
		this.actor = actor;
		this.student = student.getBotInstance();
		this.name = name;
		this.id = id;
	}

	public ComplexPlayer(PlayerActor actor, PlayerActions student, String name, long id) {
		this.actor = actor;
		this.student = student;
		this.name = name;
		this.id = id;
	}

	public PlayerActor getActor() {
		return actor;
	}

	public PlayerActions getStudent() {
		return student;
	}

	public String getName() {
		return name;
	}

	public long getId() {
		return id;
	}
}
