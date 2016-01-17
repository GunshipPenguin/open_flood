package com.gunshippenguin.openflood;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

/**
 * Activity allowing the user to configure settings.
 */
public class SettingsActivity extends AppCompatActivity {
    public static final int boardSizeChoices[] = {
            10,
            12,
            14,
            18,
            20,
            22,
            24,
    };

    public static final int numColorsChoices[] = {
            3,
            4,
            5,
            6,
            7,
            8,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Set up the board size spinner
        final Spinner boardSizeSpinner = (Spinner) findViewById(R.id.boardSizeSpinner);
        ArrayAdapter<String> boardSizesAdapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item);
        for (int currBoardSize : boardSizeChoices) {
            boardSizesAdapter.add(currBoardSize + "x" + currBoardSize);
        }
        boardSizeSpinner.setAdapter(boardSizesAdapter);

        int currBoardSize = getIntent().getExtras().getInt("boardSize");
        for (int i = 0; i < boardSizeChoices.length; i++) {
            if (boardSizeChoices[i] == currBoardSize) {
                boardSizeSpinner.setSelection(i);
            }
        }

        // Set up the num colors spinner
        final Spinner numColorsSpinner = (Spinner) findViewById(R.id.numColorsSpinner);
        ArrayAdapter<String> numColorsAdapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item);
        for (int currNumColors : numColorsChoices) {
            numColorsAdapter.add(Integer.toString(currNumColors));
        }
        numColorsSpinner.setAdapter(numColorsAdapter);

        int currNumColors = getIntent().getExtras().getInt("numColors");
        for (int i = 0; i < numColorsChoices.length; i++) {
            if (numColorsChoices[i] == currNumColors) {
                numColorsSpinner.setSelection(i);
            }
        }

        // Set up the apply button
        Button applyButton = (Button) findViewById(R.id.applyButton);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int boardSize = boardSizeChoices[boardSizeSpinner.getSelectedItemPosition()];
                int numColors = numColorsChoices[numColorsSpinner.getSelectedItemPosition()];
                Intent dataIntent = new Intent();
                dataIntent.putExtra("boardSize", boardSize);
                dataIntent.putExtra("numColors", numColors);
                setResult(RESULT_OK, dataIntent);
                finish();
            }
        });
    }

    public static int getDefaultBoardSize() {
        return boardSizeChoices[boardSizeChoices.length / 2];
    }

    public static int getDefaultNumColors() {
        return numColorsChoices[numColorsChoices.length / 2];
    }
}
