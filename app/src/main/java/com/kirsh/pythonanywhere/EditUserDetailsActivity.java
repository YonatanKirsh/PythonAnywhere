package com.kirsh.pythonanywhere;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.android.material.snackbar.Snackbar;
import com.kirsh.pythonanywhere.work.GetTokenWorker;
import com.kirsh.pythonanywhere.work.SetUserDataWorker;

public class EditUserDetailsActivity extends AppCompatActivity {

    private EditText mEditTextPrettyName;
    private EditText mEditTextImageUrl;
    private ProgressBar mSpinKit;

    private String mPrettyName;
    private String mImageUrl;
    private String mToken;
    private LifecycleOwner mLifecycleOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_details);
        Intent intentCreatedMe = getIntent();
        mPrettyName = intentCreatedMe.getStringExtra(Shared.PRETTY_NAME_TAG);
        mImageUrl = intentCreatedMe.getStringExtra(Shared.IMAGE_URL_TAG);

//        SuperApp app = (SuperApp) getApplicationContext();
//        mToken = app.getToken();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mToken = sp.getString(Shared.TOKEN_TAG, null);

        mLifecycleOwner = this;
        initViews();
    }

    private void initViews(){
        // pretty name edit text view
        mEditTextPrettyName = findViewById(R.id.edit_text_pretty_name);
        mEditTextPrettyName.setText(mPrettyName);
        // image url edit text view
        mEditTextImageUrl = findViewById(R.id.edit_text_image_url);
        mEditTextImageUrl.setText(mImageUrl);
        // button view
        Button button = findViewById(R.id.button_enter_username);
        setButton(button);
        // spin-kit
        mSpinKit = findViewById(R.id.spin_kit);
        mSpinKit.setVisibility(View.INVISIBLE);
        Sprite doubleBounce = new DoubleBounce();
        mSpinKit.setIndeterminateDrawable(doubleBounce);
    }

    private void setButton(Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prettyNameInput = mEditTextPrettyName.getText().toString();
                String imageUrlInput = mEditTextImageUrl.getText().toString();
//                if (!Shared.givenInternetPermission(v.getContext())){
//                    return;
//                }
                // request token from server using input-username
                Data inputData = new Data
                        .Builder()
                        .putString(Shared.TOKEN_TAG, mToken)
                        .putString(Shared.PRETTY_NAME_TAG, prettyNameInput)
                        .putString(Shared.IMAGE_URL_TAG, imageUrlInput)
                        .build();
                WorkRequest request = new OneTimeWorkRequest
                        .Builder(SetUserDataWorker.class)
//                        .setConstraints(Shared.CONSTRAINTS)
                        .setInputData(inputData)
                        .build();
                WorkManager manager = WorkManager.getInstance(getApplicationContext());
                manager.enqueue(request);
                mSpinKit.setVisibility(View.VISIBLE);
                // wait for response, save token to sp, goto main activity
                manager.getWorkInfoByIdLiveData(request.getId())
                        .observe(mLifecycleOwner, new Observer<WorkInfo>() {
                            @Override
                            public void onChanged(WorkInfo info) {
                                if (info != null && info.getState().isFinished()) {
                                    handleSuccessfulCreateUser();
                                }
                            }
                        });
            }
        });
    }

    private void handleSuccessfulCreateUser(){
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
