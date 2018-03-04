package com.isaacna.projects;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class JoinCommActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_comm);

    }


    public void goToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);

        intent.putExtras(getIntent().getExtras());
        startActivity(intent);
    }


}
