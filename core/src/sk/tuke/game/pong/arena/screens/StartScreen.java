package sk.tuke.game.pong.arena.screens;

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
import sk.tuke.game.pong.arena.GameInfo;
import sk.tuke.game.pong.arena.PongGame;
import sk.tuke.game.pong.interfaces.PlayerActions;

import java.util.List;

/**
 * Created by otara on 17.4.2017.
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
		counterLabel = new Label(String.format("%d", counter), textStyle);
		counterLabel.setBounds(GameInfo.GAME_WIDTH / 2 - 50, GameInfo.GAME_HEIGHT / 2 - 100, 100, 50);
		counterLabel.setAlignment(Align.center);
		stage.addActor(counterLabel);

	}

	private void generateFont(Color color) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/myFont.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 70;
		parameter.color = color;
		font = generator.generateFont(parameter); // font size 12 pixels
		generator.dispose();
	}

	@Override
	public void show() {

	}

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

	}
}
