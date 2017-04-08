package com.sampleboard.view.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sampleboard.MainActivity;
import com.sampleboard.R;
import com.sampleboard.enums.CurrentScreen;

/**
 * Created by Anuj Sharma on 4/6/2017.
 */

public class ProfileActivity extends MainActivity {
    private static ProfileActivity instance;
    public static ProfileActivity getInstance(){
        if(instance==null) new ProfileActivity();
        return instance;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        instance = this;
        if(getIntent()!=null && getIntent().getStringExtra("destination")!=null){
            if(getIntent().getStringExtra("destination").equals("profile_pic")){
                //move to profile pic screen
                Bundle bundle = new Bundle();
                bundle.putString("profile_pic","https://organicthemes.com/demo/profile/files/2012/12/profile_img.png");
                ProfileActivity.getInstance().changeScreen(R.id.profile_container, CurrentScreen.PROFILE_PIC_SCREEN,false,false,bundle);
            }else if(getIntent().getStringExtra("destination").equals("edit_profile")){
                //destination extra came, need to move to edit profile page
                changeScreen(R.id.profile_container, CurrentScreen.EDIT_PROFILE_SCREEN, false,false,null);
            }
        }else{
            changeScreen(R.id.profile_container, CurrentScreen.PROFILE_SCREEN, false,false,null);
        }

    }
}
