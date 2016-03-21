package com.gunshippenguin.openflood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
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
    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;

    private FloodView floodView;
    private TextView stepsTextView;

    private int lastColor;

    private boolean gameFinished;

    // Paints to be used for the board
    private Paint paints[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Initialize the SharedPreferences and SharedPreferences editor
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        spEditor = sp.edit();

        // Get the FloodView
        floodView = (FloodView) findViewById(R.id.floodView);

        // Initialize the paints array and pass it to the FloodView
        initPaints();
        floodView.setPaints(paints);

        ImageView settingsButton = (ImageView) findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
              Intent launchSettingsIntent = new Intent(GameActivity.this, SettingsActivity.class);
              startActivityForResult(launchSettingsIntent, UPDATE_SETTINGS);
          }
      }
        );

        ImageView infoButton = (ImageView) findViewById(R.id.infoButton);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchSettingsIntent = new Intent(GameActivity.this, InfoActivity.class);
                startActivity(launchSettingsIntent);
            }
        });

        ImageView newGameButton = (ImageView) findViewById(R.id.newGameButton);
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

    private int getBoardSize(){
        int defaultBoardSize = getResources().getInteger(R.integer.default_board_size);
        if (!sp.contains("board_size")) {
            spEditor.putInt("board_size", defaultBoardSize);
            spEditor.apply();
        }
        return sp.getInt("board_size", defaultBoardSize);
    }

    private int getNumColors(){
        int defaultNumColors = getResources().getInteger(R.integer.default_num_colors);
        if (!sp.contains("num_colors")) {
            spEditor.putInt("num_colors", defaultNumColors);
            spEditor.apply();
        }
        return sp.getInt("num_colors", defaultNumColors);
    }

    private void initPaints() {
        int[] colors;
        if (sp.getBoolean("use_old_colors", false)){
            colors = getResources().getIntArray(R.array.oldBoardColorScheme);
        } else {
            colors = getResources().getIntArray(R.array.boardColorScheme);
        }

        paints = new Paint[colors.length];
        for (int i = 0; i < colors.length; i++) {
            paints[i] = new Paint();
            paints[i].setColor(colors[i]);
        }
        return;
    }

    private void resetGame(){
        game.resetGame();
        gameFinished = false;
        lastColor = game.getColor(0, 0);
        stepsTextView.setText(game.getSteps() + " / " + game.getMaxSteps());
        floodView.drawGame(game);
        return;
    }

    private void newGame() {
        game = new Game(getBoardSize(), getNumColors());
        gameFinished = false;
        lastColor = game.getColor(0, 0);

        layoutColorButtons();

        stepsTextView.setText(game.getSteps() + " / " + game.getMaxSteps());
        floodView.setBoardSize(getBoardSize());
        floodView.drawGame(game);
    }

    private void layoutColorButtons() {
        // Add color buttons
        LinearLayout buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);
        buttonLayout.removeAllViews();
        int buttonPadding = (int) getResources().getDimension(R.dimen.color_button_padding);
        for (int i = 0; i < getNumColors(); i++) {
            final int localI = i;
            ColorButton newButton = new ColorButton(this);
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
            newButton.setPadding(buttonPadding, buttonPadding, buttonPadding, buttonPadding);

            newButton.setColorBlindText(Integer.toString(i + 1));
            newButton.setColor(paints[i].getColor());
            buttonLayout.addView(newButton);
        }
        return;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == UPDATE_SETTINGS) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                // Only start a new game if the settings have been changed
                if (extras.getBoolean("gameSettingsChanged")) {
                    newGame();
                }
                if (extras.getBoolean("colorSettingsChanged")) {
                    initPaints();
                    floodView.setPaints(paints);
                    layoutColorButtons();
                }
            }
        } else if (requestCode == NEW_GAME) {
            if (resultCode == RESULT_OK) {
                if (data.getBooleanExtra("replayGame", false)) {
                    resetGame();
                } else {
                    newGame();
                }
            }
        }
    }

    private void doColor(int color) {
        if (gameFinished || game.getSteps() >= game.getMaxSteps()) {
            return;
        }

        game.flood(color);
        floodView.drawGame(game);
        lastColor = color;
        stepsTextView.setText(game.getSteps() + " / " + game.getMaxSteps());

        if (game.checkWin() || game.getSteps() == game.getMaxSteps()) {
            gameFinished = true;
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
