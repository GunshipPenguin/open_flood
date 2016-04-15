package com.gunshippenguin.openflood;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Dialog Fragment that is displayed to the user upon a win or loss.
 */
public class EndGameDialogFragment extends DialogFragment {

    public interface EndGameDialogFragmentListener {
        public void onReplayClick();
        public void onNewGameClick();
    }

    EndGameDialogFragmentListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get steps and maxSteps from the arguments
        int steps = getArguments().getInt("steps");
        int maxSteps = getArguments().getInt("max_steps");
        boolean gameWon = steps <= maxSteps;

        // Inflate layout
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_endgame, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(layout);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        // Set up the dialog's title
        TextView endgameTitleTextView = (TextView) layout.findViewById(R.id.endGameTitle);
        if (gameWon) {
            endgameTitleTextView.setText(getString(R.string.endgame_win_title));
        } else {
            endgameTitleTextView.setText(getString(R.string.endgame_lose_title));
        }

        // Set up dialog's other text views
        TextView endgameTextView = (TextView) layout.findViewById(R.id.endGameText);
        TextView highScoreTextView = (TextView) layout.findViewById(R.id.highScoreText);
        ImageView highScoreMedalImageView = (ImageView) layout.findViewById(R.id.highScoreMedalImageView);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        HighScoreManager highScoreManager = new HighScoreManager(sp);

        int boardSize = sp.getInt("board_size", -1);
        int numColors = sp.getInt("num_colors", -1);

        if (gameWon) {
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
        Button replayButton = (Button) layout.findViewById(R.id.replayButton);
        if (gameWon) {
            replayButton.setVisibility(View.GONE);
        } else {
            replayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onReplayClick();
                    dismiss();
                }
            });
        }

        // Set up the new game button callback
        Button newGameButton = (Button) layout.findViewById(R.id.newGameButton);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNewGameClick();
                dismiss();
            }
        });
        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (EndGameDialogFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement EndGameDialogListener");
        }
    }

}
