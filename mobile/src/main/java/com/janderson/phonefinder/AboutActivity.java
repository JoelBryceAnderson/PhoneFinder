package com.janderson.phonefinder;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.janderson.phonefinder.R;

public class AboutActivity extends Activity {

    private int clickCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getActionBar().setTitle("About");
        LinearLayout aboutLayout = (LinearLayout) findViewById(R.id.about_layout);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        final TextView nameCard = (TextView) findViewById(R.id.joel_name_card);
        nameCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickCount++;
                if (clickCount == 5) {
                    if (nameCard.getText().equals("Beta Pi 1500")) {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            nameCard.animate().translationX(2000).setDuration(350)
                                    .setListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            nameCard.setText("By Joel Anderson");
                                            nameCard.setTranslationX(-2000);
                                            nameCard.animate().translationX(0).setDuration(350).
                                                    setListener(null);
                                        }
                                    });
                        } else {
                            nameCard.animate().translationX(2000).setDuration(350)
                                    .withEndAction(new Runnable() {
                                        @Override
                                        public void run() {
                                            nameCard.setText("By Joel Anderson");
                                            nameCard.setTranslationX(-2000);
                                            nameCard.animate().translationX(0).setDuration(350);
                                        }
                                    });
                        }
                        clickCount = 0;
                    } else {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            nameCard.animate().translationX(2000).setDuration(350)
                                    .setListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            nameCard.setText("Beta Pi 1500");
                                            nameCard.setTranslationX(-2000);
                                            nameCard.animate().translationX(0).setDuration(350)
                                                    .setListener(null);
                                        }
                                    });
                        } else {
                            nameCard.animate().translationX(2000).setDuration(350)
                                    .withEndAction(new Runnable() {
                                        @Override
                                        public void run() {
                                            nameCard.setText("Beta Pi 1500");
                                            nameCard.setTranslationX(-2000);
                                            nameCard.animate().translationX(0).setDuration(350);
                                        }
                                    });
                        }
                        clickCount = 0;
                    }
                }
            }
        });
        nameCard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (nameCard.getText().equals("Beta Pi 1500")) {
                    Toast.makeText(
                            getApplicationContext(),
                            "Suck it other fraternities!", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        final TextView emailCard = (TextView) findViewById(R.id.email_card);
        emailCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void sendEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "JAnderson97@mail.gatech.edu", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Phone Finder Feedback");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }
}
