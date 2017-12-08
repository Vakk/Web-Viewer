package com.github.vakk.webviewer;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Valery Kotsulym on 12/8/17.
 */

public class MainActivity2 extends AppCompatActivity {

    private static final String SHARED_PREFERENCES_NAME = "url";
    private static final String URL_KEY = "url";
    private static final int REQUEST_PERMISSION_CODE = 0x004;
    private SharedPreferences mSharedPreferences;
    private SearchView mSearchView;
    private WebView mWebView;
    private Toolbar mToolbar;
    private Runnable mSearchRunnable = new Runnable() {
        @Override
        public void run() {
            String newText = mSearchView.getQuery().toString();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mWebView.findAllAsync(newText);
            } else {
                mWebView.findAll(newText);
                mWebView.findNext(true);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        initView();
        initSharedPreferences();
        loadPredefinedUrl();
    }

    private void initView() {
        mWebView = findViewById(R.id.webview);
        mWebView.setWebViewClient(new WebViewClient());
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    private void initSharedPreferences() {
        mSharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
    }

    private void loadPredefinedUrl() {
        String url = mSharedPreferences.getString(URL_KEY, null);
        if (url != null) {
            mWebView.loadUrl(url);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.item_search);
        mSearchView = (SearchView) item.getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchRunnable.run();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }

            case R.id.item_prev: {
                mWebView.findNext(false);
                return true;
            }

            case R.id.item_next: {
                mWebView.findNext(true);
                return true;
            }
            case R.id.item_load: {
                if (isAvailablePermission()) {
                    loadFile();
                } else {
                    requestReadPermission();
                }
                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    private void loadFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        startActivityForResult(intent, 1);
    }

    private boolean isAvailablePermission() {
        if (Utils.isMarshMallow()) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    private void requestReadPermission() {
        if (Utils.isMarshMallow()) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1: {
                if (data != null) {
                    Uri data1 = data.getData();
                    if (data1 != null) {
                        SharedPreferences.Editor edit = mSharedPreferences.edit();
                        edit.putString(URL_KEY, data1.toString());
                        edit.apply();
                        mWebView.loadUrl(data1.toString());
                    }
                }
            }
            break;
            default: {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadFile();
                }
                break;
            }
        }
    }
}
