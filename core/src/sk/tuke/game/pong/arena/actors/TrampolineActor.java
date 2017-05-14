package sk.tuke.game.pong.arena.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import sk.tuke.game.pong.arena.BodyTemplate;
import sk.tuke.game.pong.arena.GameInfo;

/**
 * Created by otara on 22.1.2017.
 */
public class TrampolineActor extends BodyTemplate {

	public TrampolineActor(float x, float y) {
		image = new Texture(Gdx.files.internal("trampoline1.png"));
		sprite = new Sprite(image);
		setPosition(x, y);
		sprite.setSize(90, 20);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		sprite.setPosition((physicsBody.getPosition().x /** GameInfo.PPM */ - sprite.getWidth() / 2), (physicsBody.getPosition().y /** GameInfo.PPM*/ - sprite.getHeight() / 2));
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		sprite.draw(batch);
	}

	@Override
	protected BodyDef getBodyDef() {
		BodyDef bd = new BodyDef();
		bd.position.set(getX() / GameInfo.PPM, getY() / GameInfo.PPM);
		bd.type = BodyDef.BodyType.StaticBody;
		return bd;
	}

	@Override
	protected FixtureDef getFixtureDef() {
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(25, 2.5f);
		FixtureDef fd = new FixtureDef();
		fd.shape = shape;
		fd.filter.categoryBits = GameInfo.FILTER_TRAMPOLINE_BIT;
		fd.filter.maskBits = GameInfo.FILTER_PLAYER_BIT;
		fd.filter.groupIndex = 0;
		return fd;
	}
}
