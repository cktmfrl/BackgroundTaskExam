package com.example.backgroundtaskexam;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class CountdownActivity extends AppCompatActivity {

    private TextView mTextView;
    private CountTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coutdown);
        mTextView = findViewById(R.id.count);

        getSupportActionBar().setTitle("AsyncTask를 이용한 카운트다운");
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

    public void start(View view) {
        mTask = new CountTask();
        //mTask.execute(0);
        mTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 0);
    }

    public void clear(View view) {
        mTask.cancel(true);
        Toast.makeText(CountdownActivity.this, "취소", Toast.LENGTH_SHORT).show();
        mTextView.setText("0");
    }

    class CountTask extends AsyncTask<Integer, Integer, Integer> { // Param, Process, Result

        @Override
        protected Integer doInBackground(Integer... params) {
            do {
                try {
                    Thread.sleep(1000);
                    params[0]++;
                    publishProgress(params[0]); // onProgressUpdate 호출
                    if (isCancelled()) {
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (params[0] < 10);
            return params[0]; // Result
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            mTextView.setText(String.valueOf(progress[0]));
        }

        @Override
        protected void onPostExecute(Integer result) {
            mTextView.setText(String.valueOf(result)); // 종료 시 마지막값 표시
        }

//        @Override
//        protected void onCancelled() {
//            Toast.makeText(MainActivity.this, "카운트 초기화", Toast.LENGTH_SHORT).show();
//        }

    }
}