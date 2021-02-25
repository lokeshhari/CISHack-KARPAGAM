package com.example.descholar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;


public class MainActivity2 extends AppCompatActivity {

    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        checkDownloadPermission();
        webView = findViewById(R.id.webview);
        String URL = "https://lokallibrary.herokuapp.com";
        webView.loadUrl(URL);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                MainActivity2.this.setTitle(view.getTitle());
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        final String folder_main = "DeScholar";

        final File f=new File(Environment.getExternalStorageDirectory(),folder_main);
        if(!f.exists()||!f.isDirectory()&&Build.VERSION.SDK_INT<=Build.VERSION_CODES.LOLLIPOP)
        {
            f.mkdirs();
        }


        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
        {
            f.delete();
        }
        webView.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {


                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                try {
                        String fname = webView.getTitle();
                        File file;
                        if(Build.VERSION.SDK_INT<=Build.VERSION_CODES.LOLLIPOP) {
                            file = new File(Environment.getExternalStorageDirectory(),"DeScholar/" + fname + ".pdf");
                        }
                        else if(Build.VERSION.SDK_INT<=Build.VERSION_CODES.P)
                        {
                            file=new File(Environment.getExternalStorageDirectory(),"storage/emulated/0/DeScholar/"+fname+".pdf");
                        }
                        else
                        {
                            file=new File((getBaseContext().getExternalFilesDir(folder_main)),fname+".pdf");
                        }
                        if(file.isFile()||file.exists())
                        {
                            Toast.makeText(getApplicationContext(),fname+" was already downloaded check in your saved books",Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), fname + " is downloading", Toast.LENGTH_LONG).show();
                            String cookies = CookieManager.getInstance().getCookie(url);
                            request.setTitle(fname + ".pdf");
                            request.allowScanningByMediaScanner();
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!

                            request.setMimeType("application/pdf");
                            request.setVisibleInDownloadsUi(true);
                            if(Build.VERSION.SDK_INT<=Build.VERSION_CODES.LOLLIPOP) {
                                request.setDestinationInExternalPublicDir(folder_main, fname + ".pdf");
                            }
                            else if(Build.VERSION.SDK_INT<=Build.VERSION_CODES.P)
                            {
                                request.addRequestHeader("cookie", cookies);
                                request.addRequestHeader("User-Agent", userAgent);
                                request.setDestinationInExternalPublicDir(f.getAbsolutePath(),fname+".pdf");
                            }
                            else
                            {
                                request.addRequestHeader("cookie", cookies);
                                request.addRequestHeader("User-Agent", userAgent);
                                request.setDestinationInExternalFilesDir(getBaseContext(),folder_main,fname+".pdf");
                            }
                            DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                            dm.enqueue(request);
                            request.setDescription("Downloading file "+fname);

                            registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                        }

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                }
            }


            BroadcastReceiver onComplete = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String fname=webView.getTitle();
                    Toast.makeText(getApplicationContext(), fname+" Downloading Completed successfully", Toast.LENGTH_SHORT).show();
                }
            };
        });
    }


    private void checkDownloadPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity2.this, Manifest.permission.WRITE_EXTERNAL_STORAGE )) {
            Toast.makeText(MainActivity2.this, "Write External Storage permission allows us to save files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity2.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity2.this, Manifest.permission.MANAGE_EXTERNAL_STORAGE )) {
                Toast.makeText(MainActivity2.this, "Write External Storage permission allows us to save files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(MainActivity2.this, new String[]{ Manifest.permission.MANAGE_EXTERNAL_STORAGE}, 100);
            }
        }
    }

    public void onBackPressed() {
        if(webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
            webView.clearCache(false);
        }
    }
}