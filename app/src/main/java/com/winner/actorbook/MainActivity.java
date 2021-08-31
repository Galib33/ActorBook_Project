package com.winner.actorbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.winner.actorbook.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    ArrayList<ActorList> actorLists;
    ActorAdapter actorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        actorLists=new ArrayList<ActorList>();

        getDatabase();

        binding.recycler.setLayoutManager(new LinearLayoutManager(this));

        actorAdapter=new ActorAdapter(actorLists);

        binding.recycler.setAdapter(actorAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.actormenu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.addactor){
            Intent intent=new Intent(this,ActorActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void getDatabase(){
        try {
            SQLiteDatabase sqLiteDatabase=this.openOrCreateDatabase("ActorBook",MODE_PRIVATE,null);

            Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM actorbook",null);
            int nameindex=cursor.getColumnIndex("name");
            int idindex=cursor.getColumnIndex("ID");
            while (cursor.moveToNext()){

                String name=cursor.getString(nameindex);
                int id=cursor.getInt(idindex);
                ActorList listname=new ActorList(name,id);
                actorLists.add(listname);

            }
            actorAdapter.notifyDataSetChanged();
            cursor.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}