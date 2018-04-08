package com.isaacna.projects;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


public class ProfileActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        new AsyncTaskLoadImage(this).execute();
//        Intent intent = new Intent(this, p);



    }

    public class AsyncTaskLoadImage  extends AsyncTask<String, String, Profile> {
        private final static String TAG = "AsyncTaskLoadImage";
        private ProfileActivity activity;

        public AsyncTaskLoadImage(ProfileActivity a) {
            this.activity = a;
        }

        @Override
        protected Profile doInBackground(String... params) {
            Bitmap bitmap = null;
            Profile p = new Profile();
            String first = activity.getIntent().getStringExtra("firstName");
            String last = activity.getIntent().getStringExtra("lastName");
            String bio = activity.getIntent().getStringExtra("bio");
            String picSource = activity.getIntent().getStringExtra("prosource");
            int id = activity.getIntent().getIntExtra("userID", 2);
            try {
                 p = new Profile(first, last, bio ,picSource, id);
            } catch (Exception e) {

            }
            return p;
        }
        @Override
        protected void onPostExecute(Profile p) {
            TextView name = findViewById(R.id.name); //R.id.x refers to the textview with ID x
            name.setText((p.getFirstName() + " " + p.getLastName()));
            TextView bio = findViewById(R.id.bio);
            bio.setText(p.getBioInfo());
            ImageView img = findViewById(R.id.imageView2);
            img.setImageBitmap((Bitmap) activity.getIntent().getParcelableExtra("picture"));
        }
    }

}
