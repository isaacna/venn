package com.isaacna.projects;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Profile p = new Profile("Jack", "Leshem", "I watched a video on it");
        //Intent intent = new Intent(this, p);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        TextView name = findViewById(R.id.name); //R.id.x refers to the textview with ID x
        name.setText(p.getFirstName() + " " + p.getLastName());
        TextView bio = findViewById(R.id.bio);
        bio.setText(p.getBioInfo());

    }
}
