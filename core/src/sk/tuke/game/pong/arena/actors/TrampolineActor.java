package sk.tuke.game.pong.arena.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import sk.tuke.game.pong.arena.BodyTemplate;
import sk.tuke.game.pong.arena.DirAndPos;
import sk.tuke.game.pong.arena.GameInfo;

/**
 * Created by otara on 22.1.2017.
 */
public class TrampolineActor extends BodyTemplate {

	private DirAndPos location;
	private static final int TRAMPOLINE_WIDTH = 200;
	private static final int TRAMPOLINE_HEIGHT = 38;

	public TrampolineActor(DirAndPos location, int x, int y) {
		image = new Texture(Gdx.files.internal("trampoline.png"));
		sprite = new Sprite(image);
		this.location = location;
		setPosition(x, y);
		jsonFile = "trampoline.json";
		name = "trampolineBodyJson";
		width = TRAMPOLINE_WIDTH;

	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		Vector2 vector = physicsBody.getPosition().sub(bodyVector);
		sprite.setPosition(vector.x * GameInfo.PPM, vector.y * GameInfo.PPM);
		sprite.setOrigin(bodyVector.x, bodyVector.y);
		sprite.setRotation(physicsBody.getAngle() * MathUtils.radiansToDegrees);
		batch.draw(sprite, sprite.getX(),sprite.getY(),TRAMPOLINE_WIDTH,TRAMPOLINE_HEIGHT);
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
		FixtureDef fd = new FixtureDef();
		fd.density = 100f;
		fd.friction = 0.000f;
		fd.restitution = 1.0f;
		fd.filter.categoryBits = GameInfo.FILTER_TRAMPOLINE_BIT;
		fd.filter.maskBits = GameInfo.FILTER_PLAYER_BIT;
		fd.filter.groupIndex = 0;
		return fd;
	}
}
