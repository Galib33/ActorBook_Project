package com.winner.actorbook;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.winner.actorbook.databinding.ActivityActorBinding;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.Inet4Address;

public class ActorActivity extends AppCompatActivity {
    private ActivityActorBinding binding;
    String Name;
    String Imdb;
    Long Age;
    SQLiteDatabase database;

    ActivityResultLauncher<Intent> intentActivityResultLauncher;
    ActivityResultLauncher<String> permissionActivityResultLauncher;
    Bitmap selectedbitmapimage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityActorBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        register();
    }

    public void savedata(View view){
        Name=binding.name.getText().toString();
        Age=Long.parseLong(binding.age.getText().toString());
        Imdb=binding.imdb.getText().toString();

        Bitmap smallpicture=imagesmalled(selectedbitmapimage,300);

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        smallpicture.compress(Bitmap.CompressFormat.JPEG,60,byteArrayOutputStream);
        byte[] byteimage=byteArrayOutputStream.toByteArray();

        try {
            database=this.openOrCreateDatabase("ActorBook",MODE_PRIVATE,null);
            database.execSQL("CREATE TABLE IF NOT EXISTS actorbook (ID INTEGER PRIMARY KEY,name VARCHAR,age LONG,imdb VARCHAR,picture BLOB)",null);
            String dataString="INSERT INTO actorbook (name,age,imdb,picture) VALUES (?,?,?,?)";
            SQLiteStatement sqLiteStatement=database.compileStatement(dataString);
            sqLiteStatement.bindString(1,Name);
            sqLiteStatement.bindLong(2,Age);
            sqLiteStatement.bindString(3,Imdb);
            sqLiteStatement.bindBlob(4,byteimage);
            sqLiteStatement.execute();

        }catch (Exception e){
            e.printStackTrace();
        }



        Intent intent=new Intent(ActorActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    public Bitmap imagesmalled(Bitmap picture,int maxsize){
        int width=picture.getWidth();
        int height=picture.getHeight();
        float nisbet=(float) (width/height);
        if(nisbet>1){
            //landscape picture
            width=maxsize;
            height=(int) (width) / (int) nisbet;
        }else{
            //portrait picture
            height=maxsize;
            width=(int) height * (int) nisbet;
        }
        return picture.createScaledBitmap(picture,width,height,true);

    }


    public void selectimage(View view){

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view,"Permission needed if you see your image",Snackbar.LENGTH_INDEFINITE).setAction("Give Agreement", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //request permission
                        permissionActivityResultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                }).show();
            }else{
                //request permission
                permissionActivityResultLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }else{
            //galery
            Intent intenttogallery=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intentActivityResultLauncher.launch(intenttogallery);
        }
    }

    public void register(){

        intentActivityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode()==RESULT_OK){
                    Intent selectedimageintent=result.getData();
                    if(selectedimageintent != null){
                        Uri selectedimageuri=selectedimageintent.getData();
                        try {
                            if(Build.VERSION.SDK_INT >= 28){
                                ImageDecoder.Source source=ImageDecoder.createSource(getContentResolver(),selectedimageuri);
                                selectedbitmapimage=ImageDecoder.decodeBitmap(source);
                                binding.image.setImageBitmap(selectedbitmapimage);
                            }else{
                                selectedbitmapimage= MediaStore.Images.Media.getBitmap(getContentResolver(),selectedimageuri);
                                binding.image.setImageBitmap(selectedbitmapimage);
                            }


                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                }
            }
        });

        permissionActivityResultLauncher=registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if(result == true){
                    Intent intenttogallery=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intentActivityResultLauncher.launch(intenttogallery);

                }else{
                    Toast.makeText(ActorActivity.this, "Permission Needed!!", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }




}