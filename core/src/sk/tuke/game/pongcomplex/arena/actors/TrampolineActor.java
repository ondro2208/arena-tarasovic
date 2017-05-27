package sk.tuke.game.pongcomplex.arena.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import sk.tuke.game.pongcomplex.arena.GameInfo;

/**
 * Represent trampoline in game.
 */
public class TrampolineActor extends BodyTemplate {

	/**
	 * Initialize object. Set image and starting position.
	 *
	 * @param x initialize coordinate of x position
	 * @param y initialize coordinate of y position
	 */
	public TrampolineActor(float x, float y) {
		image = new Texture(Gdx.files.internal("trampoline.png"));
		sprite = new Sprite(image);
		setPosition(x, y);
		sprite.setSize(90, 20);
	}

	/**
	 * Draw trampoline sprite, center and fit it to actual position.
	 */
	@Override
	public void draw(Batch batch, float parentAlpha) {
		sprite.setPosition((physicsBody.getPosition().x - sprite.getWidth() / 2), (physicsBody.getPosition().y - sprite.getHeight() / 2));
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		sprite.draw(batch);
	}

	/**
	 * Create body definition with position and type. Used during creation of body.
	 */
	@Override
	protected BodyDef getBodyDef() {
		BodyDef bd = new BodyDef();
		bd.position.set(getX() / GameInfo.PPM, getY() / GameInfo.PPM);
		bd.type = BodyDef.BodyType.StaticBody;
		return bd;
	}

	/**
	 * Create fixture definition with shape and set collision filter. Used during creation of body.
	 */
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
