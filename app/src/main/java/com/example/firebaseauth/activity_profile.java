package com.example.firebaseauth;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;

import java.io.IOException;

public class activity_profile extends AppCompatActivity implements View.OnClickListener {

    //firebase auth object
    private FirebaseAuth firebaseAuth;

    //view objects
    private TextView textViewUserEmail;
    private Button buttonLogout;

    //defining a database reference
    private DatabaseReference databaseReference;

    //our new views
    private EditText editTextName, editTextAddress;
    private Button buttonSave;

    private UserInformation user1;

    private TextView textName;
    private TextView textAddress;
    private Button buttonTambah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();


        //if the user is not logged in
        //that means current user will return null
        if (firebaseAuth.getCurrentUser() == null) {
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginActivity.class));
        }

        //getting the database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        //getting the views from xml resource
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        editTextName = (EditText) findViewById(R.id.editTextName);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        textAddress = (TextView) findViewById(R.id.textAddress);
        textName = (TextView) findViewById(R.id.textName);
        buttonTambah = (Button) findViewById(R.id.buttonTambah);


        FirebaseUser user = firebaseAuth.getCurrentUser();

        //initializing views
        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);
        buttonLogout = (Button) findViewById(R.id.buttonLogout);

        //displaying logged in user name
        textViewUserEmail.setText("Welcome " + user.getEmail());

        user1 = new UserInformation();
        //adding listener to button
        buttonLogout.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
        buttonTambah.setOnClickListener(this);
    }


    private void saveUserInformation() {
        //Getting values from database
        String name = editTextName.getText().toString().trim();
        String add = editTextAddress.getText().toString().trim();

        //creating a userinformation object
        UserInformation userInformation = new UserInformation(name, add, user1.getAngka()+1);

        //getting the current logged in user
        FirebaseUser user = firebaseAuth.getCurrentUser();


        databaseReference.child(user.getUid()).setValue(userInformation);

        //displaying a success toast
        Toast.makeText(this, "Information Saved...", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        //if logout is pressed
        if (view == buttonLogout) {
            //logging out the user
            //firebaseAuth.signOut();
            //closing activity
            //finish();
            //starting login activity
            //startActivity(new Intent(this, LoginActivity.class));
            getUserInformation g = new getUserInformation();
            g.execute();
        }

        if(view == buttonSave){
            saveUserInformation();
        }

        if (view == buttonTambah){
            //String name = editTextName.getText().toString().trim();
            //String add = editTextAddress.getText().toString().trim();
            //creating a userinformation object
            getUserInformation g = new getUserInformation();
            g.execute();


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

            textName.setText(user1.getName());
            textAddress.setText(user1.getAddress());
            //user1.getAngka() = user1.getAngka() + 1;

            UserInformation userInformation = new UserInformation(user1.getName(), user1.getAddress(), user1.getAngka()+1);

            //getting the current logged in user
            FirebaseUser user = firebaseAuth.getCurrentUser();


            databaseReference.child(user.getUid()).setValue(userInformation);

            //displaying a success toast
            //Toast.makeText(this, "Information Bertambah...", Toast.LENGTH_LONG).show();

        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                user1.getUserInformation(firebaseAuth.getCurrentUser().getUid());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}