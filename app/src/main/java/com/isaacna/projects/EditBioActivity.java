package com.isaacna.projects;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class EditBioActivity extends AppCompatActivity {

    public static final String URL = "http://ec2-34-215-159-222.us-west-2.compute.amazonaws.com/alt/";
    public static final String SCRIPT_NAME = "editBio.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bio);

        String bio = getIntent().getStringExtra("bio");

        EditText text = findViewById(R.id.editBio);
        text.setText(bio);
    }

    public void doSendNewBio(View view) {
        EditText text = findViewById(R.id.editBio);
        String newBio =  text.getText().toString();
        String id = Integer.toString(getIntent().getIntExtra("userID", 0));
        String[] inputs = {id, newBio};
        new EditBioActivity.SendNewBioTask(this).execute(inputs);
    }

    class SendNewBioTask extends AsyncTask<String, String, String> {

        StringBuilder result;
        String response = "";
        EditBioActivity activity;
        Intent in;

        SendNewBioTask(EditBioActivity a) {
            activity = a;
            result = new StringBuilder();
        }


        protected String doInBackground(String... strings) {

            try {
                String id = strings[0];
                String bio = strings[1];

                String functionalURL = URL + SCRIPT_NAME + "?"
                        + "user_id=" + id
                        + "&bio=" + bio;

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
                //int idd = Integer.parseInt(id);
                //System.out.println(idd);
                in = new Intent(activity, ProfileActivity.class);
                //in.putExtra("userID", idd);
                in.putExtras(activity.getIntent());
                in.putExtra("bio", bio);
                // in.putExtra("source", p.getProSource());

                br.close();
            } catch (IOException e) {
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
