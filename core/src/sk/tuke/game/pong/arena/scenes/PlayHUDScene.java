package sk.tuke.game.pong.arena.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import sk.tuke.game.pong.arena.GameInfo;
import sk.tuke.game.pong.arena.actors.ComplexPlayer;

import java.util.ArrayList;

/**
 * Created by otara on 14.4.2017.
 */
public class PlayHUDScene {
	public Stage stage;
	private Table table;
	private Viewport viewport;
	private Label score;
	private Label name;
	private Label emptyLabel;
	private ArrayList<ComplexPlayer> complexPlayers;

	public PlayHUDScene(SpriteBatch sb, ArrayList<ComplexPlayer> complexPlayers) {
		viewport = new FitViewport(GameInfo.GAME_WIDTH, GameInfo.GAME_HEIGHT, new OrthographicCamera());
		stage = new Stage(viewport, sb);
		emptyLabel = new Label("", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		this.complexPlayers = complexPlayers;

		table = new Table();
		table.top();
		table.setFillParent(true);
		table.padTop(20);
		table.align(Align.center | Align.top);
		update();
	}

	public Stage getStage() {
		return stage;
	}

	public void update() {
		table.clear();
		//names
		for (int i = 0; i < complexPlayers.size(); i++) {
			name = new Label(complexPlayers.get(i).getName(), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
			table.add(name).expandX();
		}
		table.row();
		//scores
		for (int i = 0; i < complexPlayers.size(); i++) {
			score = new Label(String.format("%03d", complexPlayers.get(i).getActor().getScore()), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
			table.add(score).expandX();
		}
		stage.addActor(table);
	}

}
