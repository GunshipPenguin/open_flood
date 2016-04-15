package com.gunshippenguin.openflood;

import android.app.Dialog;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ClearHighScoresDialogFragment extends DialogFragment {

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_highscores_clear_confirm, null);
        final AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);
        dialog = builder.create();

        Button confirmButton = (Button) dialogView.findViewById(R.id.confirmHighScoresClearButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HighScoreManager highScoreManager = new HighScoreManager(
                        PreferenceManager.getDefaultSharedPreferences(getContext()));
                for (int boardSize : getResources().getIntArray(R.array.boardSizeChoices)) {
                    for (int numColors : getResources().getIntArray(R.array.numColorsChoices)) {
                        highScoreManager.removeHighScore(boardSize, numColors);
                    }
                }
                dialog.dismiss();

                Toast toast = Toast.makeText(getContext(),
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
        return dialog;
    }
}
