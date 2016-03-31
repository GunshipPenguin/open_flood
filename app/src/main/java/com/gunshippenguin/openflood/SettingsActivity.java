package com.gunshippenguin.openflood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Activity allowing the user to configure settings.
 */
public class SettingsActivity extends AppCompatActivity {
    Spinner boardSizeSpinner, numColorsSpinner;
    CheckBox colorBlindCheckBox, oldColorsCheckBox;
    int[] boardSizeChoices, numColorsChoices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        // Set up the board size spinner
        boardSizeSpinner = (Spinner) findViewById(R.id.boardSizeSpinner);
        ArrayAdapter<BoardSize> boardSizesAdapter = new ArrayAdapter<BoardSize>(this,
                R.layout.spinner_layout);
        boardSizeChoices = getResources().getIntArray(R.array.boardSizeChoices);
        int currBoardSize = sp.getInt("board_size",
                getResources().getInteger(R.integer.default_board_size));
        boardSizeSpinner.setAdapter(boardSizesAdapter);
        for (int bs : boardSizeChoices) {
            boardSizesAdapter.add(new BoardSize(bs));
            if (bs == currBoardSize) {
                boardSizeSpinner.setSelection(boardSizesAdapter.getCount() - 1);
            }
        }

        // Set up the num colors spinner
        numColorsSpinner = (Spinner) findViewById(R.id.numColorsSpinner);
        ArrayAdapter<ColorNum> numColorsAdapter = new ArrayAdapter<ColorNum>(this,
                R.layout.spinner_layout);
        numColorsChoices = getResources().getIntArray(R.array.numColorsChoices);
        int currNumColors = sp.getInt("num_colors",
                getResources().getInteger(R.integer.default_num_colors));
        numColorsSpinner.setAdapter(numColorsAdapter);
        for (int nc : numColorsChoices) {
            numColorsAdapter.add(new ColorNum(nc));
            if (nc == currNumColors) {
                numColorsSpinner.setSelection(numColorsAdapter.getCount() - 1);
            }
        }

        // Set up the color blind checkbox
        colorBlindCheckBox = (CheckBox) findViewById(R.id.colorBlindCheckBox);
        colorBlindCheckBox.setChecked(sp.getBoolean("color_blind_mode", false));

        // Set up the old color scheme checkbox
        oldColorsCheckBox = (CheckBox) findViewById(R.id.oldColorsCheckBox);
        oldColorsCheckBox.setChecked(sp.getBoolean("use_old_colors", false));

        // Set up the clear highscores button
        Button clearHighScoresButton = (Button) findViewById(R.id.clearHighScoresButton);
        clearHighScoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(SettingsActivity.this);
                View dialogView = inflater.inflate(R.layout.dialog_highscores_clear_confirm, null);
                final AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setView(dialogView);
                dialog = builder.create();

                Button confirmButton = (Button) dialogView.findViewById(R.id.confirmHighScoresClearButton);
                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HighScoreManager highScoreManager = new HighScoreManager(
                                PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this));
                        for (int boardSize : getResources().getIntArray(R.array.boardSizeChoices)) {
                            for (int numColors : getResources().getIntArray(R.array.numColorsChoices)) {
                                highScoreManager.removeHighScore(boardSize, numColors);
                            }
                        }
                        dialog.dismiss();
                        Toast toast = Toast.makeText(SettingsActivity.this,
                                getString(R.string.settings_clear_high_scores_toast),
                                Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });

                Button cancelButton = (Button) dialogView.findViewById(R.id.cancelHighScoresClearButton);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        // Set up the apply button
        Button applyButton = (Button) findViewById(R.id.applyButton);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s = saveSettings();
                setResult(RESULT_OK, s);
                finish();
            }
        });
    }

    private Intent saveSettings() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor spEditor = sp.edit();
        Intent dataIntent = new Intent();
        dataIntent.putExtra("gameSettingsChanged", false);
        dataIntent.putExtra("colorSettingsChanged", false);

        // Update boardSize
        int selectedBoardSize = ((BoardSize) boardSizeSpinner.getSelectedItem()).getBoardSize();
        int defaultBoardSize = getResources().getInteger(R.integer.default_board_size);
        if (selectedBoardSize != sp.getInt("board_size", defaultBoardSize)) {
            dataIntent.putExtra("gameSettingsChanged", true);
            spEditor.putInt("board_size", selectedBoardSize);
        }

        // Update number of colors
        int selectedNumColors = ((ColorNum) numColorsSpinner.getSelectedItem()).getColorNum();
        int defaultNumColors = getResources().getInteger(R.integer.default_num_colors);
        if (selectedNumColors != sp.getInt("num_colors", defaultNumColors)) {
            dataIntent.putExtra("gameSettingsChanged", true);
            spEditor.putInt("num_colors", selectedNumColors);
        }

        // Update color blind mode
        boolean selectedColorBlindMode = colorBlindCheckBox.isChecked();
        if (selectedColorBlindMode != sp.getBoolean("color_blind_mode", false)) {
            dataIntent.putExtra("colorSettingsChanged", true);
            spEditor.putBoolean("color_blind_mode", selectedColorBlindMode);
        }

        // Update whether or not to use the old color scheme
        boolean selectedOldColorScheme = oldColorsCheckBox.isChecked();
        if (selectedOldColorScheme != sp.getBoolean("use_old_colors", false)) {
            dataIntent.putExtra("colorSettingsChanged", true);
            spEditor.putBoolean("use_old_colors", selectedOldColorScheme);
        }

        spEditor.apply();
        return dataIntent;
    }

    private class BoardSize {
        private int boardSize;
        public BoardSize(int boardSize) {
            this.boardSize = boardSize;
        }

        public int getBoardSize() {
            return boardSize;
        }

        @Override
        public String toString() {
            return boardSize + "x" + boardSize;
        }
    }

    private class ColorNum {
        private int colorNum;
        public ColorNum(int colorNumber) {
            this.colorNum = colorNumber;
        }

        public int getColorNum() {
            return colorNum;
        }

        @Override
        public String toString() {
            return Integer.toString(colorNum);
        }
    }

}
