package com.androidpay.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.text.TextUtils;

import com.androidpay.R;
import com.androidpay.utils.SharedPreferencesHandler;
import com.androidpay.view.otpLogin.LoginActivity;

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
