package com.isaacna.projects;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;

public class JoinCommActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_comm);

        new GetCommParamsTask(this).execute();

    }


    public void joinCommunity(View view) {
        new JoinCommTask(this).execute();
        Intent in = new Intent(this,MainActivity.class);
        in.putExtras(getIntent().getExtras());
        in.removeExtra("comm_id");//remove to prevent arduiblo error
        in.removeExtra("community");//remove to prevent arduiblo error
        startActivity(in);
    }

    //task for putting params into hints
    class GetCommParamsTask extends AsyncTask<Integer, String, String> {

        private Exception exception;
        String response = "";
        LinkedList<String> communitiesList = new LinkedList<>();
        StringBuilder result = new StringBuilder();
        public JoinCommActivity activity;

        //this constructor is to pass in the communitiesactivity to access within onpostexecute
        public GetCommParamsTask(JoinCommActivity a) {
            this.activity = a;
        }

        protected String doInBackground(Integer... urls) {

            try {
                int comm_id = activity.getIntent().getIntExtra("comm_id", 0); //will later set to session varible
                URL url = new URL("http://ec2-34-215-159-222.us-west-2.compute.amazonaws.com/alt/getCommParams.php?comm_id=" + comm_id);
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
                br.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            //do stuff

            LinkedList<String> l = new LinkedList<String>();
//            LinkedList<Map.Entry<Integer,String>> = new LinkedList<>();
//            LinearLayout ln = (LinearLayout) findViewById(R.id.newCommunities);


            try {
                //response is a json array
//                JSONArray communitiesJson = new JSONArray(result);
                JSONObject params = new JSONObject(result);


                String p1 = params.getString("p1");
                String p2 = params.getString("p2");
                String p3 = params.getString("p3");

                EditText f1 = findViewById(R.id.field1);//alsdif 
                EditText f2 = findViewById(R.id.field2);
                EditText f3 = findViewById(R.id.field3);

                TextView tv = findViewById(R.id.joinCommName);
                tv.setText(params.getString("comm_name"));

                TextView desc = findViewById(R.id.joinCommDesc);
                desc.setText(getIntent().getStringExtra("comm_desc"));

                f1.setHint(p1);
                f2.setHint(p2);
                f3.setHint(p3);

                if(!p1.equals("") && !p1.equals("null")) {
                    f1.setVisibility(View.VISIBLE);
                }

                if(!p2.equals("") && !p2.equals("null")) {
                    f2.setVisibility(View.VISIBLE);
                }
                if(!p3.equals("") && !p3.equals("null")) {
                    f3.setVisibility(View.VISIBLE);
                }
            }
            catch (JSONException e){
                System.out.println(e);
            }
            // tv.setText(response);
        }


    }

    class JoinCommTask extends AsyncTask<Integer, String, String> {

        private Exception exception;
        String response = "";
        LinkedList<String> communitiesList = new LinkedList<>();
        StringBuilder result = new StringBuilder();
        public JoinCommActivity activity;

        //this constructor is to pass in the communitiesactivity to access within onpostexecute
        public JoinCommTask(JoinCommActivity a) {
            this.activity = a;
        }

        protected String doInBackground(Integer... urls) {

            try {
                int user_id = getIntent().getIntExtra("userID",0);
                int comm_id = getIntent().getIntExtra("comm_id",0);
                EditText field1 = findViewById(R.id.field1);
                EditText field2 = findViewById(R.id.field2);
                EditText field3 = findViewById(R.id.field3);
                String p1 = field1.getText().toString();
                String p2 = field2.getText().toString();
                String p3 = field3.getText().toString();

                URL url = new URL("http://ec2-34-215-159-222.us-west-2.compute.amazonaws.com/alt/joinCommunity.php?user_id=" + user_id
                        + "&comm_id=" + comm_id + "&p1=" + p1 + "&p2=" + p2 + "&p3=" + p3);
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
                br.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            //do stuff

        }
    }

    //



}
