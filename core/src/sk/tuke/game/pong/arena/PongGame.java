package sk.tuke.game.pong.arena;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;

public class PongGame extends ApplicationAdapter {

	public static final int GAME_WIDTH = 1920;
	public static final int GAME_HEIGHT = 1080;
	public static final String GAME_NAME = "Pong";
	public static final int POINT_OFFSET = 35;
	private static final String SCORE_TEXT = "SCORE: ";

	private Box2DDebugRenderer debugRenderer;
	private OrthographicCamera camera;
	private Stage gameStage;
	private PlayerActor player;
	private TrampolineActor trampolineActor;
	private TrampolineActor[] trampolines;
	private ArrayList<PointActor> points;
	private World world;

	private Texture backgroundImage;
	private Label scoreText;
	private int score;

	@Override
	public void create () {
		camera = new OrthographicCamera();
		debugRenderer = new Box2DDebugRenderer();
		camera.setToOrtho(false,GAME_WIDTH,GAME_HEIGHT);
		camera.position.set(GAME_WIDTH/2f,GAME_HEIGHT/2f,0);

		world = new World(new Vector2(0, 0), true);
		backgroundImage = new Texture(Gdx.files.internal("bg.png"));

		gameStage = new Stage(new ScreenViewport());
		player = new PlayerActor();

		trampolineInitialize();
		pointInitialize();
		gameStage.addActor(player);
		player.createBody(world);

		createScore();
		gameStage.addActor(scoreText);

		world.setContactListener(new PongContactListener(player));
	}



	@Override
	public void render () {

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
			Gdx.app.exit();
		}
		gameStage.act();
		if(points.size()>0) {
			for (PointActor point : points) {
				if (point.getPointBody().getPosition().dst(player.getPlayerBody().getPosition()) < 20) {
					player.grabPoint(point, gameStage);
					points.remove(point);
					updateScore();
					break;
				}
			}
		}

		gameStage.getBatch().begin();
		gameStage.getBatch().draw(backgroundImage,0,0,GAME_WIDTH,GAME_HEIGHT);
		gameStage.getBatch().end();
		if(player.getPlayerBody() != null)
			gameStage.draw();

		debugRenderer.render(world,camera.combined);
		world.step(/*Gdx.graphics.getDeltaTime()*/1/60f,6,2);
	}
	
	@Override
	public void dispose () {
		backgroundImage.dispose();
		gameStage.dispose();
	}

	private void pointInitialize(){
		points = new ArrayList<PointActor>();
		PointActor point = new PointActor(PointActor.pointsPositions[1][0] , PointActor.pointsPositions[1][1]);
		point.createBody(world);
		gameStage.addActor(point);
		points.add(0,point);
	}

	private void trampolineInitialize() {
		trampolines = new TrampolineActor[]{
				new TrampolineActor(Gdx.graphics.getWidth() / 4 , Gdx.graphics.getHeight()-(Gdx.graphics.getHeight() / 4)),
				new TrampolineActor(Gdx.graphics.getWidth() / 2 , Gdx.graphics.getHeight()-(Gdx.graphics.getHeight() / 4)),
				new TrampolineActor(Gdx.graphics.getWidth() - (Gdx.graphics.getWidth()/4) , Gdx.graphics.getHeight()-(Gdx.graphics.getHeight() / 4)),
				new TrampolineActor(Gdx.graphics.getWidth() / 4 ,Gdx.graphics.getHeight() / 4),
				new TrampolineActor(Gdx.graphics.getWidth() / 2 ,Gdx.graphics.getHeight() / 4),
				new TrampolineActor(Gdx.graphics.getWidth() - (Gdx.graphics.getWidth()/4) ,Gdx.graphics.getHeight() / 4)
		};
		for(int i=0;i<6;i++){
			gameStage.addActor(trampolines[i]);
			trampolines[i].createBody(world);
		}
	}

	private Label createScore(){
		Label.LabelStyle textStyle;
		BitmapFont font = new BitmapFont();

		textStyle = new Label.LabelStyle();
		textStyle.font = font;

		scoreText = new Label(SCORE_TEXT + score,textStyle);
		scoreText.setBounds(Gdx.graphics.getWidth()-100,Gdx.graphics.getHeight()-80,90,70);
		scoreText.setFontScale(1f,1f);
		return scoreText;
	}

	private void updateScore(){
		this.score++;
		scoreText.setText(SCORE_TEXT+score);
	}

}
