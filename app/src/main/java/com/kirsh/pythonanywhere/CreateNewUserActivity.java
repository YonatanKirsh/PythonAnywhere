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
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.kirsh.pythonanywhere.work.GetTokenWorker;
import com.kirsh.pythonanywhere.work.GetUserDataWorker;

public class CreateNewUserActivity extends AppCompatActivity {

    private final static String ILLEGAL_USERNAME_MESSAGE = "Illegal username!\nValid username contains letters and digits.";

    private TextView mTextViewEnterUserName;
    private EditText mEditTextEnterUserName;
    private Button mButtonEnterUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_user);
        initViews();
    }

    private void handleSuccessfullCreateUser(){
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void initViews(){
        // page instructions view
        mTextViewEnterUserName = findViewById(R.id.text_view_username);
        // edit text view
        mEditTextEnterUserName = findViewById(R.id.edit_text_enter_username);
        // button view
        mButtonEnterUsername = findViewById(R.id.button_enter_username);
        setButton(mButtonEnterUsername);
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
                // request token, return username
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
                Shared.showLoadingUI();

                manager.getWorkInfoByIdLiveData(request.getId())
                        .observe((LifecycleOwner) getApplicationContext(), new Observer<WorkInfo>() {
                            @Override
                            public void onChanged(WorkInfo info) {
                                if (info != null && info.getState().isFinished()) {
                                    // initialize token, return to main
                                    String token = info.getOutputData().getString(Shared.TOKEN_TAG);
                                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString(Shared.TOKEN_TAG, token);
                                    editor.apply();
                                }
                            }
                        });


            }
        });
    }

    private boolean isLegalUsername(String input){
        return !input.isEmpty() && input.matches("[^A-Za-z0-9]+");
    }
}
