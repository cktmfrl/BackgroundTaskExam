package com.example.backgroundtaskexam.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
    private static final String TAG = MyService.class.getSimpleName();
    private Thread mThread;
    private int mCount = 0;

    // * 서비스는 액티비티와 독립적으로 동작하므로 앱을 종료하고 다시 실행해도 서비스의 생명주기에는 아무 영향이 없음.
    public MyService() {
    }

    // * 서비스를 사용할 때 주의할 점(501p)으로 "모든 컴포넌트는 메인 스레드에서 동작하므로"
    // 서비스가 수행하는 작업이 메인 스레드를 점유하는 일이라면 UI를 차단하지 않도록(ANR 발생 방지)
    // 서비스 내에서 스레드를 생성해야 한다는 점..
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mThread == null) {
            mThread = new Thread("My Thread") { // name
                @Override
                public void run() {
                    for (int i = 0; i < 5; i++) {
                        try {
                            mCount++;
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            break;
                        }
                        Log.d(TAG, "서비스 동작 중 " + mCount);
                    }// for
                }// run
            };// Thread
            mThread.start();
        }

        return START_STICKY;
        // * 반환값 옵션 설명(502p) : 안드로이드 시스템은 자원이 부족하면
        // 백그라운드에서 실행되는 프로세스를 하나씩 강제 종료하므로 서비스 역시 강제 종료 대상이 될 수 있음.
        // 이를 대비하여 강제 종료된 서비스를 살릴 수 있는 다양한 옵션이 있음.
        //
        // START_NOT_STICKY : 전달할 인텐트가 있을 때를 제외하고, 서비스가 중단되면 재생성하지 않음.
        // START_STICKY : 서비스가 중단되면 서비스를 재시작하지만, 인텐트는 다시 저달하지 않음. 무기한 동작할 때 적합. ex) 미디어 플레이어
        // START_REDELIVER_INTENT : 인텐트와 함께 서비스를 재시작. 파일 다운로드와 같이 무엇을 다운로드했었는지 알아야 할 때 적합.

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");

        if (mThread != null) {
            mThread.interrupt();
            mThread = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}