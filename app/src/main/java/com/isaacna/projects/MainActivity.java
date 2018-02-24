package com.isaacna.projects;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    Queue<Profile> swipes = getSwipes(); //global


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        profiles.add(new Profile("Jack", "Leshem", "Computer science legend. Melee God. Puff. TKE."));
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

}
