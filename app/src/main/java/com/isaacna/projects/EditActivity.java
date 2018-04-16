package com.isaacna.projects;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
    }

    public void goToPhoto(View view){
        Intent intent = new Intent(this, changePhotoActivity.class);
        intent.putExtras(this.getIntent());
        startActivity(intent);
    }

    public void goToBio(View view){

    }
}
