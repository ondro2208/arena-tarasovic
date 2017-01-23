package sk.tuke.game.pong.arena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by otara on 23.1.2017.
 */
public class BackgroundImageActor extends Actor {

	private final Sprite sprite;
	private Texture backgroundImage;

	public BackgroundImageActor() {
		backgroundImage = new Texture(Gdx.files.internal("background.jpg"));
		sprite = new Sprite(backgroundImage);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(sprite,0,0, PongGame.GAME_WIDTH,PongGame.GAME_HEIGHT);
	}
}
