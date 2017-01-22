package sk.tuke.game.pong.arena;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class PongGame extends ApplicationAdapter {

	public static final int GAME_WIDTH = 1920;
	public static final int GAME_HEIGHT = 1080;
	public static final String GAME_NAME = "Pong";

	Stage gameStage;
	PlayerActor player;
	World world;


	@Override
	public void create () {
		gameStage = new Stage(new ScreenViewport());
		player = new PlayerActor();
		gameStage.addActor(player);
		world = new World(new Vector2(0, 0), true);

		player.createBody(world);
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gameStage.draw();
	}
	
	@Override
	public void dispose () {

	}
}
