package com.example.thetibetanalphabetapp;

import com.cycrix.androidannotation.AndroidAnnotationParser;
import com.cycrix.androidannotation.Click;
import com.cycrix.androidannotation.ViewById;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class CharacterActivity extends Activity {
	
	public static SoundPool mSoundPool;
	public static int[] soundId = new int[30];
	
	static int[] soundRid = new int[] {
			R.raw.ka, R.raw.kha, R.raw.ga, R.raw.nga,
			R.raw.cha, R.raw.chha, R.raw.ja, R.raw.nya,
			R.raw.ta, R.raw.tha, R.raw.da, R.raw.na,
			R.raw.pa, R.raw.pha, R.raw.ba, R.raw.ma,
			R.raw.tsa, R.raw.tsha, R.raw.dza, R.raw.wa,
			R.raw.zha, R.raw.za, R.raw.ah, R.raw.ya,
			R.raw.ra, R.raw.la, R.raw.sha, R.raw.sa,
			R.raw.ha, R.raw.a};
	
	static int mCharIndex;
	static int[] mWriteRid = new int[] {
		R.drawable.ka_write, R.drawable.kha_write, R.drawable.ga_write, R.drawable.nga_write,
		R.drawable.cha_write, R.drawable.chha_write, R.drawable.ja_write, R.drawable.nya_write,
		R.drawable.ta_write, R.drawable.tha_write, R.drawable.da_write, R.drawable.na_write,
		R.drawable.pa_write, R.drawable.pha_write, R.drawable.ba_write, R.drawable.ma_write,
		R.drawable.tsa_write, R.drawable.tsha_write, R.drawable.dza_write, R.drawable.wa_write,
		R.drawable.zha_write, R.drawable.za_write, R.drawable.ah_write, R.drawable.ya_write,
		R.drawable.ra_write, R.drawable.la_write, R.drawable.sha_write, R.drawable.sa_write,
		R.drawable.ha_write, R.drawable.a_write
	};
	
	static int[] mCanvasRid = new int[] {
		R.drawable.ka, R.drawable.kha, R.drawable.ga, R.drawable.nga,
		R.drawable.cha, R.drawable.chha, R.drawable.ja, R.drawable.nya,
		R.drawable.ta, R.drawable.tha, R.drawable.da, R.drawable.na,
		R.drawable.pa, R.drawable.pha, R.drawable.ba, R.drawable.ma,
		R.drawable.tsa, R.drawable.tsha, R.drawable.dza, R.drawable.wa,
		R.drawable.zha, R.drawable.za, R.drawable.ah, R.drawable.ya,
		R.drawable.ra, R.drawable.la, R.drawable.sha, R.drawable.sa,
		R.drawable.ha, R.drawable.a
	};
	
	@ViewById(id = R.id.imgWrite) private ImageView mImgWrite;
	@ViewById(id = R.id.layoutCanvas) private View mLayoutCanvas;
	@ViewById(id = R.id.viewPlay) private View mViewPlay;
	@ViewById(id = R.id.drawCancasView) private DrawCancasView mCanvas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_character);
		
		try {
			AndroidAnnotationParser.parse(this, findViewById(android.R.id.content));
		} catch (Exception e) {
			e.printStackTrace();
			finish();
			return;
		}
		
		if (mSoundPool == null) {
        	mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        	
        	for (int i = 0; i < 30; i++) {
        		//soundId[i] = mSoundPool.load(this, soundRid[i], 0);
        		soundId[i] = -1;
        	}
        }
		
		mViewPlay.setSoundEffectsEnabled(false);
		
		updateImage();	
	}
	
	public static void newInstance(Activity act, int charIndex) {
		mCharIndex = charIndex;
		Intent intent = new Intent(act, CharacterActivity.class);
		act.startActivity(intent);
	}
	
	private void updateImage() {
		mImgWrite.setImageResource(mWriteRid[mCharIndex]);
		mLayoutCanvas.setBackgroundResource(mCanvasRid[mCharIndex]);
		if (soundId[mCharIndex] == -1)
			soundId[mCharIndex] = mSoundPool.load(this, soundRid[mCharIndex], 0);
	}
	
	private void clearCanvas() {
		
	}
	
	@Click(id = R.id.viewPrev)
	private void onPrevClick(View v) {
		if (mCharIndex <= 0)
			return;
		
		mCharIndex--;
		updateImage();
		clearCanvas();
	}

	@Click(id = R.id.viewNext)
	private void onNextClick(View v) {
		if (mCharIndex >= 29)
			return;
		
		mCharIndex++;
		updateImage();
		clearCanvas();
	}
	
	@Click(id = R.id.viewHome)
	private void onHomeClick(View v) {
		finish();
	}
	
	@Click(id = R.id.viewEraser)
	private void onEraserClick(View v) {
		mCanvas.clear();
	}
	
	@Click(id = R.id.viewPlay)
	private void onPlayClick(View v) {
		mSoundPool.play(soundId[mCharIndex], 1, 1, 0, 0, 1);
	}
}
