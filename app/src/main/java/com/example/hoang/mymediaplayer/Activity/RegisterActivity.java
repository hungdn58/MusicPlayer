package com.example.hoang.mymediaplayer.Activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hoang.mymediaplayer.R;
import com.example.hoang.mymediaplayer.Utilities.RegisterUserClass;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextName;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextEmail;

    private Button buttonRegister;

    private static final String REGISTER_URL = "http://musicservice.hol.es/musicservice/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getControls();
    }

    public void getControls(){
        editTextName = (EditText) findViewById(R.id.name);
        editTextUsername = (EditText) findViewById(R.id.username);
        editTextPassword = (EditText) findViewById(R.id.password);
        editTextEmail = (EditText) findViewById(R.id.email);

        editTextName.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editTextName.setImeActionLabel("Done", KeyEvent.KEYCODE_ENTER);
        editTextPassword.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editTextPassword.setImeActionLabel("Done", KeyEvent.KEYCODE_ENTER);
        editTextUsername.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editTextUsername.setImeActionLabel("Done", KeyEvent.KEYCODE_ENTER);
        editTextEmail.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editTextEmail.setImeActionLabel("Done", KeyEvent.KEYCODE_ENTER);


        buttonRegister = (Button) findViewById(R.id.btnRegister);
        buttonRegister.setOnClickListener(this);
    }

    public void registerUser(){

        if (!validate()){
            Toast.makeText(getBaseContext(), "Signup failed", Toast.LENGTH_LONG).show();
        }else {

            buttonRegister.setEnabled(false);
            String name = editTextName.getText().toString().trim().toLowerCase();
            String username = editTextUsername.getText().toString().trim().toLowerCase();
            String password = editTextPassword.getText().toString().trim().toLowerCase();
            String email = editTextEmail.getText().toString().trim().toLowerCase();

            register(name, username, password, email);
        }
    }

    public boolean validate() {
        boolean valid = true;

        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String username = editTextUsername.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            editTextName.setError("at least 3 characters");
            valid = false;
        } else {
            editTextName.setError(null);
        }

        if (username.isEmpty() || name.length() < 3) {
            editTextUsername.setError("at least 3 characters");
            valid = false;
        } else {
            editTextUsername.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("enter a valid email address");
            valid = false;
        } else {
            editTextEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            editTextPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            editTextPassword.setError(null);
        }

        return valid;
    }

    @Override
    public void onClick(View v) {
        if(v == buttonRegister){
            registerUser();
        }
    }

    public void register(final String name, final String username, final String password, final String email){
        class RegisterUser extends AsyncTask<String , Void, String> {

            ProgressDialog progressDialog;

            @Override
            protected String doInBackground(String... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("name",params[0]);
                data.put("username",params[1]);
                data.put("password",params[2]);
                data.put("email",params[3]);

                RegisterUserClass registerUser = new RegisterUserClass();
                String result = registerUser.sendPostRequest(REGISTER_URL, data);
                return result;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(RegisterActivity.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Registering ...");
                progressDialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, s, Toast.LENGTH_LONG).show();
                buttonRegister.setEnabled(true);
                finish();
            }
        }

        RegisterUser registerUser = new RegisterUser();
        registerUser.execute(name, username, password, email);
    }

}
