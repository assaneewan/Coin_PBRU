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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	private Texture wallpaperTexture, cloudTexture, atomRTexture, atomLTexture;
	private Texture pigTexture, coinTexture, atomTexture;
	private OrthographicCamera objOrthographicCamera;
	private BitmapFont nameBitmapFont;
	private int xCloudAnInt,yCloudAnInt=580;
	private int xAtomRAnInt,yAtomRAnInt = 580;
	private int xAtomLAnInt, yAtomLAnInt = 1204;
	private boolean atomRABoolean, atomLABoolean = true;
	private boolean cloudABoolean = true;
	private Rectangle pigRectangle, coinRectangle;
	private Vector3 objVector3;
	private Sound pigSound, waterDropSound, coinDropSound;
	private Array<Rectangle> coinsArray;
	private long lastDropCoins;
	private Iterator<Rectangle> coinsIterator; // Java util


	@Override
	public void create () {
		batch = new SpriteBatch();


		objOrthographicCamera = new OrthographicCamera();
		objOrthographicCamera.setToOrtho(false, 1280, 768);

		wallpaperTexture = new Texture("ww3.png");

		nameBitmapFont = new BitmapFont();
		nameBitmapFont.setColor(Color.BLUE);
		nameBitmapFont.scale(3);

		//cloudTexture = new Texture("p3_right.png");
		atomTexture = new Texture("p3_right.png");
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
		// set up water drop
		waterDropSound = Gdx.audio.newSound(Gdx.files.internal("water_drop.wav"));
		// set up coin drop sound
		coinDropSound = Gdx.audio.newSound(Gdx.files.internal("coins_drop.wav"));

		// set up Coins
		coinTexture = new Texture("coins.png");

		// Create coinsArray
		coinsArray = new Array<Rectangle>();
		coinsRandomDrop();

	} // create เอาไว้กำหนดค่า

	private void coinsRandomDrop() {

		coinRectangle = new Rectangle();
		coinRectangle.x = MathUtils.random(0, 1204);
		coinRectangle.y = 800;
		coinRectangle.width = 64;
		coinRectangle.height = 64;
		coinsArray.add(coinRectangle); //
		lastDropCoins = TimeUtils.nanoTime(); //ไม่ให้ 2 เหรียญหล่นชนกัน


	} // นี่คือ coins random

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
		batch.draw(atomTexture,xAtomRAnInt,yAtomRAnInt);
		nameBitmapFont.draw(batch, "Coin PBRU", 50, 750);

		//drawable coins
		for (Rectangle forCoins : coinsArray) {
			batch.draw(coinTexture,forCoins.x,forCoins.y);
		}
		batch.end();
		//move cloud
		//moveCloud();
		moveAtom();
		//Active When Touch Screen
		activeTouchScreen();

		// Random Drop Coins
		randomDropCoins();
	}

	private void randomDropCoins() {

		// 1E9 คือ 1 วินาที กำหนดเป็นเลขยกำลัง
		if (TimeUtils.nanoTime()-lastDropCoins > 1E9) {
			coinsRandomDrop();
		}
		coinsIterator = coinsArray.iterator();
		while (coinsIterator.hasNext()) {
			Rectangle myCoinsRectangle = coinsIterator.next();
			myCoinsRectangle.y -= 50 * Gdx.graphics.getDeltaTime();

			//When Coins into Floor
			if (myCoinsRectangle.y + 64 < 0) {
				waterDropSound.play();
				coinsIterator.remove(); // เหรียญหายไปคืนหน่วยความจำให้ระบบ

			} // if
			// When Coins Overlap Pig
			// การ overlaps บางส่วนของภาพไปโดนบางส่วนของอีกภาพ
			if (myCoinsRectangle.overlaps(pigRectangle)) {
				coinDropSound.play();
				coinsIterator.remove();
			} // if

		} // while loop

	} //randomDropCoins

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
				atomTexture = atomLTexture;

			}
		} else {
			if (xAtomRAnInt>0) {
				xAtomRAnInt -= 100 * Gdx.graphics.getDeltaTime();
			} else {
				atomRABoolean = !atomRABoolean;
				atomTexture = atomRTexture;

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
