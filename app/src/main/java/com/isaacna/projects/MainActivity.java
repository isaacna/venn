package com.isaacna.projects;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    Queue<Profile> swipes; //global


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        if(getIntent().hasExtra("happened")){
            String c = getIntent().getStringExtra("happened");
            if (c.equals("y")){
                setContentView(R.layout.activity_main);
            }
        }

        swipes=getSwipes();

//        showNext(swipes);
    }

    public void viewProfile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void viewCommunities(View view) {
        Intent intent = new Intent(this, CommunitiesActivity.class);
        startActivity(intent);
    }

    public void viewCreate(View view){
        Intent intent = new Intent(this, CreateActivity.class);
        startActivity(intent);
    }

    public boolean showNext(Queue<Profile> profiles){
        if(profiles.size() > 0){
            final Profile toDisp = profiles.remove();

            //add in code to display the profiles here
            ImageView img = findViewById(R.id.otherPic);
            img.setImageBitmap(toDisp.getProfilePic());

            //set bio and name
            TextView otherName = findViewById(R.id.otherName);
            TextView otherBio = findViewById(R.id.otherBio);

            otherName.setText(toDisp.getFirstName() + " " + toDisp.getLastName());
            otherBio.setText(toDisp.getBioInfo());

            //pass profile to expanded candidate screen
            RelativeLayout rl = (RelativeLayout)findViewById(R.id.mainLayout);
            rl.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, CandidateActivity.class);
                    intent.putExtra("candidate", toDisp.getFirstName() + " " + toDisp.getLastName());
                    startActivity(intent);
                }
            });

           return true;
        }
        else{
            //display no profiles left
            return false;
        }
    }

    public Queue<Profile> getSwipes(){
        Queue<Profile> profiles = new LinkedList<Profile>(); //queue is an interface of linkedlist in java




        Bitmap lesh = getBitmapFromAssets("leshem.png");
        Bitmap nathan = getBitmapFromAssets("nathan.png");
        Bitmap tyler = getBitmapFromAssets("senator.png");
        Bitmap rohan = getBitmapFromAssets("rohan.png");

        profiles.add(new Profile("Jack", "Leshem", "Computer science legend. Melee God. Puff. TKE.", lesh));
        profiles.add(new Profile("Rohan", "Pinto", "I'm essentially a walking meme.", rohan));
        profiles.add(new Profile("Nathan", "Yee", "Member of the thousand pound club :-)", nathan));
        profiles.add(new Profile("Tyler", "Tran", "Life, Liberty, and the pursuit of schemes", tyler));
        return profiles;
    }



    public void answerYes(View view) {
        //move to next profile
        showNext(swipes);

    }

    public void answerNo() {

    }

    private Bitmap getBitmapFromAssets(String fileName){

        AssetManager am = getAssets();
        InputStream is = null;
        try{

            is = am.open(fileName);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            return bitmap;
        }catch(IOException e){
            e.printStackTrace();
        }

        return null;
    }

}
