package sk.tuke.game.pong.arena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by otara on 22.1.2017.
 */
public class PlayerActor extends BodyTemplate {

	Texture image;
	Sprite sprite;

	public PlayerActor() {
		image = new Texture(Gdx.files.internal("TU.jpg"));
		sprite = new Sprite(image);

		sprite.setPosition(Gdx.graphics.getWidth() / 2 - sprite.getWidth() / 2,
				Gdx.graphics.getHeight() / 2);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		//super.draw(batch, parentAlpha);
		batch.draw(image,0,0);
	}

	@Override
	public Body createBody(World world) {

	}

	@Override
	protected BodyDef getBodyDef() {
		BodyDef bd = new BodyDef();
		bd.position.set(800, 800);
		bd.type = BodyDef.BodyType.DynamicBody;
		return bd;
	}

	@Override
	protected FixtureDef getFixtureDef() {
		FixtureDef fd = new FixtureDef();
		fd.density = 100f;
		fd.friction = 0.001f;
		fd.restitution = 1.06f;
		return fd;
	}


}
