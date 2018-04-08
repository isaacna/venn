package com.isaacna.projects;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CreateCommunityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_community);
    }

    protected void sendNewCom(View veiw){
        new CreateTask(this).execute(1);

    }

    class CreateTask extends AsyncTask<Integer, String, String> {

        private Exception exception;
        String response = "";
        StringBuilder result = new StringBuilder();
        public CreateCommunityActivity activity;

        public CreateTask(CreateCommunityActivity a) {
            this.activity = a;
        };

        protected String doInBackground(Integer... params) {
            try{

                EditText holder = findViewById(R.id.comName);//.getText().toString();
                String name = holder.getText().toString();
                holder = findViewById(R.id.editText2);
                String desc = holder.getText().toString();
                holder = findViewById(R.id.editText4);
                String par1 = holder.getText().toString();
                holder = findViewById(R.id.editText5);
                String par2 = holder.getText().toString();
                holder = findViewById(R.id.editText6);
                String par3 = holder.getText().toString();
                int userId = params[0];
                String urlString = "http://ec2-34-215-159-222.us-west-2.compute.amazonaws.com/alt/createCom.php?user_id=" + userId
                        + "&name=" + name + "&desc=" + desc + "&p1=" + par1
                        + "&p2=" + par2 + "&p3=" + par3;
                URL url =new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
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
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            Intent intent = new Intent(this.activity, JustCreatedActivity.class);
            startActivity(intent);
        }

    }

}
