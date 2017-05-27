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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import kpi.openlab.arena.interfaces.Bot;
import sk.tuke.game.pongcomplex.arena.GameInfo;
import sk.tuke.game.pongcomplex.arena.PongGame;
import sk.tuke.game.pongcomplex.interfaces.PlayerActions;

import java.util.List;

/**
 * First screen after start game. It is like intro into game.
 */
public class StartScreen implements Screen {

	private final OrthographicCamera gameCam;
	private final FitViewport gamePort;
	private final Texture backgroundImage;
	private Stage stage;
	private PongGame game;
	private Label counterLabel;
	private int counter;
	private long time;
	private List<Bot<PlayerActions>> playerBots;
	private BitmapFont font;

	/**
	 * Prepare game title and counter.
	 *
	 * @param game       reference to main game, send to PlayScreen
	 * @param playerBots list contains all players
	 */
	public StartScreen(PongGame game, List<Bot<PlayerActions>> playerBots) {
		this.playerBots = playerBots;
		this.game = game;
		stage = new Stage();
		gameCam = new OrthographicCamera();
		gamePort = new FitViewport(GameInfo.GAME_WIDTH / GameInfo.PPM, GameInfo.GAME_HEIGHT / GameInfo.PPM, gameCam);
		backgroundImage = new Texture(Gdx.files.internal("bg.png"));
		gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
		counter = 3;
		time = System.currentTimeMillis();

		generateFont(Color.YELLOW);
		Label.LabelStyle textStyle = new Label.LabelStyle();
		textStyle.font = font;
		Label gameTitle = new Label("PongComplex", textStyle);
		gameTitle.setAlignment(Align.center);
		gameTitle.setBounds(GameInfo.GAME_WIDTH / 2 - 50, GameInfo.GAME_HEIGHT / 2 + 100, 100, 50);
		stage.addActor(gameTitle);

		generateFont(Color.GREEN);
		textStyle.font = font;
		textStyle = new Label.LabelStyle(font,Color.WHITE);
		counterLabel = new Label(String.format("%d", counter), textStyle);
		counterLabel.setBounds(GameInfo.GAME_WIDTH / 2 - 50, GameInfo.GAME_HEIGHT / 2 - 100, 100, 50);
		counterLabel.setAlignment(Align.center);
		stage.addActor(counterLabel);

	}

	/**
	 * Generate font, which is used to draw text.
	 * @param color color used in draw text
	 */
	private void generateFont(Color color) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/myFont.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 70;
		parameter.color = color;
		font = generator.generateFont(parameter);
		generator.dispose();
	}

	@Override
	public void show() {

	}

	/**
	 * Call in each iteration, update text.
	 */
	@Override
	public void render(float delta) {
		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}

		update();
		stage.getBatch().begin();
		stage.getBatch().draw(backgroundImage, 0, 0, GameInfo.GAME_WIDTH, GameInfo.GAME_HEIGHT);
		stage.getBatch().end();
		stage.draw();
	}

	private void update() {
		if (System.currentTimeMillis() - time > 1000) {
			time = System.currentTimeMillis();
			counter--;
			if (counter == 0) {
				game.setScreen(new PlayScreen(game, playerBots));
			}
			counterLabel.setText(String.format("%d", counter));
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
		stage.dispose();
	}
}
