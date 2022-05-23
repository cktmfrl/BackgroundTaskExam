package com.example.backgroundtaskexam.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

// 503p. 인텐트 서비스 최초 추가
// 1. onStartCommond() 메서드에 전달된 인텐트마다 별도의 스레드를 자동 생성해준다.
// 2. 한 번에 하나의 인텐트를 처리하는 onHandleIntent() 메서드를 제공한다.
// 3. 모든 수행이 끝나면 자동으로 stopSelf() 메서드를 호출하여 종료된다.

// * 인텐트 서비스의 특징(505p)으로 작업 스레드에서 동작한다는 것과
// 한 번에 하나의 작업만 수행한다는 점이 있다.
public class MyIntentService extends IntentService {
    private static final String TAG = MyIntentService.class.getSimpleName();

    public MyIntentService() {
        super(TAG); // 디버그 시 사용되는 스레드명
    }

    // 인텐트 서비스를 시작하면 호출됨. 이 메서드는 작업 스레드로 동작하므로(504p) 따로 스레드 객체를 만들지 않아도 됨.
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent : ");
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            Log.d(TAG, "인텐트 서비스 동작 중 " + i);
        }
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand : ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy : ");
    }
}