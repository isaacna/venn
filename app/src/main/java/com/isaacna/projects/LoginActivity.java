package com.isaacna.projects;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;

public class LoginActivity extends AppCompatActivity {

    EditText email_form, pw_form;
    Button loginB;
    Intent in;
    ProgressDialog progressDialog;


    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Email = "emailKey";
    public static final String sessionUserId = "sessionUserId";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_form= findViewById(R.id.email);
        pw_form= findViewById(R.id.password);
        loginB = findViewById(R.id.loginBtn);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email  = email_form.getText().toString();
                String password  = pw_form.getText().toString();
                SharedPreferences.Editor editor = sharedpreferences.edit();

                try {
                    Profile p = new LoginTask(LoginActivity.this).execute(email, password).get();
                    editor.putInt(sessionUserId,p.getUserId());
                    editor.commit();
                    in = new Intent(LoginActivity.this,MainActivity.class);
                    in.putExtra("userID", p.getUserId());
                    in.putExtra("firstName", p.getFirstName());
                    in.putExtra("lastName", p.getLastName());
                    in.putExtra("bio", p.getBioInfo());
                    in.putExtra("source", p.getProSource());
                    in.putExtra("firstTime", false);
                    startActivity(in);
                }
                catch (Exception e) {
                    System.out.println("failed login");
                }





            }
        });
    }


    //async task for getting swipes (called by getSwipes())
    class LoginTask extends AsyncTask<String, String, Profile> {

        private Exception exception;
        String response = "";
        StringBuilder result = new StringBuilder();
        public LoginActivity activity;
        Profile p;

        //this constructor is to pass in the communitiesactivity to access within onpostexecute
        public LoginTask(LoginActivity a) {
            this.activity = a;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(LoginActivity.this,"Logging in","Please Wait",false,false);

        }


        protected Profile doInBackground(String... params) {

            try {
                String email = params[0];
                String pw_hash = params[1];
                URL url = new URL("http://ec2-34-215-159-222.us-west-2.compute.amazonaws.com/alt/login.php?email=" + email + "&pw_hash=" + pw_hash);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                //System.out.println(urls);
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));


                while ((line = br.readLine()) != null) {
                    result.append(line);
                    response += result.toString();
                    System.out.println(response);
                    //communitiesList.add(result.toString());

                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //return result.toString();
//            Queue<Profile> swipesTemp = new LinkedList<Profile>();
            try {

                //response is a json array'
                // System.out.println("hitting the execute");


                JSONObject sessionUser = new JSONObject(result.toString()); //get
                System.out.println("success");
//                System.out.println("JSON STRING: " + swipesJson.toString());
                String firstName = sessionUser.getString("first_name");
                String lastName = sessionUser.getString("last_name");
                String bio = sessionUser.getString("bio");
                String picture = sessionUser.getString("picture");
                int userId = sessionUser.getInt("user_id");


                p = new Profile(firstName, lastName, bio, picture, userId); //create new session profile




            } catch (JSONException e) {
                System.out.println(e);
                System.out.println("no ppl");
            }
            return p;
        }

        protected void onPostExecute() {
            progressDialog.dismiss(); 
        }

    }
}
