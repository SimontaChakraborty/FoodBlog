package edu.ewubd.foodblog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class PostActivity extends Activity {
    private int  SELECT_PICTURE;
    private EditText titleTF, descriptionTF;
    Uri selectedImageUri;
    private ImageView image;
    //private String existingKey = null;
    Database db;

    //private String existingKey = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_post);

        titleTF = findViewById(R.id.textTitle);
        setEditTextMaxLength(titleTF, 64);

        descriptionTF = findViewById(R.id.textDesc);
        setEditTextMaxLength(descriptionTF,500);
        image = (ImageView) findViewById(R.id.imageBtn);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });
//        Intent i = getIntent();
//        existingKey = i.getStringExtra("PostKey");
//        initializeFormWithExistingData(existingKey);

        findViewById(R.id.postBtn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                saveposts();
            }
        });
        findViewById(R.id.CancelBtn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PostActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });


    }


    public void saveposts(){
        db = new Database(this);
        String title = titleTF.getText().toString();
        String desc = descriptionTF.getText().toString();
        String uri="";
        if(selectedImageUri != null){
            uri=encodeImage(image);
        }
        String userMail = db.getCurrentUser();
        String dateTime = java.text.DateFormat.getDateTimeInstance().format(new Date());

        String errorMsg = "";
        if(title==null || title.length()<4){
            errorMsg += "Post Title should have more than 4 characters\n";
        }
        if(desc.isEmpty()){
            errorMsg += "Description Cannot be empty\n";
        }
        if(desc.length()<10){
            errorMsg += "Description should have more than 10 Characters\n";
        }
        if(errorMsg.isEmpty()){
            String value = title+":-;-:"+desc+":-;-:"+uri+":-;-:"+userMail+":-;-:"+dateTime;
            String key = " ";
            key = title;
            System.out.println("Key: "+key);
            System.out.println("Value: "+value);
            Util.getInstance().setKeyValue(PostActivity.this,key,value);
            showDialog("Posts has been saved successfully","Info","Ok",false);

        } else{
            showDialog(errorMsg,"Error in Posts Data","Back",true);
        }
        System.out.println("Post Title: "+ title);
        System.out.println("Description: "+desc);
    }

//    private void initializeFormWithExistingData(String key){
//
//        String value = Util.getInstance().getValueByKey(this, key);
//        System.out.println("Key: " + key + "; Value: "+value);
//
//        if(value != null) {
//            String[] fieldValues = value.split(":-;-:");
//            String title = fieldValues[0];
//            String desc = fieldValues[1];
//            String img = fieldValues[2];
//
//            titleTF.setText(title);
//            descriptionTF.setText(desc);
//            showDecodedImage(image,img);
//        }
//
//    }

    private void showDialog(String message, String title,String buttonlabel,boolean closeDialog ){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Uncomment the below code to Set the message and title from the strings.xml file
        builder.setMessage(message);
        builder.setTitle(title);

        //Setting message manually and performing action on button click

        builder.setCancelable(false)
                .setNegativeButton(buttonlabel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(closeDialog){
                            dialog.cancel();
                        }
                        else{
                            finish();
                        }
                    }
                });

        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        //alert.setTitle("Error Dialog");
        alert.show();
    }

    public void setEditTextMaxLength(EditText et, int length){
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(length);
        et.setFilters(filterArray);
    }
    void imageChooser(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction((Intent.ACTION_GET_CONTENT));

        startActivityForResult(Intent.createChooser(i,"Select Picture"),SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(resultCode == RESULT_OK){
            if(requestCode == SELECT_PICTURE){
                selectedImageUri = data.getData();
                if (null!= selectedImageUri){
                    image.setImageURI(selectedImageUri);
                }
            }
        }
    }
    // pass the ImageView to encode the image that the ImageView is showing
    public String encodeImage(ImageView myImageView){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BitmapDrawable drawable = (BitmapDrawable) myImageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        //bitmap = scaleDown(bitmap, 120, true); // if you want to resize the image with max height/width 120dp
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return imageString;
    }
    public Bitmap scaleDown(Bitmap realImage, float maxImageSize, boolean filter) {
        float ratio = Math.min( (float) maxImageSize / realImage.getWidth(), (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());
        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width, height, filter);
        return newBitmap;
    }
    // pass the ImageView in which you want to show the image
// pass the encoded string of an image that you stored previously in the SQLite
    public void showDecodedImage(ImageView myImageView, String encodedImageString){
        byte[] decodedString = Base64.decode(encodedImageString, Base64.DEFAULT);
        Bitmap myImage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        myImageView.setImageBitmap(myImage);
    }
}
