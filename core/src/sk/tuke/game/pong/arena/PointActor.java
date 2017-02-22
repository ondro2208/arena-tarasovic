package sk.tuke.game.pong.arena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Random;

/**
 * Created by otara on 22.1.2017.
 */
public class PointActor extends BodyTemplate {

	public static final int POINT_SIZE = 20;
	public static final short BIT_POINT = 4;
	public static final int POINT_OFFSET = 35;
	private int x;
	private int y;
	private float radius;
	private Body pointBody;
	private Sprite sprite;
	private Texture image;

	public static int[][] pointsPositions = new int[][]{
			{Gdx.graphics.getWidth() / 4 , Gdx.graphics.getHeight()-(Gdx.graphics.getHeight() / 4)-POINT_OFFSET},
			{Gdx.graphics.getWidth() / 2,Gdx.graphics.getHeight()-(Gdx.graphics.getHeight() / 4)-POINT_OFFSET},
			{Gdx.graphics.getWidth() - (Gdx.graphics.getWidth()/4),Gdx.graphics.getHeight()-(Gdx.graphics.getHeight() / 4)-POINT_OFFSET},
			{Gdx.graphics.getWidth() / 4 , Gdx.graphics.getHeight() / 4 + POINT_OFFSET},
			{Gdx.graphics.getWidth() / 2,Gdx.graphics.getHeight() / 4 + POINT_OFFSET},
			{Gdx.graphics.getWidth() - (Gdx.graphics.getWidth()/4),Gdx.graphics.getHeight() / 4 + POINT_OFFSET}
	};

	public PointActor(int x, int y) {
		this.x = x;
		this.y = y;
		image = new Texture(Gdx.files.internal("point.jpg"));
		sprite = new Sprite(image);
		sprite.setPosition(x,y);
		setPosition(x,y);
		radius = 10;
		sprite.setSize(POINT_SIZE,POINT_SIZE);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		//sprite.setSize(POINT_SIZE,POINT_SIZE);
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2); //nastav bod otacania na stred textury
		sprite.setCenter(getX(),getY()); //nastav poziciu podla stredu, aby som nemusel riesit offset posunutia textury
		sprite.draw(batch);
	}

	@Override
	public Body createBody(World world)
	{
		BodyDef bd = getBodyDef();
		FixtureDef fd = getFixtureDef();
		pointBody = world.createBody(bd);
		if (fd != null) {
			if (fd.shape == null) {
				throw new RuntimeException("Shape is not defined in FixtureDef");
			}
			physicsFixture = pointBody.createFixture(fd);
			fd.shape.dispose(); //needs to be called when shape is no longer needed

			physicsFixture.setUserData(this);
		}

		pointBody.setActive(false);
		return pointBody;
	}

	@Override
	protected BodyDef getBodyDef(){
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.KinematicBody;
		bodyDef.position.set(x,y);
		return bodyDef;
	}

	@Override
	protected FixtureDef getFixtureDef() {
		CircleShape shape = new CircleShape();
		shape.setRadius(radius);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		//Ball nepotrebuje restitution lebo to je nastavene na Paddle
		//fixtureDef.restitution = 1.03f; //zvysenie rychlosti po kazdom odraze
		//fixtureDef.friction = 0.001f; //trenie aby sa lopticka otacala a menil sa aj uhol odrazu
		//fixtureDef.density = 1f;
		fixtureDef.filter.categoryBits = BIT_POINT;
		fixtureDef.filter.maskBits = BIT_POINT;
		fixtureDef.filter.groupIndex = 0;
		return fixtureDef;
	}

	public Body getPointBody() {
		return pointBody;
	}

	public void setPointBody(Body pointBody) {
		this.pointBody = pointBody;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	public Texture getImage() {
		return image;
	}

	public void setImage(Texture image) {
		this.image = image;
	}

	public PointActor generateNewPoint(Vector2 positionVector) {
		int n, x, y;
		n = generateRandomPosition();
		x = pointsPositions[n][0];
		y = pointsPositions[n][1];

		while (positionVector.x == x && positionVector.y == y) {
			n = generateRandomPosition();
			x = pointsPositions[n][0];
			y = pointsPositions[n][1];
		}

		PointActor point = new PointActor(x,y);
		return point;
	}

	private int generateRandomPosition() {
		Random rand = new Random();
		int n = rand.nextInt(56) + 6;
		System.out.println("before round: " + n);
		n = Math.round(n / (float)10);
		System.out.println("after round: " + n);
		return n-1;
		/*int n = rand.nextInt(2);
		if(n==0){
			return 2-1;
		}
		else
			return 5-1;*/
	}
}
