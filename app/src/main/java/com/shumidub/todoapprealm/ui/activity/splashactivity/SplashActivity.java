package com.shumidub.todoapprealm.ui.activity.splashactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.shumidub.todoapprealm.R;
import com.shumidub.todoapprealm.ui.activity.mainactivity.MainActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
