package com.example.descholar;


import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity3 extends ListActivity {

    private List<String> fileLists=new ArrayList <String>();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String folder_main = "DeScholar";



        File path;
        if( Build.VERSION.SDK_INT<=Build.VERSION_CODES.LOLLIPOP) {
            path = new File(Environment.getExternalStorageDirectory(), "DeScholar");
        }
        else if(Build.VERSION.SDK_INT<=Build.VERSION_CODES.P)
        {
            path=new File(Environment.getExternalStorageDirectory(), "storage/emulated/0/DeScholar");
        }
        else
        {
            path=new File(String.valueOf(getBaseContext().getExternalFilesDir("DeScholar")));
        }
        ListDir(path);
        Toast toast=Toast.makeText(MainActivity3.this,"Click on the items to read",Toast.LENGTH_LONG);
        toast.show();
        toast.show();
        toast.show();

    }
    void ListDir(File f){
        File[] files=f.listFiles();
        fileLists.clear();

        assert files != null;
        for (File file : files) {
                String fname = file.getPath().substring(file.getPath().lastIndexOf('/') + 1, file.getPath().length());
                fileLists.add(fname);
            }
            listView =findViewById(R.id.Listview);
            ArrayAdapter<String> directoryList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fileLists);
            setListAdapter(directoryList);

    }
    @Override
    protected void onListItemClick(ListView l,View v,int i,long id){
        super.onListItemClick(l,v,i,id);
        File path;
        if(Build.VERSION.SDK_INT<=Build.VERSION_CODES.LOLLIPOP) {
            path = new File(Environment.getExternalStorageDirectory() + File.separator + "DeScholar" + File.separator + fileLists.get(i));
        }
        else if(Build.VERSION.SDK_INT<=Build.VERSION_CODES.P)
        {
            path= new File(Environment.getExternalStorageDirectory() + File.separator + "storage/emulated/0/DeScholar" + File.separator + fileLists.get(i));
        }
        else
        {
            path= new File(String.valueOf(getBaseContext().getExternalFilesDir( "DeScholar" + File.separator + fileLists.get(i))));
        }
        Uri uri= FileProvider.getUriForFile(MainActivity3.this,BuildConfig.APPLICATION_ID+".provider",path);
        Intent intent=new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri,"application/pdf");

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent intent2 = Intent.createChooser(intent, "Open File");
        try {
            startActivity(intent2);
        } catch (Exception e) {
            // Instruct the user to install a PDF reader here, or something
            String st=e.toString();
           Toast.makeText(MainActivity3.this,st,Toast.LENGTH_LONG).show();
        }

    }


}
