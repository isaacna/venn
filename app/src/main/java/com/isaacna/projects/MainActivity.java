package com.isaacna.projects;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {

//    int occurences;
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    Queue<Profile> swipes; //global
    Profile currentDisplayedProfile; //to keep track of displayed profile
//    boolean isMatch;
//    String matchName;
//    String matchCom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //if logged in
            //if swipes is empty
                //call getSwipes (query) - load data into swipes
                //showNext

        if (true) {
           // swipes = new Queue<Profile>();
            getSwipes();
            System.out.println("returned from get swipes - " + swipes.size());
            try {
//                while(!swipes.isEmpty()) {
//                    System.out.println(swipes.remove().getFirstName());
//                }
                for(Profile p : swipes) {
                    p.getFirstName();
                }

            }
            catch (Exception e) {
                System.out.println("fucked");
            }

            showNext(swipes);
        }


       // new RetrieveTask(this).execute();
    }

    public void viewProfile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtras(this.getIntent());
        startActivity(intent);
    }

    public void viewCommunities(View view) {
        Intent intent = new Intent(this, CommunitiesActivity.class);
        intent.putExtras(this.getIntent());
        startActivity(intent);
    }

    public void viewCreate(View view){
        Intent intent = new Intent(this, CreateActivity.class);
        intent.putExtras(this.getIntent());
        startActivity(intent);
    }

    public boolean showNext(Queue<Profile> profiles){

        if(profiles.size() > 0){
            final Profile toDisp = profiles.remove();

            //add in code to display the profiles here
            ImageView img = findViewById(R.id.otherPic);
            img.setImageBitmap(toDisp.getProfilePic());

            //set bio and name and community
            TextView otherName = findViewById(R.id.otherName);
            TextView otherBio = findViewById(R.id.otherBio);
//            TextView otherCommunity = findViewById(R.id.otherCommunity);

            String nameAndBio = toDisp.getFirstName() + " " + toDisp.getLastName() + ": " + toDisp.getWhichCommunity();
            otherName.setText(nameAndBio);
            otherBio.setText(toDisp.getBioInfo());




            //pass profile to expanded candidate screen
            RelativeLayout rl = (RelativeLayout)findViewById(R.id.mainLayout);
            rl.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, CandidateActivity.class);
                    intent.putExtras(MainActivity.this.getIntent());

                    //put swipes info to intent
                    intent.putExtra("candidate_name", toDisp.getFirstName() + " " + toDisp.getLastName());
//                    intent.putExtra("candidate_pic", toDisp.getProfilePic());
                    intent.putExtra("candidate_bio", toDisp.getBioInfo());
                    intent.putExtra("f1",toDisp.getF1());
                    intent.putExtra("f2",toDisp.getF2());
                    intent.putExtra("f3",toDisp.getF3());
                    intent.putExtra("p1",toDisp.getP1());
                    intent.putExtra("p2",toDisp.getP2());
                    intent.putExtra("p3",toDisp.getP3());

                    //put picture into intent
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    toDisp.getProfilePic().compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    intent.putExtra("candidate_pic",byteArray);


                    startActivity(intent);
                }
            });

            currentDisplayedProfile=toDisp;//keep track of the current displayed profile (since it is removed from queue)
           return true;
        }
        else{
            //display no profiles left
            ImageView img = findViewById(R.id.otherPic);

            //set bio and name and community
            TextView otherName = findViewById(R.id.otherName);
            TextView otherBio = findViewById(R.id.otherBio);

            img.setImageBitmap(null);
            otherName.setText("No more candidates");
            otherBio.setText("");

            return false;
        }
    }

   // public Queue<Profile> getSwipes(){
   public void getSwipes() {
        //Queue<Profile> profiles = new LinkedList<Profile>(); //queue is an interface of linkedlist in java

        System.out.println("getting the swipes");
        try {
           swipes =  new GetCandidatesTask(this).execute().get();

        }

        catch (Exception e) {

        }
       System.out.println("got the swipes");



    }

    public void answerYes(View view) {

//        new UpdateSwipeTask().execute(1,currentDisplayedProfile.getCommunityId(),currentDisplayedProfile.getUserId(),1,currentDisplayedProfile.getSwiperNum());
        if(currentDisplayedProfile.getAnswer()==1) { //candidate answered yes to you

            Intent intent = new Intent(this, MessagesActivity.class);
            intent.putExtra("other_id",currentDisplayedProfile.getUserId());
            intent.putExtra("other_name", currentDisplayedProfile.getFirstName() + " " + currentDisplayedProfile.getLastName());
            intent.putExtra("swipe_id", currentDisplayedProfile.getSwipeId());
            intent.putExtras(this.getIntent());
            startActivity(intent);
            showNext(swipes);
        }

        else { //candidate answered no or hasn't answered yet
            //update swipes
            showNext(swipes);
        }


    }

    public void answerNo(View view) {
        //update swipes
//        new UpdateSwipeTask().execute(1,currentDisplayedProfile.getCommunityId(),currentDisplayedProfile.getUserId(),0,currentDisplayedProfile.getSwiperNum());
        showNext(swipes);
    }


    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL("http://ec2-34-215-159-222.us-west-2.compute.amazonaws.com/images/" + src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            return null;
        }
    }


    //async task for getting swipes (called by getSwipes())
    class GetCandidatesTask extends AsyncTask<String, String, Queue<Profile>> {

        private Exception exception;
        String response = "";
        StringBuilder result = new StringBuilder();
        public MainActivity activity;

        //this constructor is to pass in the communitiesactivity to access within onpostexecute
        public GetCandidatesTask(MainActivity a) {
            this.activity = a;
        }

        protected Queue<Profile> doInBackground(String... urls) {

            try {
                int user_id = activity.getIntent().getIntExtra("userID",0); //will later set to session varible
                URL url = new URL("http://ec2-34-215-159-222.us-west-2.compute.amazonaws.com/alt/getAllCandidates.php?user_id=" + user_id);
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
            //return result.toString();
            Queue<Profile> swipesTemp = new LinkedList<Profile>();
            try {

                //response is a json array'
               // System.out.println("hitting the execute");
                JSONArray swipesJson = new JSONArray(result.toString()); //get
                System.out.println("success");
                System.out.println("JSON STRING: " + swipesJson.toString());

                //go through json array, create new profile, and add to swipes
                for (int i = 0; i < swipesJson.length(); i++) {
                    JSONObject jsonobject = swipesJson.getJSONObject(i);
                    String firstName = jsonobject.getString("first_name");
                    String lastName = jsonobject.getString("last_name");
                    String bio = jsonobject.getString("bio");
                    String community = jsonobject.getString("community");
                    int userId = jsonobject.getInt("user_id");
                    int commId = jsonobject.getInt("comm_id");
                    String picture = jsonobject.getString("picture");
                    int swiperNum = jsonobject.getInt("swiper_number");
                    int swipeId = jsonobject.getInt("swipe_id");

                    String f1 = jsonobject.getString("f1");
                    String f2 = jsonobject.getString("f2");
                    String f3 = jsonobject.getString("f3");
                    String p1 = jsonobject.getString("p1");
                    String p2 = jsonobject.getString("p2");
                    String p3 = jsonobject.getString("p3");

                    int answer = -1;
                    try {
                        if (jsonobject.get("candidate_ans") != null) {//check if answer is null
                            answer = jsonobject.getInt("candidate_ans");
                        }
                    }

                    catch (Exception e) {
                        System.out.println("answer was null");

                    }

                    Profile p = new Profile(firstName, lastName, bio, picture, community, commId, userId, answer,swiperNum, swipeId,f1,f2,f3,p1,p2,p3);
                    swipesTemp.add(p);
                    System.out.println("added the swipes " + p.getFirstName() + " " + swipesTemp.size());
                }


            }
            catch (JSONException e){
                System.out.println(e);
            }
            return swipesTemp;
        }

        @Override
        protected void onPostExecute(Queue<Profile> q) {





        }


    }


    class UpdateSwipeTask extends AsyncTask<Integer, String, String> {

        private Exception exception;
        String response = "";
        StringBuilder result = new StringBuilder();
//        public MainActivity activity;

        //this constructor is to pass in the communitiesactivity to access within onpostexecute
//        public UpdateSwipeTask(MainActivity a) {
//            this.activity = a;
//        }

        protected String doInBackground(Integer... params) {

            try {
                //int user_id = 1; //will later set to session variable
                int user_id = params[0];
                int comm_id = params[1];
                int candidate_id = params[2];
                int user_ans = params[3];
                int swiper_num = params[4];
                String u = "http://ec2-34-215-159-222.us-west-2.compute.amazonaws.com/alt/updateSwipe.php?user_id=" + user_id +
                        "&comm_id=" +comm_id + "&candidate_id="+ candidate_id + "&user_ans=" + user_ans + "&swiper_num=" + swiper_num;
                URL url = new URL(u);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
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

            return "";
        }

        @Override
        protected void onPostExecute(String s) {





        }


    }


}
