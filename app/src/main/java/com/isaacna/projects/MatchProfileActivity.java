package com.isaacna.projects;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MatchProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_profile);
    try {
        new DisplayMatchTask(this).execute().get();
    }
    catch (Exception e) {

    }
    }


    public Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL("http://ec2-34-215-159-222.us-west-2.compute.amazonaws.com/alt/images/" + src);
            System.out.println(url.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }



    class DisplayMatchTask extends AsyncTask<Integer, String, String> {

        private Exception exception;
        String response = "";
        StringBuilder result = new StringBuilder();
        public MatchProfileActivity activity;

        //this constructor is to pass in the communitiesactivity to access within onpostexecute
        public DisplayMatchTask(MatchProfileActivity a) {
            this.activity = a;
        }

        protected String doInBackground(Integer... urls) {

            try {
                int other_id = activity.getIntent().getIntExtra("other_id", 0); //will later set to session varible
                int swipe_id = activity.getIntent().getIntExtra("swipe_id",0);
                URL url = new URL("http://ec2-34-215-159-222.us-west-2.compute.amazonaws.com/alt/displayMatchProfile.php?other_id=" + other_id + "&swipe_id=" + swipe_id);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));


                while ((line = br.readLine()) != null) {
                    result.append(line);
                    response += result.toString();
                    System.out.println(response);

                }
                br.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            //response is a json array
            try {
                Intent intent = getIntent();
                JSONObject match = new JSONObject(result.toString());

                TextView name = findViewById(R.id.candidateName);
                ImageView pic = findViewById(R.id.candPic);
                TextView p1 = findViewById(R.id.p1);
                TextView p2 = findViewById(R.id.p2);
                TextView p3 = findViewById(R.id.p3);

                String f1 = match.getString("f1");
                String f2 = match.getString("f2");
                String f3 = match.getString("f3");
                String p1val = match.getString("p1");
                String p2val = match.getString("p2");
                String p3val = match.getString("p3");

                System.out.println("display happened");
                if (!(f1.equals("null") || f1.equals(""))) { //check if empty field
                    System.out.println("wassup");
                    System.out.println(p1val);
                    String val1 = f1 + ": " + p1val;
                    p1.setText(val1);
                    p1.setVisibility(View.VISIBLE);
                }
                if (!(f2.equals("null") || f2.equals(""))) { //check if empty field
                    System.out.println("wassup2");
                    System.out.println(p2val);
                    String val1 = f2 + ": " + p2val;
                    p2.setText(val1);
                    p2.setVisibility(View.VISIBLE);
                }
                if (!(f3.equals("null") || f3.equals(""))) { //check if empty field
                    System.out.println("wassup3");
                    System.out.println(p3val);
                    String val1 = f3 + ": " + p3val;
                    p3.setText(val1);
                    p3.setVisibility(View.VISIBLE);
                }

                name.setText(match.getString("first_name"));

                Bitmap bm = getBitmapFromURL(match.getString("picture"));
                pic.setImageBitmap(bm);
            }

            catch (Exception e) {

            }

            return result.toString();
        }

    }


}
