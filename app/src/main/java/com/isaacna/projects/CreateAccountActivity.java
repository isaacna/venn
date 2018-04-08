package com.isaacna.projects;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CreateAccountActivity extends AppCompatActivity {

    private static int RESULT_LOAD_IMG = 1;
    private Bitmap img;
    private String URLString = "http://ec2-34-215-159-222.us-west-2.compute.amazonaws.com/alt/receiveImage.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
    }

    private void getImageFromAlbum(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
    }

    public void findImage(View view){
        getImageFromAlbum();
    }

    public void sendImageAfterUpload(View view){
        new UploadTask().execute(this.img);
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == RESULT_OK) try {
            final Uri imageUri = data.getData();
            final InputStream imageStream = getContentResolver().openInputStream(imageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            //ImageView image_view;
            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageBitmap(selectedImage);
            this.img = selectedImage;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //Toast.makeText(PostImage.this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
        //else Toast.makeText(PostImage.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
    }
    private class UploadTask extends AsyncTask<Bitmap, Void, Void> {


        String attachmentName = "bitmap";
        String attachmentFileName = "bitmap.bmp";
        String crlf = "\r\n";
        String twoHyphens = "--";
        String boundary =  "*****";
        @Override
        protected Void doInBackground(Bitmap... bitmaps) {
            if(bitmaps[0] == null){
                return null;
            }
            else{
                try{
                    Bitmap bitmap = bitmaps[0];
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                    InputStream in = new ByteArrayInputStream(stream.toByteArray());
//                    URL url = new URL(URLString);
//                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //from https://stackoverflow.com/questions/11766878/sending-files-using-post-with-httpurlconnection?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa



                    HttpURLConnection httpUrlConnection = null;
                    URL url = new URL(URLString);
                    httpUrlConnection = (HttpURLConnection) url.openConnection();
                    httpUrlConnection.setUseCaches(false);
                    httpUrlConnection.setDoOutput(true);

                    httpUrlConnection.setRequestMethod("POST");
                    httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
                    httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
                    httpUrlConnection.setRequestProperty(
                            "Content-Type", "multipart/form-data;boundary=" + this.boundary);

                    DataOutputStream request = new DataOutputStream(
                            httpUrlConnection.getOutputStream());

                    request.writeBytes(this.twoHyphens + this.boundary + this.crlf);
                    request.writeBytes("Content-Disposition: form-data; name=\"" +
                            this.attachmentName + "\";filename=\"" +
                            this.attachmentFileName + "\"" + this.crlf);
                    request.writeBytes(this.crlf);

                    byte[] pixels = new byte[bitmap.getWidth() * bitmap.getHeight()];
                    for (int i = 0; i < bitmap.getWidth(); ++i) {
                        for (int j = 0; j < bitmap.getHeight(); ++j) {
                            //we're interested only in the MSB of the first byte,
                            //since the other 3 bytes are identical for B&W images
                            pixels[i + j] = (byte) ((bitmap.getPixel(i, j) & 0x80) >> 7);
                        }
                    }

                    request.write(pixels);

                    request.writeBytes(this.crlf);
                    request.writeBytes(this.twoHyphens + this.boundary +
                            this.twoHyphens + this.crlf);
                    request.flush();
                    request.close();

                    InputStream responseStream = new
                            BufferedInputStream(httpUrlConnection.getInputStream());

                    BufferedReader responseStreamReader =
                            new BufferedReader(new InputStreamReader(responseStream));

                    String line = "";
                    StringBuilder stringBuilder = new StringBuilder();

                    while ((line = responseStreamReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    responseStreamReader.close();

                    String response = stringBuilder.toString();
                    System.out.println(response);
                    responseStream.close();
                    httpUrlConnection.disconnect();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}
