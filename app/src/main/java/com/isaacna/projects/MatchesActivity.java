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

public class MatchesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);
        Intent intent = getIntent();
//        intent.removeExtra("comm_id");

        TextView community = findViewById(R.id.matchCommunity);
        community.setText(intent.getStringExtra("community")); //display name of community the match page is for

        int comm_id = intent.getIntExtra("comm_id", -1); //return -1 if no comm_id passed

        new RetrieveMatchesTask(this).execute(getIntent().getIntExtra("userID", 0),comm_id); //replace the 1 with the session user id

    }

    public void chatPerson(View view) {
        Intent intent = new Intent(this, ChatActivity.class);
        String name = ((Button) view).getText().toString();
        intent.putExtra("matchName", name);
        intent.putExtras(getIntent());
        startActivity(intent);
    }

    public void editParams(View view) {
        Intent intent = new Intent(this, EditCommParamsActivity.class);
//        intent.putExtra("comm_id", getIntent().getIntExtra("comm_id",-1));
        intent.putExtras(getIntent());
        System.out.println("user id should be passed here");
        System.out.println(getIntent().getIntExtra("user_id", 0));
        startActivity(intent);
    }


    //leave community and go back to communities page
    public void leaveCommunity(View vew) {
        new LeaveCommunityTask(this).execute();

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtras(getIntent());
        intent.removeExtra("comm_id"); //remove to prevent arduiblo error
        intent.removeExtra("community"); //remove to prevent arduiblo error

        startActivity(intent);
    }



    class RetrieveMatchesTask extends AsyncTask<Integer, String, String> {

        private Exception exception;
        String response = "";
        StringBuilder result = new StringBuilder();
        public MatchesActivity activity;

        //this constructor is to pass in the communitiesactivity to access within onpostexecute
        public RetrieveMatchesTask(MatchesActivity a) {
            this.activity = a;
        }

        protected String doInBackground(Integer... params) {

            try {
                int user_id = params[0]; //get my user_id from params
                int comm_id = params[1]; //get comm_id from params

                URL url = new URL("http://ec2-34-215-159-222.us-west-2.compute.amazonaws.com/alt/displayMatches.php?user_id=" + user_id + "&comm_id=" + comm_id);
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
            HashMap<Integer, String> matchedIdAndName = new HashMap<Integer,String>(); //hashmap containing the match's id and name
            HashMap<Integer, Integer> matchedUserIdAndSwipeId = new HashMap<Integer,Integer>();

            LinearLayout ln = (LinearLayout) findViewById(R.id.matchesLayout);


            try {
                //response is a json array
                JSONArray matchesJson = new JSONArray(result);
                System.out.println(matchesJson.toString());

                //go through json array and add the id and name of each match to the map
                for(int i=0; i < matchesJson.length(); i++) {
                    JSONObject jsonobject = matchesJson.getJSONObject(i);
                    String fullName = (jsonobject.getString("first_name") + " " + jsonobject.getString("last_name")); //concatenate names

                    int user_id = jsonobject.getInt("user_id");
                    int swipe_id = jsonobject.getInt("swipe_id"); //get corresponding swipe_id

                    matchedIdAndName.put(user_id,fullName); // map user id and full name
                    matchedUserIdAndSwipeId.put(user_id, swipe_id); //map user id and swipe id
                }

                //add to linear layout and bind communities to button
                for (int match_id : matchedIdAndName.keySet()) {

                    //need to make these final to put in intent
                    final String match_name = matchedIdAndName.get(match_id);
                    final int match_id_final = match_id;
                    final int swipe_id = matchedUserIdAndSwipeId.get(match_id);

                    System.out.println(match_name);
//                    final String s2= s; //make string final so it can be put in intent


                    Button match  = new Button(activity); //must pass in communitiesActivity from constructor
                    match.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    match.setText(match_name); //add in name of community to button


                    match.setOnClickListener(new View.OnClickListener() { //bind function that sends to match page to button
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MatchesActivity.this, MessagesActivity.class);
                            intent.putExtra("other_name", match_name); //pass in community to remember for that match page
                            intent.putExtra("other_id",match_id_final); //pass the person you are messaging's id
                            intent.putExtra("swipe_id", swipe_id); //pass the swipe id
                            intent.putExtras(getIntent());
                            startActivity(intent);
                        }
                    });

                    ln.addView(match); //add the community
                }


            }
            catch (JSONException e){
                System.out.println(e);
            }
            // tv.setText(response);
        }



    }


    class LeaveCommunityTask extends AsyncTask<Integer, String, String> {

        private Exception exception;
        String response = "";
        StringBuilder result = new StringBuilder();
        public MatchesActivity activity;

        //this constructor is to pass in the communitiesactivity to access within onpostexecute
        public LeaveCommunityTask(MatchesActivity a) {
            this.activity = a;
        }

        protected String doInBackground(Integer... params) {

            try {
                int user_id = getIntent().getIntExtra("userID",0); //get my user_id from intent
                int comm_id = getIntent().getIntExtra("comm_id",-1); //get comm_id from intent

                URL url = new URL("http://ec2-34-215-159-222.us-west-2.compute.amazonaws.com/alt/leaveCommunity.php?user_id=" + user_id + "&comm_id=" + comm_id);
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


    }


    }
