package sk.tuke.game.pong.arena.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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

		Label.LabelStyle textStyle;
		BitmapFont font = new BitmapFont();

		textStyle = new Label.LabelStyle();
		textStyle.font = font;

		counterLabel = new Label(String.format("%d", counter), textStyle);
		counterLabel.setBounds(GameInfo.GAME_WIDTH / 2 - 50, GameInfo.GAME_HEIGHT / 2 - 25, 100, 50);
		counterLabel.setFontScale(1.5f, 1.5f);
		counterLabel.setAlignment(Align.center);
		stage.addActor(counterLabel);
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
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
