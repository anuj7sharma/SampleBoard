package com.sampleboard.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.sampleboard.R;
import com.sampleboard.utils.SharedPreferencesHandler;
import com.sampleboard.view.otpLogin.LoginActivity;

/**
 * Created by Anuj Sharma on 4/5/2017.
 */

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String userID = SharedPreferencesHandler.getStringValues(this,getString(R.string.pref_user_id));
        Intent intent = null;
        if(TextUtils.isEmpty(userID)){
            //navigate to Login Screen
            intent = new Intent(this,LoginActivity.class);
        }else{
            intent = new Intent(this,DashBoardActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
