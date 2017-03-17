package sk.tuke.game.pong.arena.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import sk.tuke.game.pong.arena.GameInfo;
import sk.tuke.game.pong.arena.JsonBodyTemplate;
import sk.tuke.game.pong.interfaces.Enemy;

import java.util.Random;

/**
 * Created by otara on 22.1.2017.
 */
public class EnemyActor extends JsonBodyTemplate implements Enemy {

	private final int ENEMY_WIDTH = 50;
	private final int ENEMY_HEIGHT = 30;
	StartSide position;

	public EnemyActor() {
		image = new Texture(Gdx.files.internal("enemy.jpg"));
		sprite = new Sprite(image);
		jsonFile = "enemyBody.json";
		name = "enemyBodyJson";
		width = ENEMY_WIDTH;
		size = generateSize();
		if (generateSize() == 1) {
			position = StartSide.LEFT;
			setPosition(generateX(1), generateY());
		} else {
			position = StartSide.RIGHT;
			setPosition(generateX(2), generateY());
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		Vector2 vector = physicsBody.getPosition().sub(bodyVector);
		sprite.setPosition(vector.x * GameInfo.PPM, vector.y * GameInfo.PPM);
		sprite.setOrigin(bodyVector.x, bodyVector.y);
		sprite.setRotation(physicsBody.getAngle() * MathUtils.radiansToDegrees);
		batch.draw(sprite, sprite.getX(), sprite.getY(), ENEMY_WIDTH * size, ENEMY_HEIGHT * size);
	}

	@Override
	protected BodyDef getBodyDef() {
		BodyDef bd = new BodyDef();
		bd.position.set(getX() / GameInfo.PPM, getY() / GameInfo.PPM);
		bd.type = BodyDef.BodyType.DynamicBody;
		return bd;
	}

	@Override
	protected FixtureDef getFixtureDef() {
		FixtureDef fd = new FixtureDef();
		fd.filter.categoryBits = GameInfo.FILTER_ENEMY_BIT;
		fd.filter.maskBits = GameInfo.FILTER_PLAYER_BIT;
		fd.filter.groupIndex = 0;
		return fd;
	}

	private int generateSize() {
		int big = 2;
		int small = 1;
		Random rand = new Random();

		int n = rand.nextInt(21);
		if (n > 10) {
			return big;
		} else {
			return small;
		}
	}

	private float generateY() {
		float minY = PointActor.pointsPositions[3][1];
		float maxY = PointActor.pointsPositions[0][1] - minY;
		Random rand = new Random();
		return rand.nextFloat() * maxY + minY;
	}

	private float generateX(int side) {
		if (side == 1) {
			return 0;
		}
		return GameInfo.GAME_WIDTH;
	}

	public void updateVector(float x) {
		float mass = physicsBody.getMass();
		float targetVelocity = 20;
		Vector2 targetPosition = new Vector2(x / GameInfo.PPM, getY() / GameInfo.PPM);
		float impulseMag = mass * targetVelocity;
		Vector2 impulse = new Vector2();
		impulse.set(targetPosition).sub(physicsBody.getPosition());
		impulse.scl(impulseMag);
		physicsBody.applyLinearImpulse(impulse, physicsBody.getWorldCenter(), true);
	}

	public void setImpulse(float enemyX) {
		if (enemyX == 0) {
			updateVector(GameInfo.GAME_WIDTH);
		} else {
			updateVector(0);
		}
	}

	public enum StartSide {
		LEFT, RIGHT
	}


	@Override
	public float getEnemyX() {
		return physicsBody.getPosition().x * GameInfo.PPM;
	}

	@Override
	public float getEnemyY() {
		return physicsBody.getPosition().y * GameInfo.PPM;
	}
}
