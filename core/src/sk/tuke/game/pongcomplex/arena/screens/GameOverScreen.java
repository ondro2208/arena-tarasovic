package sk.tuke.game.pongcomplex.arena.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import kpi.openlab.arena.interfaces.Bot;
import kpi.openlab.arena.interfaces.BotResult;
import sk.tuke.game.pongcomplex.arena.GameInfo;
import sk.tuke.game.pongcomplex.interfaces.PlayerActions;

import java.util.List;

/**
 * Last screen in game. Shows results and after five seconds game end.
 */
public class GameOverScreen implements Screen {

	private final Texture backgroundImage;
	private OrthographicCamera gameCam;
	private FitViewport gamePort;
	private Stage gameStage;
	private long time;
	private BitmapFont myFont;

	/**
	 * Create table with results.
	 */
	public GameOverScreen(List<BotResult> results, List<Bot<PlayerActions>> bots) {
		time = System.currentTimeMillis();
		gameCam = new OrthographicCamera();
		gamePort = new FitViewport(GameInfo.GAME_WIDTH / GameInfo.PPM, GameInfo.GAME_HEIGHT / GameInfo.PPM, gameCam);
		backgroundImage = new Texture(Gdx.files.internal("bg.png"));
		gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
		gameStage = new Stage();

		generateFont(70);
		Label gameTitle = new Label("PongComplex", new Label.LabelStyle(myFont, Color.YELLOW));
		gameTitle.setAlignment(Align.center);
		gameTitle.setBounds(GameInfo.GAME_WIDTH / 2 - 50, GameInfo.GAME_HEIGHT / 2 + 100, 100, 50);
		gameStage.addActor(gameTitle);

		generateFont(30);
		Table table = new Table();
		table.setFillParent(true);
		table.row();
		for (BotResult result : results) {
			for (Bot<PlayerActions> bot : bots) {
				if (result.getBotId() == bot.getId()) {
					table.add(new Label(bot.getName(), new Label.LabelStyle(myFont, Color.WHITE))).expandX();
					table.add(new Label(String.format("%03d", result.getScore()), new Label.LabelStyle(myFont, Color.WHITE))).expandX();
					break;
				}
			}
			table.row();
		}
		table.align(Align.center);
		gameStage.addActor(table);
	}

	/**
	 * Generate font, which is used to draw text.
	 * @param size size used in draw text
	 */
	private void generateFont(int size) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/myFont.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = size;
		myFont = generator.generateFont(parameter); // font size 12 pixels
		generator.dispose();
	}

	@Override
	public void show() {

	}

	/**
	 * Draw text, count seconds and end game.
	 */
	@Override
	public void render(float delta) {
		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}
		gameStage.getBatch().begin();
		gameStage.getBatch().draw(backgroundImage, 0, 0, GameInfo.GAME_WIDTH, GameInfo.GAME_HEIGHT);
		gameStage.getBatch().end();
		gameStage.draw();
		if (System.currentTimeMillis() > time + 5000) {
			Gdx.app.exit();
		}
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		backgroundImage.dispose();
		gameStage.dispose();
	}
}
