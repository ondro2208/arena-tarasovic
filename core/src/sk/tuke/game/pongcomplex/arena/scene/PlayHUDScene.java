package sk.tuke.game.pongcomplex.arena.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import sk.tuke.game.pongcomplex.arena.GameInfo;
import sk.tuke.game.pongcomplex.arena.actors.PlayerActor;

import java.util.ArrayList;

/**
 * Draw actual score and names during gameplay.
 */
public class PlayHUDScene {
	public Stage stage;
	private Table table;
	private Viewport viewport;
	private Label score;
	private Label name;
	private ArrayList<PlayerActor> players;
	private BitmapFont myFont;

	/**
	 * Prepare name and score to each player.
	 *
	 * @param sb      batch used in game
	 * @param players list of actual player
	 */
	public PlayHUDScene(SpriteBatch sb, ArrayList<PlayerActor> players) {
		viewport = new FitViewport(GameInfo.GAME_WIDTH, GameInfo.GAME_HEIGHT, new OrthographicCamera());
		stage = new Stage(viewport, sb);
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/myFont.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 16;
		myFont = generator.generateFont(parameter); // font size 12 pixels
		generator.dispose();
		this.players = players;

		table = new Table();
		table.top();
		table.setFillParent(true);
		table.padTop(20);
		table.align(Align.center | Align.top);
		update();
	}

	/**
	 * Stage which should be updated during game.
	 * @return Stage used to draw score and names.
	 */
	public Stage getStage() {
		return stage;
	}

	/**
	 * Update view of scene to actual players.
	 */
	public void update() {
		table.clear();
		//names
		for (int i = 0; i < players.size(); i++) {
			name = new Label(players.get(i).getName(), new Label.LabelStyle(myFont, Color.YELLOW));
			table.add(name).expandX();
		}
		table.row();
		//scores
		for (int i = 0; i < players.size(); i++) {
			score = new Label(String.format("%03d", players.get(i).getScore()), new Label.LabelStyle(myFont, Color.WHITE));
			table.add(score).expandX();
		}
		stage.addActor(table);
	}

}
