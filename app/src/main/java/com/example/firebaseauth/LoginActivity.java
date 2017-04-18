package com.example.firebaseauth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    //defining views
    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignup;
    private TextView textName;
    private TextView textAddress;
    private UserInformation user;

    //firebase auth object
    private FirebaseAuth firebaseAuth;

    //progress dialog
    private ProgressDialog progressDialog;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //getting firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();


        //if the objects getcurrentuser method is not null
        //means user is already logged in
        /*if (firebaseAuth.getCurrentUser() != null) {
            //close this activity
            finish();
            //opening profile activity
            startActivity(new Intent(getApplicationContext(), activity_profile.class));
        }*/

        //initializing views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonSignIn = (Button) findViewById(R.id.buttonSignin);
        textViewSignup = (TextView) findViewById(R.id.textViewSignUp);
        textAddress = (TextView) findViewById(R.id.textAddress);
        textName = (TextView) findViewById(R.id.textName);

        progressDialog = new ProgressDialog(this);
        user = new UserInformation();

        //attaching click listener
        buttonSignIn.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);
    }

    //method for user login
    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();


        //checking if email and passwords are empty
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        //logging in the user
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        //if the task is successfull
                        if (task.isSuccessful()) {
                            //start the profile activity
                            finish();
                            startActivity(new Intent(getApplicationContext(), activity_profile.class));
                        }
                    }
                });



    }

    @Override
    public void onClick(View view) {
        if (view == buttonSignIn) {
            //userLogin();
            getUserInformation g = new getUserInformation();
            g.execute();
        }

        if (view == textViewSignup) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }


    public class getUserInformation extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            textName.setText(user.getName());
            textAddress.setText(user.getAddress());

        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                user.getUserInformation(firebaseAuth.getCurrentUser().getUid());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}