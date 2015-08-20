package paew.cs.scaat;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	private Texture wallpaperTexture, cloudTexture, atomRTexture, atomLTexture;
	private Texture pigTexture;
	private OrthographicCamera objOrthographicCamera;
	private BitmapFont nameBitmapFont;
	private int xCloudAnInt,yCloudAnInt=580;
	private int xAtomRAnInt,yAtomRAnInt = 580;
	private int xAtomLAnInt, yAtomLAnInt = 1204;
	private boolean atomRABoolean, atomLABoolean = true;
	private boolean cloudABoolean = true;
	private Rectangle pigRectangle;
	private Vector3 objVector3;
	private Sound pigSound;

	@Override
	public void create () {
		batch = new SpriteBatch();


		objOrthographicCamera = new OrthographicCamera();
		objOrthographicCamera.setToOrtho(false, 1280, 768);

		wallpaperTexture = new Texture("w3.png");

		nameBitmapFont = new BitmapFont();
		nameBitmapFont.setColor(Color.BLUE);
		nameBitmapFont.scale(3);

		//cloudTexture = new Texture("p3_right.png");
		atomRTexture = new Texture("p3_right.png");
		atomLTexture = new Texture("p3_left.png");
		pigTexture = new Texture("pig.png");

		//set up Rectangle Pig
		pigRectangle = new Rectangle();
		pigRectangle.x = 602;
		pigRectangle.y = 100;
		pigRectangle.width = 64;
		pigRectangle.height = 64;

		//set up pig sound
		pigSound = Gdx.audio.newSound(Gdx.files.internal("pig.wav"));



	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//setup screen
		objOrthographicCamera.update();
		batch.setProjectionMatrix(objOrthographicCamera.combined);

		batch.begin();
		//batch.draw(img, 0, 0);
		batch.draw(wallpaperTexture, 0, 0);
		// drawable cloudTexture
		//batch.draw(cloudTexture,xCloudAnInt,yCloudAnInt);

		//drawable pig
		batch.draw(pigTexture,pigRectangle.x,pigRectangle.y);

		//drawable atom
		batch.draw(atomRTexture,xAtomRAnInt,yAtomRAnInt);
		nameBitmapFont.draw(batch, "Coin PBRU", 50, 750);

		batch.end();

		//move cloud
		//moveCloud();
		moveAtom();
		//Active When Touch Screen
		activeTouchScreen();
	}

	private void activeTouchScreen() {
		// นิ้วสัมผัสจอ
		if (Gdx.input.isTouched()) {
			// Sound effect pig
			pigSound.play();
			// ตำแหน่งของจอ
			objVector3 = new Vector3();
			objVector3.set(Gdx.input.getX(), Gdx.input.getY(), 0);

			if (objVector3.x < 602) {
				if (pigRectangle.x < 0) {
					pigRectangle.x = 0;
				} else {
					pigRectangle.x -= 10;
				}

			} else {
				if (pigRectangle.x > 1202) {
					pigRectangle.x = 1202;
				} else {
					pigRectangle.x += 10;
				}

			}
		}
	} // active Touch Screen

	private void moveAtom() {
		if (atomRABoolean) {
			if (xAtomRAnInt<994) {
				xAtomRAnInt += 100 * Gdx.graphics.getDeltaTime();
			} else {
				atomRABoolean = !atomRABoolean;
				//batch.draw(atomLTexture,xAtomLAnInt,yAtomLAnInt);

			}
		} else {
			if (xAtomRAnInt>0) {
				xAtomRAnInt -= 100 * Gdx.graphics.getDeltaTime();
			} else {
				atomRABoolean = !atomRABoolean;
			}
		}
	}

	private void moveCloud() {
		if (cloudABoolean) {
			if (xCloudAnInt<960) {
				xCloudAnInt += 100 * Gdx.graphics.getDeltaTime();
			} else {
				cloudABoolean = !cloudABoolean;
			}
		} else {
			if (xCloudAnInt>0) {
				xCloudAnInt -= 100 * Gdx.graphics.getDeltaTime();
			} else {
				cloudABoolean = !cloudABoolean;
			}
		}

	} // move cloud
}
