package com.sampleboard.view.fragment.profile;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.sampleboard.R;
import com.sampleboard.permission.PermissionsAndroid;
import com.sampleboard.utils.TouchImageView;
import com.sampleboard.utils.Utils;
import com.sampleboard.view.BaseFragment;
import com.sampleboard.view.activity.ProfileActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.databinding.adapters.ImageViewBindingAdapter.setImageUri;

/**
 * Created by anuj on 4/8/2017.
 */

public class ProfilePicFragment extends BaseFragment {
    private View rootView;
    private Toolbar mToolbar;
    private TouchImageView mProfileImage;
    private ProgressBar mProgressBar;
    private String profilePicUrl;

    //request code
    private int IMAGE_CAPTURE_REQUEST_CODE = 0;
    private int IMAGE_CHOOSER_REQUEST_CODE = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_profile_pic,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_edit:
                //show Edit options
                if(PermissionsAndroid.getInstance().checkCameraPermission(getActivity())){
                    showOptins();
                }
                else{
                    PermissionsAndroid.getInstance().requestForCameraPermission(ProfilePicFragment.this);
                }
                break;
            case R.id.action_share:
                //show share dialog
                break;
        }
        return true;
    }

    private void showOptins() {
        PackageManager packageManager = getActivity().getPackageManager();
        List<Intent> cameraIntents = new ArrayList<Intent>();
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, IMAGE_CAPTURE_REQUEST_CODE);
        for (ResolveInfo res : listCam) {
            String packageName = res.activityInfo.packageName;
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri(getActivity()));
            cameraIntents.add(intent);
        }

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(galleryIntent, "Choose Option");

        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));
        getActivity().startActivityForResult(chooserIntent, IMAGE_CHOOSER_REQUEST_CODE);
    }

    // function to set the path of picture captured from camera.
    private Uri setImageUri(Context context) {
        checkFolder(context);
        File file = new File(Environment.getExternalStorageDirectory()
                + "/" + context.getResources().getString(R.string.app_name) + "/originalImage.png");
        Uri imgUri = Uri.fromFile(file);
        return imgUri;
    }
    private void checkFolder(Context context) {
        try {
            File folder = new File(Environment.getExternalStorageDirectory() + "/" + context.getResources().getString(R.string.app_name));
            if (!folder.exists()) {
                folder.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile_pic,container,false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    private void initViews() {
        mToolbar = (Toolbar)rootView.findViewById(R.id.toolbar);
        ((ProfileActivity)getActivity()).setSupportActionBar(mToolbar);
        ((ProfileActivity)getActivity()).getSupportActionBar().setTitle("Profile Pic");
        mToolbar.setNavigationIcon(R.drawable.ic_navigation_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ProfileActivity)getActivity()).oneStepBack();
            }
        });
        mProfileImage = (TouchImageView)rootView.findViewById(R.id.profile_pic);
        mProgressBar = (ProgressBar)rootView.findViewById(R.id.progress_bar);

        try {
            if(getArguments()!=null && getArguments().getString("profile_pic")!=null){
                profilePicUrl = getArguments().getString("profile_pic");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if(!TextUtils.isEmpty(profilePicUrl)){
            mProgressBar.setVisibility(View.VISIBLE);
            Picasso.with(getActivity()).load(profilePicUrl).into(mProfileImage, new Callback() {
                @Override
                public void onSuccess() {
                    mProgressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    mProgressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PermissionsAndroid.CAMERA_PERMISSION_REQUEST_CODE:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    showOptins();
                }else{
                    //permission denied
                    Utils.getInstance().showToast("Camera permission required");
                }
                break;
        }
    }
}
