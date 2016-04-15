package com.gunshippenguin.openflood;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Dialog allowing the user to enter a seed to start a new game from.
 */
public class SeedDialogFragment extends DialogFragment {

    public interface SeedDialogFragmentListener {
        public void onNewGameFromSeedClick(String seed);
    }

    SeedDialogFragmentListener listener;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View layout = inflater.inflate(R.layout.dialog_seed, null);
        final AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(layout);
        dialog = builder.create();

        final EditText seedEditText = (EditText) layout.findViewById(R.id.seedEditText);
        seedEditText.requestFocus();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        seedEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_DONE:
                        listener.onNewGameFromSeedClick(seedEditText.getText().toString());
                        dialog.dismiss();
                        break;
                }
                return true;
            }
        });

        Button startGameButton = (Button) layout.findViewById(R.id.startGameFromSeedButton);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNewGameFromSeedClick(seedEditText.getText().toString());
                dialog.dismiss();
            }
        });

        Button cancelButton = (Button) layout.findViewById(R.id.cancelStartGameFromSeedButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (SeedDialogFragment.SeedDialogFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement SeedDialogFragmentListener");
        }
    }
}
