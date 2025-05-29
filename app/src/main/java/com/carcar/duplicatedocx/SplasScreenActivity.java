package com.carcar.duplicatedocx;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Window;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;

public class SplasScreenActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "MyAppPrefs";
    private static final String PREF_DONT_SHOW_AGAIN = "dontShowAgain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splas_screen);

        Window window=getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.neon_violate));



        ImageView imageView = findViewById(R.id.cardimage);

        // Load GIF safely
        try {
            GifDrawable gifDrawable = new GifDrawable(getResources(), R.drawable.search);
            gifDrawable.setSpeed(0.5f);
            imageView.setImageDrawable(gifDrawable);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            boolean dontShowAgain = preferences.getBoolean(PREF_DONT_SHOW_AGAIN, false);

            Intent nextIntent =
                    new Intent(SplasScreenActivity.this, MainActivity.class);
            startActivity(nextIntent);
            finish();
        }, 3000);
    }
}
