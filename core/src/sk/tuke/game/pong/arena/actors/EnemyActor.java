package sk.tuke.game.pong.arena.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import sk.tuke.game.pong.arena.GameInfo;
import sk.tuke.game.pong.interfaces.Enemy;

import java.util.Random;

/**
 * Created by otara on 22.1.2017.
 */
public class EnemyActor extends Actor implements Enemy {

	private final int unit = GameInfo.GAME_WIDTH / 100;
	private final int smallUnit = unit / 2;
	private StartSide startPosition;
	private int size;
	private Body physicsBody;
	private Texture image;
	private Sprite sprite;

	public EnemyActor(World world) {
		//image = new Texture(Gdx.files.internal("enemy.jpg"));
		image = new Texture(Gdx.files.internal("enemy1.png"));
		sprite = new Sprite(image);
		size = generateRandFromTwoValues();
		if (generateRandFromTwoValues() == 1) {
			startPosition = StartSide.LEFT;
			setPosition(generateX(1) / GameInfo.PPM, generateY() / GameInfo.PPM);
		} else {
			startPosition = StartSide.RIGHT;
			setPosition(generateX(2) / GameInfo.PPM, generateY() / GameInfo.PPM);
		}

		BodyDef bd = new BodyDef();
		FixtureDef fd = new FixtureDef();
		bd.type = BodyDef.BodyType.DynamicBody;
		bd.position.set(getX(), getY());

		if (size == 1) {
			createEnemyBody(world, bd, fd, smallUnit);
			sprite.setSize(smallUnit * 3, smallUnit * 2);
		} else {
			createEnemyBody(world, bd, fd, unit);
			sprite.setSize(unit * 3, unit * 2);
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		sprite.setPosition((physicsBody.getPosition().x /** GameInfo.PPM */ - sprite.getWidth() / 2), (physicsBody.getPosition().y /** GameInfo.PPM*/ - sprite.getHeight() / 2));
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		sprite.draw(batch);
	}

	private void createEnemyBody(World world, BodyDef bd, FixtureDef fd, int unit) {
		PolygonShape boxShape1 = new PolygonShape();
		boxShape1.setAsBox(unit, unit);
		fd.shape = boxShape1;
		fd.filter.categoryBits = GameInfo.FILTER_ENEMY_BIT;
		fd.filter.maskBits = GameInfo.FILTER_PLAYER_BIT;
		fd.filter.groupIndex = 0;

		physicsBody = world.createBody(bd);
		Fixture fixture = physicsBody.createFixture(fd);
		fixture.setUserData(this);
		boxShape1.dispose();

		PolygonShape boxShape2 = new PolygonShape();
		boxShape2.setAsBox(unit / 2 + unit, unit / 2);
		fd.shape = boxShape2;
		fd.filter.categoryBits = GameInfo.FILTER_ENEMY_BIT;
		fd.filter.maskBits = GameInfo.FILTER_PLAYER_BIT;
		fd.filter.groupIndex = 0;
		fixture = physicsBody.createFixture(fd);
		fixture.setUserData(this);
		boxShape2.dispose();

		CircleShape circle1 = new CircleShape();
		circle1.setPosition(new Vector2(-unit, unit / 2));
		circle1.setRadius(unit / 2);
		fd.shape = circle1;
		fd.filter.categoryBits = GameInfo.FILTER_ENEMY_BIT;
		fd.filter.maskBits = GameInfo.FILTER_PLAYER_BIT;
		fd.filter.groupIndex = 0;
		fixture = physicsBody.createFixture(fd);
		fixture.setUserData(this);
		circle1.dispose();

		CircleShape circle2 = new CircleShape();
		circle2.setPosition(new Vector2(unit, unit / 2));
		circle2.setRadius(unit / 2);
		fd.shape = circle2;
		fd.filter.categoryBits = GameInfo.FILTER_ENEMY_BIT;
		fd.filter.maskBits = GameInfo.FILTER_PLAYER_BIT;
		fd.filter.groupIndex = 0;
		fixture = physicsBody.createFixture(fd);
		fixture.setUserData(this);
		circle2.dispose();

		CircleShape circle3 = new CircleShape();
		circle3.setPosition(new Vector2(-unit, -unit / 2));
		circle3.setRadius(unit / 2);
		fd.shape = circle3;
		fd.filter.categoryBits = GameInfo.FILTER_ENEMY_BIT;
		fd.filter.maskBits = GameInfo.FILTER_PLAYER_BIT;
		fd.filter.groupIndex = 0;
		fixture = physicsBody.createFixture(fd);
		fixture.setUserData(this);
		circle3.dispose();

		CircleShape circle4 = new CircleShape();
		circle4.setPosition(new Vector2(unit, -unit / 2));
		circle4.setRadius(unit / 2);
		fd.shape = circle4;
		fd.filter.categoryBits = GameInfo.FILTER_ENEMY_BIT;
		fd.filter.maskBits = GameInfo.FILTER_PLAYER_BIT;
		fd.filter.groupIndex = 0;
		fixture = physicsBody.createFixture(fd);
		fixture.setUserData(this);
		circle4.dispose();
	}

	private int generateRandFromTwoValues() {
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
		float minY = GameInfo.positions[3][1] + 50;
		float maxY = GameInfo.positions[0][1] - 50 - minY;
		Random rand = new Random();
		return rand.nextFloat() * maxY + minY;
	}

	private float generateX(int side) {
		if (side == 1) {
			return 0;
		}
		return GameInfo.GAME_WIDTH - 100;
	}

	private void updateVector(float x) {
		float mass = physicsBody.getMass();
		float targetVelocity = 20;
		Vector2 targetPosition = new Vector2(x / GameInfo.PPM, getEnemyY() / GameInfo.PPM);
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

	@Override
	public StartSide getEnemyDirection() {
		if (StartSide.LEFT == startPosition) {
			return StartSide.RIGHT;
		} else return StartSide.LEFT;
	}

	public Body getPhysicsBody() {
		return physicsBody;
	}
}
