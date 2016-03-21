package com.gunshippenguin.openflood;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

        // Set up the dialog's text
        TextView endgameTextView = (TextView) findViewById(R.id.endGameText);
        if (gameWon) {
            String stepsString = String.format(getString(R.string.endgame_win_text),
                    getIntent().getExtras().getInt("steps"));
            endgameTextView.setText(stepsString);
        } else {
            endgameTextView.setVisibility(View.GONE);
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
