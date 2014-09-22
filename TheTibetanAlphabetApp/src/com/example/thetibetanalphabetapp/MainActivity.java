package com.example.thetibetanalphabetapp;

import com.cycrix.androidannotation.Click;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class MainActivity extends Activity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ActionBar ab = getActionBar();
        ab.setDisplayShowCustomEnabled(true);
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ab.setCustomView(R.layout.home_action_bar); // Id of the custom_action_bar
        
        ViewGroup layoutGrid = (ViewGroup) findViewById(R.id.layoutGrid);
        
        for (int row = 0; row < 8; row++) {
        	ViewGroup layoutRow = (ViewGroup) layoutGrid.getChildAt(row);
        	for (int col = 0; col < 4; col++) {
        		int index = row * 4 + col;
        		View cell = layoutRow.getChildAt(col);
        		cell.setTag(index);
        		cell.setOnClickListener(this);
        	}
        }
        
        findViewById(R.id.btnQuiz).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				QuizActivity.newInstance(MainActivity.this);
			}
		});
        
        findViewById(R.id.btnAbout).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onAboutClick(null);
			}
		});
    }

	@Override
	public void onClick(View v) {
		int index = (Integer) v.getTag();
		
		if (index >= 30)
			return;
		
		CharacterActivity.newInstance(this, index);
	}
	
	@Click(id = R.id.btnAbout)
	private void onAboutClick(View v) {
		Intent intent = new Intent(this, AboutActivity.class);
		startActivity(intent);
	}
}
