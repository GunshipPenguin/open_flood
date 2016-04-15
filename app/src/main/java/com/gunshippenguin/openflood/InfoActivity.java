package com.gunshippenguin.openflood;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity displaying information about the application.
 */
public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        // Set up the first line of the info
        TextView versionTextView = (TextView) findViewById(R.id.infoVersionTextView);
        String appName = getResources().getString(R.string.app_name);

        PackageInfo pInfo;
        try {
            pInfo = getPackageManager().getPackageInfo(this.getPackageName(), 0);
            versionTextView.setText(appName + " " + pInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            versionTextView.setText(appName);
        }

        // Set up the source link
        TextView sourceTextView = (TextView) findViewById(R.id.infoSourceTextView);
        sourceTextView.setMovementMethod(LinkMovementMethod.getInstance());

        // Set up the back button
        Button backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.appNameTextView).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast toast = Toast.makeText(InfoActivity.this,
                        "Eric Hamber Secondary Class of 2016",
                        Toast.LENGTH_LONG);
                toast.show();
                return true;
            }
        });

        return;
    }
}
