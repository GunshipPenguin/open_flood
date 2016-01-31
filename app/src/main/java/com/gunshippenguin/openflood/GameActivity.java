package com.gunshippenguin.openflood;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Activity allowing the user to play the actual game.
 */
public class GameActivity extends AppCompatActivity {
    private final int UPDATE_SETTINGS = 1;
    private final int NEW_GAME = 2;

    private Game game;
    private GameSettings gameSettings;

    private FloodView floodView;
    private TextView stepsTextView;

    private int lastColor;

    // Paints to be used for the board
    private Paint paints[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Initialize the GameSettings
        int[] boardSizeChoices = getResources().getIntArray(R.array.boardSizeChoices);
        int[] numColorsChoices = getResources().getIntArray(R.array.numColorsChoices);
        gameSettings = new GameSettings(boardSizeChoices[boardSizeChoices.length / 2],
                numColorsChoices[numColorsChoices.length / 2]);

        // Get the FloodView
        floodView = (FloodView) findViewById(R.id.floodView);

        // Initialize the paints array and pass it to the FloodView
        initPaints();
        floodView.setPaints(paints);

        ImageButton settingsButton = (ImageButton) findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
              Intent launchSettingsIntent = new Intent(GameActivity.this, SettingsActivity.class);
              launchSettingsIntent.putExtra("boardSize", gameSettings.getBoardSize());
              launchSettingsIntent.putExtra("numColors", gameSettings.getNumColors());
              startActivityForResult(launchSettingsIntent, UPDATE_SETTINGS);
          }
      }
        );

        ImageButton infoButton = (ImageButton) findViewById(R.id.infoButton);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchSettingsIntent = new Intent(GameActivity.this, InfoActivity.class);
                startActivity(launchSettingsIntent);
            }
        });

        ImageButton newGameButton = (ImageButton) findViewById(R.id.newGameButton);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGame();
            }
        });

        // Get the steps text view
        stepsTextView = (TextView) findViewById(R.id.stepsTextView);

        // Set up a new game
        newGame();
    }

    private void initPaints() {
        int[] colors = getResources().getIntArray(R.array.boardColorScheme);
        paints = new Paint[colors.length];
        for (int i = 0; i < colors.length; i++) {
            paints[i] = new Paint();
            paints[i].setColor(colors[i]);
        }
        return;
    }

    private void newGame() {
        game = new Game(gameSettings.getBoardSize(), gameSettings.getNumColors());
        lastColor = game.getColor(0, 0);

        // Add color buttons
        LinearLayout buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);
        buttonLayout.removeAllViews();
        Resources resources = getResources();
        for (int i = 0; i < gameSettings.getNumColors(); i++) {
            final int localI = i;
            ImageView newButton = new ImageView(this);
            newButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(AnimationUtils.loadAnimation(GameActivity.this, R.anim.button_anim));
                    if (localI != lastColor) {
                        doColor(localI);
                    }
                }
            });
            newButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));

            Drawable buttonDrawable = ContextCompat.getDrawable(this, R.drawable.button);
            buttonDrawable.setColorFilter(paints[i].getColor(), PorterDuff.Mode.SRC_ATOP);
            newButton.setImageDrawable(buttonDrawable);

            newButton.setPadding(5, 5, 5, 5);
            buttonLayout.addView(newButton);
        }

        stepsTextView.setText(game.getSteps() + " / " + game.getMaxSteps());
        floodView.setBoardSize(gameSettings.getBoardSize());
        floodView.drawGame(game);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == UPDATE_SETTINGS) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                // Only update the gameSettings and create a new game if they have been changed
                if (gameSettings.getBoardSize() != extras.getInt("boardSize")
                        || gameSettings.getNumColors() != extras.getInt("numColors")) {
                    gameSettings.setBoardSize(extras.getInt("boardSize"));
                    gameSettings.setNumColors(extras.getInt("numColors"));
                    newGame();
                }
            }
        } else if (requestCode == NEW_GAME) {
            newGame();
        }
    }

    private void doColor(int color) {
        if (game.getSteps() >= game.getMaxSteps()) {
            return;
        }

        game.flood(color);
        floodView.drawGame(game);
        lastColor = color;
        stepsTextView.setText(game.getSteps() + " / " + game.getMaxSteps());

        if (game.checkWin() || game.getSteps() == game.getMaxSteps()) {
            showEndGameActivity();
        }

        return;
    }

    private void showEndGameActivity() {
        Intent launchEndgameIntent = new Intent(GameActivity.this, EndgameActivity.class);
        launchEndgameIntent.putExtra("gameWon", game.checkWin());
        launchEndgameIntent.putExtra("steps", game.getSteps());
        startActivityForResult(launchEndgameIntent, NEW_GAME);
        return;
    }
}
