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
        setContentView(R.layout.activity_main);
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

    public boolean showNext(Queue<Profile> profiles){
        if(profiles.size() > 0){
            Profile toDisp = profiles.remove();

            //add in code to display the profiles here

           return true;
        }
        else{
            //display no profiles left
            return false;
        }
    }

    public Queue<Profile> getSwipes(){
        Queue<Profile> profiles = new LinkedList<Profile>(); //queue is an interface of linkedlist in java


//        AssetManager assetManager = getAssets();


//            File image = assetManager.open("leshem.png");


            // get input stream
//            InputStream ims = getAssets().open("leshem.jpg");
//
//
//            Bitmap b1;
//            b1 = BitmapFactory.decodeStream(ims);

            Bitmap b1 = getBitmapFromAssets("leshem.png");
//
            ImageView img = findViewById(R.id.otherPic);
            img.setImageBitmap(b1);
            profiles.add(new Profile("Jack", "Leshem", "Computer science legend. Melee God. Puff. TKE.", b1));
//


        profiles.add(new Profile("Rohan", "Pinto", "I'm essentially a walking meme."));
        profiles.add(new Profile("Nathan", "Yee", "Member of the thousand pound club :-)"));
        profiles.add(new Profile("Isaac", "Na", "Javascript frameworks are life!!"));
        return profiles;
    }



    public void answerYes() {
        //move to next profile
        showNext(swipes);

    }

    public void answerNo() {

    }

    private Bitmap getBitmapFromAssets(String fileName){
        /*
            AssetManager
                Provides access to an application's raw asset files.
        */

        /*
            public final AssetManager getAssets ()
                Retrieve underlying AssetManager storage for these resources.
        */
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
