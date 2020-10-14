package in.binplus.learnbygenius;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import in.binplus.learnbygenius.networkconnectivity.NoInternetConnectionActivity;

public class MainActivity extends AppCompatActivity {
    AlertDialog dialog;
    ProgressDialog progressDialog;
    boolean doubleBackToExitPressedOnce = false;
    WebView browser;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_FIELS = 102;
    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog=new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Please wait for a moment");

        txt=(TextView)findViewById(R.id.text_url);

        browser=(WebView)findViewById(R.id.webView);
        if (ConnectivityReceiver.isConnected()) {

            browser.getSettings().setLoadsImagesAutomatically(true);
            browser.getSettings().setJavaScriptEnabled(true);
            browser.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            browser.setWebViewClient(new myWebViewClient());
            checkAppPermissions();
        }
        else {
            Intent intent = new Intent(MainActivity.this, NoInternetConnectionActivity.class);
            startActivity(intent);

        }




    }
    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        if(browser.canGoBack())
        {
            if (ConnectivityReceiver.isConnected()) {

                browser.goBack();
                return;
            }
            else {
                Intent intent = new Intent(MainActivity.this, NoInternetConnectionActivity.class);
                startActivity(intent);

            }
        }
        else if(doubleBackToExitPressedOnce)
        {
            finish();
        }
        this.doubleBackToExitPressedOnce=true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        },1000);

    }

    private class myWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String Url) {
            if (ConnectivityReceiver.isConnected()) {

                view.loadUrl(Url);
                txt.setText(Url);
            }
            else
            {
                Intent intent = new Intent(MainActivity.this, NoInternetConnectionActivity.class);
                startActivity(intent);

            }
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressDialog.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressDialog.dismiss();
        }
    }

    public void checkAppPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_NETWORK_STATE)
                        != PackageManager.PERMISSION_GRANTED
        ) {
            if ( ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.INTERNET) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_NETWORK_STATE)) {
                go_next();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.INTERNET,
                                android.Manifest.permission.ACCESS_NETWORK_STATE
                        },
                        MY_PERMISSIONS_REQUEST_WRITE_FIELS);
            }
        } else {
            go_next();
        }
    }

    private void go_next() {
        String url="https://learnbygenius.com";

        String address=txt.getText().toString().trim();

        if(address.equals("check"))
        {
            address="https://learnbygenius.com";
        }

        browser.loadUrl(address);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_FIELS) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("App required some permission please enable it")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                openPermissionScreen();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                                dialog.dismiss();
                            }
                        });
                dialog = builder.show();
            }
            return;
        }
    }

    public void openPermissionScreen() {
        // startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", MainActivity.this.getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}