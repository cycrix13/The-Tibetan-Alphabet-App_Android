package com.example.thetibetanalphabetapp;

import java.util.Currency;
import java.util.Random;

import com.cycrix.androidannotation.AndroidAnnotationParser;
import com.cycrix.androidannotation.ViewById;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class QuizActivity extends Activity implements OnClickListener {
	
	public static int[] mQuizRid = new int[] {
		R.drawable.ka_quiz, R.drawable.kha_quiz, R.drawable.ga_quiz, R.drawable.nga_quiz,
		R.drawable.cha_quiz, R.drawable.chha_quiz, R.drawable.ja_quiz, R.drawable.nya_quiz,
		R.drawable.ta_quiz, R.drawable.tha_quiz, R.drawable.da_quiz, R.drawable.na_quiz,
		R.drawable.pa_quiz, R.drawable.pha_quiz, R.drawable.ba_quiz, R.drawable.ma_quiz,
		R.drawable.tsa_quiz, R.drawable.tsha_quiz, R.drawable.dza_quiz, R.drawable.wa_quiz,
		R.drawable.zha_quiz, R.drawable.za_quiz, R.drawable.ah_quiz, R.drawable.ya_quiz,
		R.drawable.ra_quiz, R.drawable.la_quiz, R.drawable.sha_quiz, R.drawable.sa_quiz,
		R.drawable.ha_quiz, R.drawable.a_quiz
	};
	
	public static String[] mWordArr = new String[] {
		"ka", "kha", "ga", "nga",
		"cha", "chha", "ja", "nya",
		"ta", "tha", "da", "na",
		"pa", "pha", "ba", "ma",
		"tsa", "tsha", "dza", "wa",
		"zha", "za", "ah", "ya",
		"ra", "la", "sha", "sa",
		"ha", "a"
	};
	
	@ViewById(id = R.id.btnAnswer1)	private Button mBtnAnswer1;
	@ViewById(id = R.id.btnAnswer2)	private Button mBtnAnswer2;
	@ViewById(id = R.id.btnAnswer3)	private Button mBtnAnswer3;
	@ViewById(id = R.id.btnAnswer4)	private Button mBtnAnswer4;
	private Button[] mBtnArr;
	@ViewById(id = R.id.txtQuestion)private TextView mTxtQuestion;
	@ViewById(id = R.id.txtCorrect) private TextView mTxtCorrect;
	@ViewById(id = R.id.imgQuestion)private ImageView mImgQuestion;
	@ViewById(id = R.id.imgIndicator)private ImageView mImgIndicator;
	@ViewById(id = R.id.layoutAnswer)private View mLayoutAnswer;
	
	private int[] questions = new int[30];
	private Random random = new Random();
	
	private int questionIndex = 0;
	private int correct = 0;
	private int[] answerArr;
	
	public static void newInstance(Activity act) {
		Intent intent = new Intent(act, QuizActivity.class);
		act.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);
		
		ActionBar ab = getActionBar();
        ab.setDisplayShowCustomEnabled(true);
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ab.setCustomView(R.layout.quiz_action_bar); // Id of the custom_action_bar
		
		try {
			AndroidAnnotationParser.parse(this, findViewById(android.R.id.content));
		} catch (Exception e) {
			e.printStackTrace();
			finish();
			return;
		}
		
		getActionBar().setTitle("Quiz");
		
		mBtnArr = new Button[] {mBtnAnswer1, mBtnAnswer2, mBtnAnswer3, mBtnAnswer4};
		
		for (int i = 0; i < 4; i++) {
			mBtnArr[i].setOnClickListener(this);
			mBtnArr[i].setTag(i);
		}
		
		for (int i = 0; i < 30; i++)
			questions[i] = i;
		
		// Scramble
		for (int i = 0; i < 100; i++) {
			int p1 = random.nextInt(30);
			int p2 = random.nextInt(30);
			int temp = questions[p1];
			questions[p1] = questions[p2];
			questions[p2] = temp;
		}
		
		updateLayout();
	}

	private void updateLayout() {
		
		answerArr = new int[] {questions[questionIndex], 0, 0, 0};
		
		for (int i = 1; i < 4; i++) {
			do {
				answerArr[i] = random.nextInt(30);
			} while (isDuplicate(answerArr, i));
		}

		// Scramble
		for (int i = 0; i < 100; i++) {
			int p1 = random.nextInt(4);
			int p2 = random.nextInt(4);
			int temp = answerArr[p1];
			answerArr[p1] = answerArr[p2];
			answerArr[p2] = temp;
		}
		
		String[] abcd = new String[] {"A", "B", "C", "D"};
		for (int i = 0; i < 4; i++) {
			mBtnArr[i].setText(abcd[i] + ". " + mWordArr[answerArr[i]].toUpperCase());
		}
		
		mTxtQuestion.setText("" + (questionIndex + 1) + "/30");
		mTxtCorrect.setText("Correct: " + correct);
		
		mImgQuestion.setImageResource(mQuizRid[questions[questionIndex]]);
	}
	
	private boolean isDuplicate(int[] arr, int index) {
		for (int i = 0; i < index; i++)
			if (arr[i] == arr[index])
				return true;
		
		return false;
	}
	
	@Override
	public void onClick(View v) {
		int index = (Integer) v.getTag();
		
		if (answerArr[index] == questions[questionIndex]) {
			// Right
			mImgIndicator.setImageResource(R.drawable.right);
			correct++;
			mTxtCorrect.setText("Correct: " + correct);
		} else {
			// Wrong
			mImgIndicator.setImageResource(R.drawable.wrong);
		}
		
		float height = mLayoutAnswer.getHeight() / 4;
		float x = (mLayoutAnswer.getWidth() - mImgIndicator.getWidth()) / 2;
		float y = mLayoutAnswer.getY() + (height - mImgIndicator.getHeight()) / 2 + height * index;
		mImgIndicator.setTranslationX(x);
		mImgIndicator.setTranslationY(y);
		mImgIndicator.setVisibility(View.VISIBLE);
		
		for (Button btn : mBtnArr)
			btn.setEnabled(false);
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				nextQuestion();
			}
		}, 1000);
	}
	
	private void nextQuestion() {
		questionIndex++;
		if (questionIndex >= 30) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Finish!");
			builder.setMessage("Correct: " + correct + "/30");
			builder.setCancelable(false);
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					finish();
				}
			});
			builder.show();
		} else {
			mImgIndicator.setVisibility(View.INVISIBLE);
			for (Button btn : mBtnArr)
				btn.setEnabled(true);
			
			updateLayout();
		}
	}
}
