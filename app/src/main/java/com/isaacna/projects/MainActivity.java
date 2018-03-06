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

    int occurences;
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    Queue<Profile> swipes; //global
    boolean isMatch;
    String matchName;
    String matchCom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        isMatch = false;

        if(getIntent().hasExtra("happened")){
            String c = getIntent().getStringExtra("happened");
            if (c.equals("y")){
                if(getIntent().hasExtra("place")){
                    setContentView(R.layout.activity_main);
                    occurences = getIntent().getIntExtra("place", 0);
                    swipes = getSwipes();
                    for(int i = 0; i < occurences; ++i){
                        showNext(swipes);
                    }

                }
                else {
                    System.out.println("hello" + "\n fail \n\n\n fail \n\n" + occurences);
                    setContentView(R.layout.activity_main);
                    swipes = getSwipes();
                    showNext(swipes);
                    occurences = 1;
                }
            }
        }
    }

    public void viewProfile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtras(getIntent());
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

            //set bio and name and community
            TextView otherName = findViewById(R.id.otherName);
            TextView otherBio = findViewById(R.id.otherBio);
//            TextView otherCommunity = findViewById(R.id.otherCommunity);

            String nameAndBio = toDisp.getFirstName() + " " + toDisp.getLastName() + ": " + toDisp.getWhichCommunity();
            otherName.setText(nameAndBio);
            otherBio.setText(toDisp.getBioInfo());
//            otherCommunity.setText(toDisp.getWhichCommunity());

            if(toDisp.getFirstName().equals("Nathan")){
                isMatch = true;
                matchName = "Nathan";
                matchCom = "Workouts";
            }
            else if(toDisp.getFirstName().equals("Rohan")){
                isMatch = true;
                matchName = "Rohan";
                matchCom = "Hockey";
            }
            else if(toDisp.getFirstName().equals("Tyler")){
                isMatch = true;
                matchName = "Tyler";
                matchCom = "Hockey";
            }
            else if(toDisp.getFirstName().equals("Isaac")){
                isMatch = true;
                matchName = "Isaac";
                matchCom = "Workouts";
            }
            else{
                isMatch = false;
            }


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

        Bitmap nathan = getBitmapFromAssets("nathan.png");
        Bitmap tyler = getBitmapFromAssets("senator.png");
        Bitmap rohan = getBitmapFromAssets("rohan.png");
        Bitmap chris = getBitmapFromAssets("chris.png");
        Bitmap isaac = getBitmapFromAssets("isaac.png");
        Bitmap jaryd = getBitmapFromAssets("jaryd.png");
        Bitmap chauncey = getBitmapFromAssets("chauncey.png");
        Bitmap john = getBitmapFromAssets("john.png");
        Bitmap stock = getBitmapFromAssets("stock.png");

        profiles.add(new Profile("Rohan", "Pinto", "I'm essentially a walking meme.", rohan,"Hockey"));
        profiles.add(new Profile("Nathan", "Yee", "Member of the thousand pound club :-)", nathan, "Workouts"));
        profiles.add(new Profile("Tyler", "Tran", "Life, Liberty, and the pursuit of schemes", tyler, "Hockey"));
        profiles.add(new Profile("Chris", "Chow", "Welcome to the chow down.", chris,"Workouts"));
        profiles.add(new Profile("John", "Newton", "I'm good at poems", john, "Workouts"));
        profiles.add(new Profile("Chauncey", "Hill", "Falco is 3rd on the tier list", chauncey, "Hockey"));
        profiles.add(new Profile("Isaac", "Na", "Falco is 4th on the tier list", isaac,"Workouts"));
        profiles.add(new Profile("Jaryd", "Huffman", "Semper Fi", jaryd, "Workouts"));
        profiles.add(new Profile("We're out of people )", "", "", stock, ""));

        return profiles;
    }

    public void answerYes(View view) {
        if(isMatch){
            ++occurences;
            Intent intent = new Intent(this, oneMatchActivity.class);
            intent.putExtras(getIntent());
            if(intent.hasExtra("name") && intent.hasExtra("community")){
                intent.removeExtra("name");
                intent.removeExtra("community");
            }
            if(intent.hasExtra("place")){
                intent.removeExtra("place");
            }
            intent.putExtra("name", matchName);
            intent.putExtra("community", matchCom);
            intent.putExtra("place",occurences);

            startActivity(intent);
        }
       else{
            ++occurences;
            showNext(swipes);
        }
    }

    public void answerNo(View view) {
        ++occurences;
        showNext(swipes);
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
