package com.example.sfreaderdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;


import com.sifli.siflicore.error.SFError;
import com.sifli.siflireadersdk.reader.SFReaderManager;
import com.sifli.siflireadersdk.reader.SFReaderManagerCallback;
import com.sifli.siflireadersdk.result.SFReaderResult;
import com.example.sfreaderdemo.*;

public class MainActivity extends AppCompatActivity implements SFReaderManagerCallback, View.OnClickListener{
    private final static String TAG = "MainActivity";
    private Button assetGetBtn;
    private Button assetClearBtn;
    private Button assetOnBtn;
    private Button assetOffBtn;
    private ProgressBar progressBar;

    private SFReaderManager readerManager;
    private String targetMac = "FF:FF:79:DA:77:C8";//write your ble device mac here!!!
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupView();
        //初始化
        this.readerManager = SFReaderManager.getInstance();
        this.readerManager.init(this.getApplication());
        //设置回调
        this.readerManager.setManagerCallback(this);
        String[] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.BLUETOOTH_CONNECT};
        this.requestPermissions(permissions,1);
    }

    private void setupView(){
        assetGetBtn = findViewById(R.id.asset_get_btn);
        assetClearBtn = findViewById(R.id.asset_clear_btn);
        assetOnBtn = findViewById(R.id.asset_on_btn);
        assetOffBtn = findViewById(R.id.asset_off_btn);
        progressBar = findViewById(R.id.main_progress_pb);

        assetGetBtn.setOnClickListener(this);
        assetClearBtn.setOnClickListener(this);
        assetOnBtn.setOnClickListener(this);
        assetOffBtn.setOnClickListener(this);

    }

    @Override
    public void onManagerStatusChanged(int status) {
        Log.i(TAG,"onManagerStatusChanged =" + status);
    }

    @Override
    public void onProgress(long currentBytes, long totalBytes) {
        int progress = 0;
        if(totalBytes > 0){
            double percent = (double)currentBytes / totalBytes;
            progress = (int)(percent * 100);
        }
        int finalProgress = progress;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setProgress(finalProgress);
            }
        });
    }

    @Override
    public void onComplete(boolean success, SFReaderResult result, SFError error) {
        //读取到的文件列表从 result.getRcvFiles()返回
        String log = "task complete.success=" + success + ",req=" + result.getRequestCommand()  + ",fileCount=" + result.getRcvFiles().size();
        Log.i(TAG,log);
        for (String path:result.getRcvFiles()) {
            Log.i(TAG,path);
        }
    }



    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if(viewId == R.id.asset_on_btn){
            openAssetOption(true);
        }else if(viewId == R.id.asset_get_btn){
            readAsset();
        }else if(viewId == R.id.asset_clear_btn){
            clearAsset();
        }else if(viewId == R.id.asset_off_btn){
            openAssetOption(false);
        }
    }

    private void openAssetOption(boolean option){
        if(targetMac == null || targetMac.isEmpty()){
            return;
        }
        this.readerManager.assetOptionRequest(option,targetMac);
    }

    private void readAsset(){
        if(targetMac == null || targetMac.isEmpty()){
            return;
        }

        String logDir = FolderHelper.getDeviceAssetPath(this);
        Log.i(TAG,"REQ_GET_ASSET:" + logDir);
        this.readerManager.readAssetRequest(logDir,targetMac);
    }

    private void clearAsset(){
        if(targetMac == null || targetMac.isEmpty()){
            return;
        }
        Log.i(TAG,"clearAsset");
        this.readerManager.clearAssetRequest(targetMac);
    }
}