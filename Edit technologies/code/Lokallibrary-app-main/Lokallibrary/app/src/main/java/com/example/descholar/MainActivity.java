package com.example.descholar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},101);
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},101);
            }
        }
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                online();
            }
        });
        Button button1 = findViewById(R.id.button3);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saved();
            }
        });
    }
    public void online(){
        Intent inten=new Intent(this,MainActivity2.class);
        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI );
        android.net.NetworkInfo data=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE );
        if ((wifi != null & data != null) && (wifi.isConnected() | data.isConnected())) {
            startActivity(inten);
        }else{
            //no connection
            Toast toast = Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_LONG);
            toast.show();
        }

    }
    public void saved(){
        Intent inten2= new Intent(this,MainActivity3.class);
        File path;
        if( Build.VERSION.SDK_INT<=android.os.Build.VERSION_CODES.LOLLIPOP) {
            path = new File(Environment.getExternalStorageDirectory(), "DeScholar");
        }
        else if(Build.VERSION.SDK_INT<= Build.VERSION_CODES.P)
        {
            path=new File(Environment.getExternalStorageDirectory(),"storage/emulated/0/DeScholar");
        }
        else
        {
            path=new File(String.valueOf(getBaseContext().getExternalFilesDir("DeScholar")));
        }
        File[] files=path.listFiles();
        try {
            if (files.length > 0) {

                startActivity(inten2);
            }
            else
            {
                Toast t=Toast.makeText(MainActivity.this,"No files currently downloaded",Toast.LENGTH_LONG);
                t.show();
            }
        }catch (NullPointerException e){
            Toast t=Toast.makeText(MainActivity.this,"No files currently downloaded",Toast.LENGTH_LONG);
            t.show();
        }
    }
}