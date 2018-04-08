package com.isaacna.projects;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import java.util.List;
import java.util.Map;

public class CommunitiesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communities); //set layout


        new RetrieveTask(this).execute();
        int count = 0;

        Button addComm = findViewById(R.id.addCommBtn);

        addComm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(CommunitiesActivity.this,AddCommunityActivity.class );
                startActivity(in);

                Button addComm = findViewById(R.id.addCommBtn);

                addComm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(CommunitiesActivity.this,AddCommunityActivity.class );
                        in.putExtras(getIntent());
                        startActivity(in);
                    }
                });
            }
        });

    }


    class RetrieveTask extends AsyncTask<String, String, String> {

        private Exception exception;
        String response = "";
        LinkedList<String> communitiesList = new LinkedList<>();
        StringBuilder result = new StringBuilder();
        public CommunitiesActivity activity;

        //this constructor is to pass in the communitiesactivity to access within onpostexecute
        public RetrieveTask(CommunitiesActivity a) {
            this.activity = a;
        }

        protected String doInBackground(String... urls) {

            try {
                int user_id = activity.getIntent().getIntExtra("userID", 1); //will later set to session varible
                URL url = new URL("http://ec2-34-215-159-222.us-west-2.compute.amazonaws.com/alt/getCommunities2.php?user_id=" + user_id);
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
            HashMap<Integer, String> commIdAndName = new HashMap<Integer,String>();

            LinearLayout ln = (LinearLayout) findViewById(R.id.communities);


            try {
                //response is a json array
                JSONArray communitiesJson = new JSONArray(result);
                System.out.println("Communities joined below");
                System.out.println(communitiesJson.toString());

                //go through json array and add the name of each community to the linkedlist
                for(int i=0; i < communitiesJson.length(); i++) {
                    JSONObject jsonobject = communitiesJson.getJSONObject(i);
                    String name = jsonobject.getString("name");
                    int comm_id = jsonobject.getInt("comm_id");
//                    l.add(name);
                    commIdAndName.put(comm_id,name);
                }

                //add to linear layout and bind communities to button
                for (int comm_id : commIdAndName.keySet()) {

                    //need to make these final to put in intent
                    final String comm_name = commIdAndName.get(comm_id);
                    final int comm_id_final = comm_id;
//                    final String s2= s; //make string final so it can be put in intent


                    Button community  = new Button(activity); //must pass in communitiesActivity from constructor
                    community.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    community.setText(comm_name); //add in name of community to button


                    community.setOnClickListener(new View.OnClickListener() { //bind function that sends to match page to button
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(CommunitiesActivity.this, MatchesActivity.class);
                            intent.putExtra("community", comm_name); //pass in community to remember for that match page
                            intent.putExtra("comm_id", comm_id_final);
                            intent.putExtras(activity.getIntent());
                            startActivity(intent);
                        }
                    });

                    ln.addView(community); //add the community
                }


            }
            catch (JSONException e){
                System.out.println(e);
            }
           // tv.setText(response);
        }


    }



}
