package com.example.backgroundtaskexam;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.backgroundtaskexam.service.MyIntentService;
import com.example.backgroundtaskexam.service.MyService;

public class AysncTaskMainActivity extends AppCompatActivity {

    private TextView mTextView;
    private ProgressBar mProcessBar;
    private DownloadTask mDownloadTask;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = findViewById(R.id.textView);
        mProcessBar = findViewById(R.id.progressBar);
        findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDownloadTask != null && !mDownloadTask.isCancelled()) {
                    // * 태스트 동작 취소(401p) : cancel(true) 메서드가 호출되면 AsyncTask 내부에서는 isCancelled()로 취소됨을 알 수 있음.
                    // 이때 취소된 태스크는 마지막에 onPostExecute() 대신에 onCancelled()가 호출됨.
                    mDownloadTask.cancel(true);
                }
            }
        });
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

    public void download(View view) {
        // * AsyncTask의 스레딩 규칙(399p)
        // 1. AsyncTask의 인스턴스는 반드시 메인 스레드에서 생성해야 한다.
        // 2. execute() 메서드는 반드시 메인 스레드에서 실행해야 한다.
        // 3. onPreExecute(), onPostExecute(Result), doInBackground(Params...), onProgressUpdate(Progress...) 콜백 메서드를 직접 호출하면 안 된다.
        // 4. AsyncTask의 인스턴스는 한 번만 실행할 수 있다.

        // 여러 개의 AsyncTask를 호출하면 스레딩 규칙 4번 위배. 에러가 발생하지는 않으나 호출한 순서대로 차례차례 수행됨.
//        new DownloadTask().execute();
//        new DownloadTask().execute();
//        new DownloadTask().execute();

        // * AsyncTask 동시 수행 : 동시 수행되게 하려면 exeOnExecutor()와 AsyncTask.THREAD_POOL_EXECUTOR를 사용함.
//        new DownloadTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        new DownloadTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        new DownloadTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        mDownloadTask = new DownloadTask();
        mDownloadTask.execute();
    }

    public void moveToCountDown(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    class DownloadTask extends AsyncTask<Void, Integer, Void> { // Param, Process, Result

        @Override
        protected Void doInBackground(Void... params) {
            for (int i = 0; i <= 100; i++) {
                try {
                    Thread.sleep(100); // 0.1초
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final int percent = i;
                publishProgress(percent);
                // 취소 됨
                if (isCancelled()) {
                    break;
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) { // Process
            mTextView.setText(values[0] + "%");
            mProcessBar.setProgress(values[0]);
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(AysncTaskMainActivity.this, "취소 됨", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Void result) {
            Toast.makeText(AysncTaskMainActivity.this, "완료 됨", Toast.LENGTH_SHORT).show();
        }
    }
}
