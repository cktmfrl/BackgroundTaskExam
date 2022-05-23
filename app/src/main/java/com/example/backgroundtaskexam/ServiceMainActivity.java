package com.example.backgroundtaskexam;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.backgroundtaskexam.service.MyIntentService;
import com.example.backgroundtaskexam.service.MyService;

public class ServiceMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        getSupportActionBar().setTitle("서비스 컴포넌트");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Up Navigatioin 표기
    }

    // 4.0 미만 및 툴바 사용 시
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: // Up/Home 버튼
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // 499p
    public void onStartService(View view) {
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
    }

    public void onStopService(View view) {
        Intent intent = new Intent(this, MyService.class);
        stopService(intent);// 서비스 내에서는 stopSelf() 를 사용해도 됨(502p)
    }

    // 505p
    public void onStartIntentService(View view) {
        Intent intent = new Intent(this, MyIntentService.class);
        startService(intent);
    }

    // 508p
    public void onStartForegroundService(View view) {
        Intent intent = new Intent(this, MyService.class);
        intent.setAction(MyService.FOREGROUND_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }
}