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
	private Texture pigTexture, coinTexture, atomTexture, rainTexture;
	private OrthographicCamera objOrthographicCamera;
	private BitmapFont nameBitmapFont, scoreBitmapFont, showScoreBitmapFont;
	private int xCloudAnInt,yCloudAnInt=580;
	private int xAtomRAnInt,yAtomRAnInt = 580;
	private int xAtomLAnInt, yAtomLAnInt = 1204;
	private boolean atomRABoolean, atomLABoolean = true;
	private boolean cloudABoolean = true, finishABoolean = false;
	private Rectangle pigRectangle, coinRectangle, rainRectangle;
	private Vector3 objVector3;
	private Sound pigSound, waterDropSound, coinDropSound;
	private Array<Rectangle> coinsArray, rainArray;
	private long lastDropCoins, lastDropRain;
	//วนหลายรูป
	private Iterator<Rectangle> coinsIterator, rainIterator; // Java util
	private int scoreAnInt = 0, falseAnInt = 0, finalScoreAnInt;
	private Music rainMusic, bgMusic; // เสียงเป็น background loop


	@Override
	public void create () {
		batch = new SpriteBatch();


		objOrthographicCamera = new OrthographicCamera();
		objOrthographicCamera.setToOrtho(false, 1280, 768);

		wallpaperTexture = new Texture("ww3.png");

		// ตัวอักษร
		nameBitmapFont = new BitmapFont();
		nameBitmapFont.setColor(Color.BLUE);
		nameBitmapFont.scale(3);
		scoreBitmapFont = new BitmapFont();
		scoreBitmapFont.setColor(Color.OLIVE);
		scoreBitmapFont.scale(3);
		showScoreBitmapFont = new BitmapFont();
		showScoreBitmapFont.setColor(230, 28, 223, 255);
		showScoreBitmapFont.scale(6);



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

		// set up background sound
		bgMusic = Gdx.audio.newMusic(Gdx.files.internal("bggame.mp3"));
		// set up rain music
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
		//set up pig sound
		pigSound = Gdx.audio.newSound(Gdx.files.internal("pig.wav"));
		// set up water drop
		waterDropSound = Gdx.audio.newSound(Gdx.files.internal("water_drop.wav"));
		// set up coin drop sound
		coinDropSound = Gdx.audio.newSound(Gdx.files.internal("coins_drop.wav"));

		// set up Coins
		coinTexture = new Texture("coins.png");

		// set up rain
		rainTexture = new Texture("droplet.png");

		// Create coinsArray
		coinsArray = new Array<Rectangle>();
		coinsRandomDrop();

		// Create rainsArray
		rainArray = new Array<Rectangle>();
		rainRandomDrop();

	} // create เอาไว้กำหนดค่า

	private void rainRandomDrop() {
		rainRectangle = new Rectangle();
		rainRectangle.x = MathUtils.random(0, 1204);
		rainRectangle.y = 800;
		rainRectangle.width = 64;
		rainRectangle.height = 64;

		rainArray.add(rainRectangle);
		lastDropRain = TimeUtils.nanoTime();
	}

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
		} // coin

		// drawable rain
		for (Rectangle forRain : rainArray) {
			batch.draw(rainTexture,forRain.x,forRain.y);
		} // rain

		// drawable score
		scoreBitmapFont.draw(batch, "Score = " + Integer.toString(scoreAnInt), 900, 750);
		if (finishABoolean) {
			batch.draw(wallpaperTexture,0,0);
			showScoreBitmapFont.draw(batch, "Your Score = " + Integer.toString(finalScoreAnInt),300,400);
		}

		batch.end();
		//move cloud
		//moveCloud();
		moveAtom();
		//Active When Touch Screen
		activeTouchScreen();

		// Random Drop Coins
		randomDropCoins();

		// Random drop Rain
		randomDropRain();

		// play music
		rainMusic.play();

		//backgound music
		bgMusic.play();

	} // render

	private void randomDropRain() {
		if (TimeUtils.nanoTime()-lastDropRain> 1E9) {
			rainRandomDrop();
		}

		rainIterator = rainArray.iterator();
		while (rainIterator.hasNext()) {
			Rectangle myRainRectangle = rainIterator.next();
			myRainRectangle.y -= 50 * Gdx.graphics.getDeltaTime();

			// when rain drop into floor
			if (myRainRectangle.y + 64 < 0) {
				waterDropSound.play();
				rainIterator.remove();
			} // if
			if (myRainRectangle.overlaps(pigRectangle)) {
				waterDropSound.play();
				scoreAnInt -= 1;
				rainIterator.remove();
			}
		}// while

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
				falseAnInt += 1;
				waterDropSound.play();
				coinsIterator.remove(); // เหรียญหายไปคืนหน่วยความจำให้ระบบ
				checkFalse();

			} // if
			// When Coins Overlap Pig
			// การ overlaps บางส่วนของภาพไปโดนบางส่วนของอีกภาพ
			if (myCoinsRectangle.overlaps(pigRectangle)) {
				coinDropSound.play();
				scoreAnInt += 1;
				coinsIterator.remove();
			} // if

		} // while loop

	} //randomDropCoins

	private void checkFalse() {
		if (falseAnInt > 20) {

			dispose();
			if (!finishABoolean) {
				finalScoreAnInt = scoreAnInt;
			}
			finishABoolean = true;

		}
	} // check False

	@Override
	public void dispose() {
		super.dispose();
		bgMusic.dispose();
		rainMusic.dispose();
		coinDropSound.dispose();
		pigSound.dispose();
		waterDropSound.dispose();

	} // dispose

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
