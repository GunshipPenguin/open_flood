package com.gunshippenguin.openflood;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Activity allowing the user to play the actual game.
 */
public class GameActivity extends AppCompatActivity {
    private final int UPDATE_SETTINGS = 1;
    private final int NEW_GAME = 2;
    private final String GAME_SETTINGS_SHAREDPREFS = "com.gunshippenguin.open_flood.shared_prefs_file";

    private Game game;
    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;

    private FloodView floodView;
    private TextView stepsTextView;

    private int lastColor;

    // Paints to be used for the board
    private Paint paints[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Initialize the SharedPreferences and SharedPreferences editor
        sp = getSharedPreferences(GAME_SETTINGS_SHAREDPREFS, Context.MODE_PRIVATE);
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
              launchSettingsIntent.putExtra("boardSize", sp.getInt("board_size", getBoardSize()));
              launchSettingsIntent.putExtra("numColors", sp.getInt("num_colors", getNumColors()));
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
            setBoardSize(defaultBoardSize);
        }
        return sp.getInt("board_size", defaultBoardSize);
    }

    private int getNumColors(){
        int defaultNumColors = getResources().getInteger(R.integer.default_num_colors);
        if (!sp.contains("num_colors")) {
            setNumColors(defaultNumColors);
        }
        return sp.getInt("num_colors", defaultNumColors);
    }

    private void setBoardSize(int boardSize){
        spEditor.putInt("board_size", boardSize);
        spEditor.apply();
        return;
    }

    private void setNumColors(int numColors){
        spEditor.putInt("num_colors", numColors);
        spEditor.apply();
        return;
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
        game = new Game(getBoardSize(), getNumColors());
        lastColor = game.getColor(0, 0);

        // Add color buttons
        LinearLayout buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);
        buttonLayout.removeAllViews();
        Resources resources = getResources();
        for (int i = 0; i < getNumColors(); i++) {
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
        floodView.setBoardSize(getBoardSize());
        floodView.drawGame(game);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == UPDATE_SETTINGS) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                // Only update the gameSettings and create a new game if they have been changed
                if (getBoardSize() != extras.getInt("boardSize")
                        || getNumColors() != extras.getInt("numColors")) {
                    setBoardSize(extras.getInt("boardSize"));
                    setNumColors(extras.getInt("numColors"));
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
