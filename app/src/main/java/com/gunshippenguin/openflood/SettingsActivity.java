package com.gunshippenguin.openflood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

/**
 * Activity allowing the user to configure settings.
 */
public class SettingsActivity extends AppCompatActivity {
    Spinner boardSizeSpinner, numColorsSpinner;
    CheckBox colorBlindCheckBox;
    int[] boardSizeChoices, numColorsChoices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        // Set up the board size spinner
        boardSizeSpinner = (Spinner) findViewById(R.id.boardSizeSpinner);
        ArrayAdapter<BoardSize> boardSizesAdapter = new ArrayAdapter<BoardSize>(this,
                R.layout.support_simple_spinner_dropdown_item);
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
                R.layout.support_simple_spinner_dropdown_item);
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

        // Set up the apply button
        Button applyButton = (Button) findViewById(R.id.applyButton);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dataIntent = new Intent();
                dataIntent.putExtra("settingsChanged", saveSettings());
                setResult(RESULT_OK, dataIntent);
                finish();
            }
        });
    }

    private boolean saveSettings() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor spEditor = sp.edit();
        boolean settingsChanged = false;

        // Update boardSize
        int selectedBoardSize = ((BoardSize) boardSizeSpinner.getSelectedItem()).getBoardSize();
        int defaultBoardSize = getResources().getInteger(R.integer.default_board_size);
        if (selectedBoardSize != sp.getInt("board_size", defaultBoardSize)) {
            settingsChanged = true;
            spEditor.putInt("board_size", selectedBoardSize);
        }

        // Update number of colors
        int selectedNumColors = ((ColorNum) numColorsSpinner.getSelectedItem()).getColorNum();
        int defaultNumColors = getResources().getInteger(R.integer.default_num_colors);
        if (selectedNumColors != sp.getInt("num_colors", defaultNumColors)) {
            settingsChanged = true;
            spEditor.putInt("num_colors", selectedNumColors);
        }

        // Update color blind mode
        spEditor.putBoolean("color_blind_mode", colorBlindCheckBox.isChecked());

        spEditor.apply();
        return settingsChanged;
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
