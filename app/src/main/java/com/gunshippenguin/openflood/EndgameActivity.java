package com.gunshippenguin.openflood;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Dialog Activity that is displayed to the user upon a win or loss.
 */
public class EndgameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endgame);

        // Fill 95% of the screen with the dialog
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.95);
        getWindow().setLayout(screenWidth, ViewGroup.LayoutParams.WRAP_CONTENT);

        setFinishOnTouchOutside(false);

        boolean gameWon = getIntent().getExtras().getBoolean("gameWon");

        // Set up the dialog's title
        TextView endgameTitleTextView = (TextView) findViewById(R.id.endGameTitle);
        if (gameWon) {
            endgameTitleTextView.setText(getString(R.string.endgame_win_title));
        } else {
            endgameTitleTextView.setText(getString(R.string.endgame_lose_title));
        }

        // Set up dialog's other text views
        TextView endgameTextView = (TextView) findViewById(R.id.endGameText);
        TextView highScoreTextView = (TextView) findViewById(R.id.highScoreText);
        ImageView highScoreMedalImageView = (ImageView) findViewById(R.id.highScoreMedalImageView);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        HighScoreManager highScoreManager = new HighScoreManager(sp);

        int boardSize = sp.getInt("board_size", -1);
        int numColors = sp.getInt("num_colors", -1);

        if (gameWon) {
            int steps = getIntent().getExtras().getInt("steps");
            String stepsString = String.format(getString(R.string.endgame_win_text),
                    steps);
            endgameTextView.setText(stepsString);

            if (highScoreManager.isHighScore(boardSize, numColors, steps)) {
                highScoreManager.setHighScore(boardSize, numColors, steps);
                highScoreTextView.setText(String.format(getString(R.string.endgame_new_highscore_text),
                        steps));
                highScoreTextView.setTypeface(null, Typeface.BOLD);
            } else {
                highScoreTextView.setText(String.format(getString(R.string.endgame_old_highscore_text),
                        highScoreManager.getHighScore(boardSize, numColors)));
                highScoreMedalImageView.setVisibility(View.GONE);
            }

        } else {
            endgameTextView.setVisibility(View.GONE);
            if (highScoreManager.highScoreExists(boardSize, numColors)) {
                highScoreTextView.setText(String.format(getString(R.string.endgame_old_highscore_text),
                        highScoreManager.getHighScore(boardSize, numColors)));
                highScoreMedalImageView.setVisibility(View.GONE);
            } else {
                highScoreTextView.setVisibility(View.GONE);
                highScoreMedalImageView.setVisibility(View.GONE);
            }

        }

        // Show the replay butotn if the game has been lost
        Button replayButton = (Button) findViewById(R.id.replayButton);
        if (gameWon) {
            replayButton.setVisibility(View.GONE);
        } else {
            replayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("replayGame", true);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            });
        }

        // Set up the new game button callback
        Button newGameButton = (Button) findViewById(R.id.newGameButton);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK, new Intent());
                finish();
            }
        });
    }
}
