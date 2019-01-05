
package com.isaacna.projects;
import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;


public class changePhotoActivity extends AppCompatActivity {

    Button CaptureImageFromCamera, UploadImageToServer;

    ImageView ImageViewHolder;

    //EditText imageName;
    ProgressDialog progressDialog;
    Intent intent;
    public static final int RequestPermissionCode = 1;
    Bitmap bitmap;
    boolean check = true;
    String GetImageNameFromEditText;
    String ImageNameFieldOnServer = "image_name";
    String ImagePathFieldOnServer = "image_path";
    String ImageUploadPathOnSever = "http://ec2-34-215-159-222.us-west-2.compute.amazonaws.com/alt/testingTestily2.php";

    private static int RESULT_LOAD_IMG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        CaptureImageFromCamera = (Button) findViewById(R.id.button);
        // ImageViewHolder = (ImageView) findViewById(R.id.imageView);
        UploadImageToServer = (Button) findViewById(R.id.button2);
        // imageName = Integer.toString(getIntent().getIntExtra("userID",0));// (EditText) findViewById(R.id.editText);


        Button btn=(Button)findViewById(R.id.button2);
        btn.setVisibility(View.GONE);
        //EnableRuntimePermissionToAccessCamera();

//        CaptureImageFromCamera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//
//                startActivityForResult(intent, 7);
//
//            }
//        });

        UploadImageToServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //GetImageNameFromEditText = imageName.getText().toString();
                GetImageNameFromEditText = Integer.toString(getIntent().getIntExtra("userID",0));
                ImageUploadToServerFunction();

            }
        });
    }//asdf

    private void getImageFromAlbum(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
    }

    public void findImage(View view){
        getImageFromAlbum();
        Button btn=(Button)findViewById(R.id.button2);
        btn.setVisibility(View.VISIBLE);
    }

    public void sendImageAfterUpload(View view){
        ImageUploadToServerFunction();
    }

    // Star activity for result method to Set captured image on image view after click.


//    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
//        super.onActivityResult(reqCode, resultCode, data);
//
//        if (resultCode == RESULT_OK) try {
//            final Uri imageUri = data.getData();
//            final InputStream imageStream = getContentResolver().openInputStream(imageUri);
//            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//            //ImageView image_view;
//            ImageView imageView = findViewById(R.id.imageView);
//            imageView.setImageBitmap(selectedImage);
//            this.img = selectedImage;
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            //Toast.makeText(PostImage.this, "Something went wrong", Toast.LENGTH_LONG).show();
//        }
//        //else Toast.makeText(PostImage.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //System.out.println("test test test test sodifj paosdijf poaisjd fpoaisjd f");
        super.onActivityResult(requestCode, resultCode, data);
        //System.out.println("TESTSTSERSTE");
        //System.out.println(resultCode + " " + RESULT_OK + "    " + data.toString());
//
        if (resultCode == RESULT_OK) try {
            final Uri imageUri = data.getData();
            final InputStream imageStream = getContentResolver().openInputStream(imageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            //ImageView image_view;
            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageBitmap(selectedImage);
            this.bitmap = selectedImage;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //Toast.makeText(PostImage.this, "Something went wrong", Toast.LENGTH_LONG).show();
        }

    }

    // Requesting runtime permission to access camera.
    public void EnableRuntimePermissionToAccessCamera(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(changePhotoActivity.this,
                Manifest.permission.CAMERA))
        {

            // Printing toast message after enabling runtime permission.
            Toast.makeText(changePhotoActivity.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(changePhotoActivity.this,new String[]{Manifest.permission.CAMERA}, RequestPermissionCode);

        }
    }

    // Upload captured image online on server function.
    public void ImageUploadToServerFunction(){

        ByteArrayOutputStream byteArrayOutputStreamObject ;
        byteArrayOutputStreamObject = new ByteArrayOutputStream();

        // Converting bitmap image to jpeg format, so by default image will upload in jpeg format.
        Bitmap smallMap = getResizedBitmap(bitmap,500);
        smallMap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);

        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();
        final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {


            changePhotoActivity activity;
            AsyncTaskUploadClass(changePhotoActivity a){
                activity = a;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Showing progress dialog at image upload time.
                progressDialog = ProgressDialog.show(changePhotoActivity.this,"Image is Uploading","Please Wait",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {
                super.onPostExecute(string1);
                // Dismiss the progress dialog after done uploading.
                progressDialog.dismiss();

                // Printing uploading success message coming from server on android app.
                Toast.makeText(changePhotoActivity.this,string1,Toast.LENGTH_LONG).show();
                System.out.println(string1);
                // Setting image as transparent after done uploading.
//                ImageViewHolder.setImageResource(android.R.color.transparent);
                Intent in = new Intent(activity, ProfileActivity.class);//comment
                in.putExtras(activity.getIntent());
                startActivity(in);

            }

            @Override
            protected String doInBackground(Void... params) {

                ImageProcessClass imageProcessClass = new ImageProcessClass();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();

                HashMapParams.put(ImageNameFieldOnServer, GetImageNameFromEditText);

                HashMapParams.put(ImagePathFieldOnServer, ConvertImage);

                String FinalData = imageProcessClass.ImageHttpRequest(ImageUploadPathOnSever, HashMapParams);
                System.out.println(FinalData);
                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass(this);

        AsyncTaskUploadClassOBJ.execute();
    }

    public class ImageProcessClass{

        public String ImageHttpRequest(String requestURL,HashMap<String, String> PData) {

            StringBuilder stringBuilder = new StringBuilder();

            try {

                URL url;
                HttpURLConnection httpURLConnectionObject ;
                OutputStream OutPutStream;
                BufferedWriter bufferedWriterObject ;
                BufferedReader bufferedReaderObject ;
                int RC ;

                url = new URL(requestURL);

                httpURLConnectionObject = (HttpURLConnection) url.openConnection();

                httpURLConnectionObject.setReadTimeout(19000);

                httpURLConnectionObject.setConnectTimeout(19000);

                httpURLConnectionObject.setRequestMethod("POST");

                httpURLConnectionObject.setDoInput(true);

                httpURLConnectionObject.setDoOutput(true);

                OutPutStream = httpURLConnectionObject.getOutputStream();

                bufferedWriterObject = new BufferedWriter(

                        new OutputStreamWriter(OutPutStream, "UTF-8"));

                bufferedWriterObject.write(bufferedWriterDataFN(PData));

                bufferedWriterObject.flush();

                bufferedWriterObject.close();

                OutPutStream.close();

                RC = httpURLConnectionObject.getResponseCode();

                if (RC == HttpsURLConnection.HTTP_OK) {

                    bufferedReaderObject = new BufferedReader(new InputStreamReader(httpURLConnectionObject.getInputStream()));

                    stringBuilder = new StringBuilder();

                    String RC2;

                    while ((RC2 = bufferedReaderObject.readLine()) != null){

                        stringBuilder.append(RC2);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {

            StringBuilder stringBuilderObject;

            stringBuilderObject = new StringBuilder();

            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {

                if (check)

                    check = false;
                else
                    stringBuilderObject.append("&");

                stringBuilderObject.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));

                stringBuilderObject.append("=");

                stringBuilderObject.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }

            return stringBuilderObject.toString();
        }

    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(changePhotoActivity.this,"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(changePhotoActivity.this,"Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

}
//from https://stackoverflow.com/questions/11766878/sending-files-using-post-with-httpurlconnection?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa


