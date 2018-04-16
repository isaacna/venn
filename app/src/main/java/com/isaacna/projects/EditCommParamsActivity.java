package com.isaacna.projects;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

public class EditCommParamsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_comm_params);
        try {
            new GetCommParamsTask(this).execute().get();
            new GetCurrentParamsTask(this).execute();

        }
        catch (Exception e) {

        }
    }

    public void editParams(View view) {
        new EditParamsTask(this).execute();
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
        public EditCommParamsActivity activity;

        //this constructor is to pass in the communitiesactivity to access within onpostexecute
        public GetCommParamsTask(EditCommParamsActivity a) {
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

    class GetCurrentParamsTask extends AsyncTask<Integer, String, String> {

        private Exception exception;
        String response = "";
        StringBuilder result = new StringBuilder();
        public EditCommParamsActivity activity;

        //this constructor is to pass in the communitiesactivity to access within onpostexecute
        public GetCurrentParamsTask(EditCommParamsActivity a) {
            this.activity = a;
        }

        protected String doInBackground(Integer... urls) {

            try {
                int comm_id = activity.getIntent().getIntExtra("comm_id", 0); //will later set to session varible
                int user_id = activity.getIntent().getIntExtra("userID",0);
                URL url = new URL("http://ec2-34-215-159-222.us-west-2.compute.amazonaws.com/alt/getCurrentParams.php?comm_id=" + comm_id + "&user_id=" + user_id);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                System.out.println("comm id: " + comm_id);
                System.out.println("user id: " + user_id);
                System.out.println("getting the current params");
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
            try {
                //response is a json array
                JSONObject params = new JSONObject(result);


                String p1 = params.getString("p1");
                String p2 = params.getString("p2");
                String p3 = params.getString("p3");

                EditText f1 = findViewById(R.id.field1);//alsdif
                EditText f2 = findViewById(R.id.field2);
                EditText f3 = findViewById(R.id.field3);

                System.out.println("p1 - " + p1);
                System.out.println("p2 - " + p2);
                System.out.println("p3 - " + p3);


                f1.setText(p1);
                f2.setText(p2);
                f3.setText(p3);

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
        }


    }



    class EditParamsTask extends AsyncTask<Integer, String, String> {

        private Exception exception;
        String response = "";
        LinkedList<String> communitiesList = new LinkedList<>();
        StringBuilder result = new StringBuilder();
        public EditCommParamsActivity activity;

        //this constructor is to pass in the communitiesactivity to access within onpostexecute
        public EditParamsTask(EditCommParamsActivity a) {
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

                URL url = new URL("http://ec2-34-215-159-222.us-west-2.compute.amazonaws.com/alt/editCommParams.php?user_id=" + user_id
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


}
