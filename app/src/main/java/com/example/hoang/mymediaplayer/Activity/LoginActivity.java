package com.example.hoang.mymediaplayer.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hoang.mymediaplayer.R;
import com.example.hoang.mymediaplayer.Utilities.PlayAudioService;
import com.example.hoang.mymediaplayer.Utilities.RegisterUserClass;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, PlayAudioService.CallBacks{

    private EditText editName;
    private EditText editPassword;
    private TextView linkToRegister;

    private Button btnLogin;
    private static final int RC_SIGNIN = 0;
    private static final String TAG = "MainActivity";

    private static final String LOGIN_URL = "http://musicservice.hol.es/musicservice/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getControls();
    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin){
            loginUser();
        }else if (v == linkToRegister) {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
    }

    public void getControls(){
        editName = (EditText) findViewById(R.id.username);
        editPassword = (EditText) findViewById(R.id.password);

        editName.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editName.setImeActionLabel("Done", KeyEvent.KEYCODE_ENTER);
        editPassword.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editPassword.setImeActionLabel("Done", KeyEvent.KEYCODE_ENTER);

        linkToRegister = (TextView) findViewById(R.id.linkSignup);

        btnLogin = (Button) findViewById(R.id.buttonLogin);
        linkToRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }


    public void loginUser(){
        String name = editName.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        login(name, password);
    }

    public void login(final String username, String password){
        class LoginUser extends AsyncTask<String, Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(LoginActivity.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Authenticating...");
                progressDialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                if(s.equalsIgnoreCase("success")){
                    Intent intent = new Intent(LoginActivity.this,ListSongsActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this,s,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("username",params[0]);
                data.put("password",params[1]);

                RegisterUserClass ruc = new RegisterUserClass();

                String result = ruc.sendPostRequest(LOGIN_URL,data);

                return result;
            }
        }
        LoginUser loginUser = new LoginUser();
        loginUser.execute(username, password);
    }


    @Override
    public void updateInterface(MediaPlayer mp) {

    }

    @Override
    public void changePlayButtonIcon(boolean isPlaying) {

    }
}
