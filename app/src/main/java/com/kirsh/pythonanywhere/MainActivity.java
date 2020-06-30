package com.kirsh.pythonanywhere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kirsh.pythonanywhere.server.Communicator;
import com.kirsh.pythonanywhere.server.User;
import com.kirsh.pythonanywhere.work.GetUserDataWorker;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity {

    final static String PRETTY_NAME_PARTIAL_TEXT = "Welcome %s!\nThis is your personal profile!!";

    private String mToken;
    private User mUser;
    private WorkManager mWorkManager;

    private TextView mUserNameTextView;
    private TextView mPrettyNameTextView;
    private ImageView mUserImageView;
    private Button mUpdateDetailsButton;

    private final Data mTokenInputData = new Data.Builder()
            .putString(Shared.TOKEN_TAG, mToken)
            .build();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWorkManager = WorkManager.getInstance(this);
        initUser();
        reloadLocalUserInfo(savedInstanceState);
        initViews();
    }

//    @Override
//    protected void onResume(){
//        super.onResume();
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
//        String username = sp.getString(USERNAME_TAG, null);
//        String prettyName = sp.getString(PRETTY_NAME_TAG, "");
//        String imageUrl = sp.getString(IMAGE_URL_TAG, "");
//        mUser = new User(username, prettyName, imageUrl);
//    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        if (mToken != null){
            outState.putString(Shared.USERNAME_TAG, mUser.username);
            outState.putString(Shared.PRETTY_NAME_TAG, mUser.prettyName);
            outState.putString(Shared.IMAGE_URL_TAG, mUser.imageUrl);
        }
        super.onSaveInstanceState(outState);
    }

    private void initUser(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        mToken = sp.getString(Shared.TOKEN_TAG, null);
        if (mToken == null){
            openActivityCreateNewUser();
        }
        reloadServerUserInfo();
    }

    private void initViews(){
        // username view
        mUserNameTextView = findViewById(R.id.text_view_username);
        mUserNameTextView.setText(mUser.username);

        // pretty name view
        mPrettyNameTextView = findViewById(R.id.text_view_pretty_name);
        setPrettyNameText(mPrettyNameTextView);

        // image view
        mUserImageView = findViewById(R.id.image_view_user_image);
        setImageView(mUserImageView);

        // button view
        mUpdateDetailsButton = findViewById(R.id.button_goto_update_details);
        setButton(mUpdateDetailsButton);
    }

    private void setPrettyNameText(TextView view){
        if (mUser.prettyName.isEmpty()){
            view.setText(String.format(PRETTY_NAME_PARTIAL_TEXT, mUser.username));
        }
        view.setText(String.format(PRETTY_NAME_PARTIAL_TEXT, mUser.prettyName));
    }

    private void setImageView(ImageView view){
        if (mUser.imageUrl.isEmpty()){
            view.setVisibility(View.INVISIBLE);
        }else {
            Picasso.get().load(Communicator.BASE_URL + mUser.imageUrl).into(view);
        }
    }

    private void setButton(Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityUpdateDetails();
            }
        });
    }

    private void openActivityCreateNewUser(){
        Intent intent = new Intent(this, CreateNewUserActivity.class);
        startActivity(intent);
    }

    private void openActivityUpdateDetails(){
        Intent intent = new Intent(this, EditUserDetailsActivity.class);
        startActivity(intent);
    }

    private void reloadLocalUserInfo(Bundle savedInstanceState){
        if (savedInstanceState != null){
            String username = savedInstanceState.getString(Shared.USERNAME_TAG);
            String prettyName = savedInstanceState.getString(Shared.PRETTY_NAME_TAG);
            String imageUrl = savedInstanceState.getString(Shared.IMAGE_URL_TAG);
            mUser = new User(username, prettyName, imageUrl);
        }
    }

    public void reloadServerUserInfo(){
        // request user info from worker
        WorkRequest request = new OneTimeWorkRequest
                .Builder(GetUserDataWorker.class)
                .setConstraints(Shared.CONSTRAINTS)
                .setInputData(mTokenInputData)
                .build();
        mWorkManager.enqueue(request);
        Shared.showLoadingUI();

        // add loading-image till user received
        mWorkManager.getWorkInfoByIdLiveData(request.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo info) {
                        if (info != null && info.getState().isFinished()) {
                            // initialize mUser
                            Data data = info.getOutputData();
                            mUser = GetUserDataWorker.getUserFromData(data);
                            Shared.removeLoadingUI();
                        }
                    }
                });
    }
}
