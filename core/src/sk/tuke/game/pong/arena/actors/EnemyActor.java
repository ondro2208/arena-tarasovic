package sk.tuke.game.pong.arena.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import sk.tuke.game.pong.arena.BodyTemplate;
import sk.tuke.game.pong.arena.GameInfo;

import java.util.Random;

/**
 * Created by otara on 22.1.2017.
 */
public class EnemyActor extends BodyTemplate {

	private final int ENEMY_WIDTH = 50;
	private final int ENEMY_HEIGHT = 30;
	StartPosition position;

	public EnemyActor() {
		jsonFile = "enemyBody.json";
		name = "enemyBodyJson";
		width = ENEMY_WIDTH;
		size = generateSize();
		if (generateSize() == 1) {
			position = StartPosition.LEFT;
		} else {
			position = StartPosition.RIGHT;
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		Vector2 vector = physicsBody.getPosition().sub(bodyVector);
		sprite.setPosition(vector.x * GameInfo.PPM, vector.y * GameInfo.PPM);
		sprite.setOrigin(bodyVector.x, bodyVector.y);
		sprite.setRotation(physicsBody.getAngle() * MathUtils.radiansToDegrees);
		batch.draw(sprite, sprite.getX(), sprite.getY(), ENEMY_WIDTH, ENEMY_HEIGHT);
	}

	@Override
	protected BodyDef getBodyDef() {
		BodyDef bd = new BodyDef();
		bd.position.set(getX() / GameInfo.PPM, getY() / GameInfo.PPM);
		bd.type = BodyDef.BodyType.DynamicBody;
		return bd;
	}

	private int generateSize() {
		int big = 2;
		int small = 1;
		Random rand = new Random();

		int n = rand.nextInt(21);
		System.out.println("Generated: " + n);
		if (n > 10) {
			return big;
		} else {
			return small;
		}
	}

	@Override
	protected FixtureDef getFixtureDef() {
		FixtureDef fd = new FixtureDef();
		fd.density = 100f;
		fd.friction = 0.000f;
		fd.restitution = 1.0f;
		fd.filter.categoryBits = GameInfo.FILTER_ENEMY_BIT;
		fd.filter.maskBits = GameInfo.FILTER_PLAYER_BIT;
		fd.filter.groupIndex = 0;
		return fd;
	}

	public enum StartPosition {
		LEFT, RIGHT
	}

}
