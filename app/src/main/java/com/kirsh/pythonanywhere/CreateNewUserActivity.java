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

public class CreateNewUserActivity extends AppCompatActivity {

    private final static String ILLEGAL_USERNAME_MESSAGE = "Illegal username!\nValid username contains letters and digits.";

    private EditText mEditTextEnterUserName;
    private Button mButtonEnterUsername;
    private LifecycleOwner mLifecycleOwner;
    private ProgressBar mSpinKit;
//    private SuperApp mApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_user);
        mLifecycleOwner = this;
//        mApp = (SuperApp) getApplicationContext();
        initViews();
    }

    private void initViews(){
        // edit text view
        mEditTextEnterUserName = findViewById(R.id.edit_text_enter_username);
        // button view
        mButtonEnterUsername = findViewById(R.id.button_enter_username);
        setButton(mButtonEnterUsername);
        // spin-kit
        mSpinKit = findViewById(R.id.spin_kit);
        mSpinKit.setVisibility(View.INVISIBLE);
        Sprite doubleBounce = new DoubleBounce();
        mSpinKit.setIndeterminateDrawable(doubleBounce);
    }

    private void setButton(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameInput = mEditTextEnterUserName.getText().toString();
                // make sure input is a legal username
                if (!isLegalUsername(usernameInput)){
                    Snackbar.make(v, ILLEGAL_USERNAME_MESSAGE, Snackbar.LENGTH_LONG).show();
                    return;
                }
//                if (!Shared.givenInternetPermission(v.getContext())){
//                    return;
//                }
                // request token from server using input-username
                Data inputData = new Data
                        .Builder()
                        .putString(Shared.USERNAME_TAG, usernameInput)
                        .build();
                WorkRequest request = new OneTimeWorkRequest
                        .Builder(GetTokenWorker.class)
                        .setConstraints(Shared.CONSTRAINTS)
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
                                    // save token, return to main
                                    String token = info.getOutputData().getString(Shared.TOKEN_TAG);

//                                    mApp.initToken(token);
                                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString(Shared.TOKEN_TAG, token);
                                    editor.apply();
                                    handleSuccessfulCreateUser();
                                }
                            }
                        });
            }
        });
    }

    private boolean isLegalUsername(String input){
        return !input.isEmpty() && input.matches("[A-Za-z0-9]+");
    }

    private void handleSuccessfulCreateUser(){
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
