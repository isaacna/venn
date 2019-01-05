package com.isaacna.projects;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

public class CreateAccountActivityOriginal extends AppCompatActivity {

    public static final int FIRST_NAME = 0;
    public static final int LAST_NAME = 1;
    public static final int EMAIL = 2;
    public static final int BIO = 3;
    public static final int PASSWORD = 4;
    public static final String URL = "http://ec2-34-215-159-222.us-west-2.compute.amazonaws.com/alt/";
    public static final String SCRIPT_NAME = "createAccount.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_original);
    }

    private void alertProblem(String message){
        new AlertDialog.Builder(this).setTitle("Bad input").setMessage(message).setNeutralButton("Close", null).show();
    }

    public void addButtonListener(View view){

        EditText editText = findViewById(R.id.fname);
        String firstName = editText.getText().toString();
        editText = findViewById(R.id.lname);
        String lastName = editText.getText().toString();
        editText = findViewById(R.id.email);
        String email = editText.getText().toString();
        editText = findViewById(R.id.bio);
        String bio = editText.getText().toString();
        editText = findViewById(R.id.pass1);
        String pass1 = editText.getText().toString();
        editText = findViewById(R.id.pass2);
        String pass2 = editText.getText().toString();

        if(firstName == null || lastName == null || email == null
                || bio == null || pass1 == null || pass2 == null){
            alertProblem("Please fill out all fields");
        }
        else {
            if (!(firstName.equals(""))) {
                if (!(lastName.equals(""))) {
                    if (!(email.equals(""))) {
                        if (!(bio.equals(""))) {
                            if (pass1.equals(pass2)) {
                                if (pass1.equals("") || pass2.equals("")) {
                                    alertProblem("You must enter and confirm your password");
                                } else {
                                    String[] inputs = {firstName, lastName, email, bio, pass1};
                                    new CreateAccountTask(this).execute(inputs);
                                }
                            } else {
                                alertProblem("passwords do not match");
                            }
                        } else {
                            alertProblem("");
                        }
                    } else {
                        alertProblem("email is missing");
                    }
                } else {
                    alertProblem("last name is missing");
                }
            } else {
                alertProblem("first name is missing");
            }
        }

    }

    class CreateAccountTask extends AsyncTask<String, String, String> {

        private Exception exception;
        String response = "";
        StringBuilder result = new StringBuilder();
        public CreateAccountActivityOriginal activity;
        Intent in;

        public CreateAccountTask(CreateAccountActivityOriginal a){
            activity = a;
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                String firstName = strings[FIRST_NAME];
                String lastName = strings[LAST_NAME];
                String email = strings[EMAIL];
                String bio = strings[BIO];
                String pass = strings[PASSWORD];

                String functionalURL = URL + SCRIPT_NAME + "?"
                        + "firstName=" + firstName
                        + "&lastName=" + lastName
                        + "&email=" + email
                        + "&bio=" + bio
                        + "&pass=" + pass;

                URL url = new URL(functionalURL);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                //System.out.println(urls);
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                while ((line = br.readLine()) != null) {
                    result.append(line);
                    response += result.toString();
                    System.out.println(response);

                }
                int id = Integer.parseInt(response);
                System.out.println(id);
                in = new Intent(activity, CreateAccountActivity.class);
                in.putExtra("userID", id);
                in.putExtra("firstName", strings[FIRST_NAME]);
                in.putExtra("lastName", strings[LAST_NAME]);
                in.putExtra("bio", strings[BIO]);
               // in.putExtra("source", p.getProSource());

                br.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }
        @Override
        protected void onPostExecute(String result) {
            startActivity(in);
        }
    }
}
