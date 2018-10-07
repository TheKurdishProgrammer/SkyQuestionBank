package com.example.mohammed.skyquestionbank.utils;

import android.os.AsyncTask;

import com.example.mohammed.skyquestionbank.interfaces.OnTimeProgressUpdate;

public class TimerAsyncTask extends AsyncTask<Void, Integer, Void> {

    private OnTimeProgressUpdate onUpdate;
    private int startTime;
    private int curretnTime;


    public TimerAsyncTask(int startTime, OnTimeProgressUpdate onUpdate) {
        this.onUpdate = onUpdate;
        this.startTime = startTime;
    }

    public int getCurrentTime() {
        return curretnTime;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        for (int i = startTime; i >= 0 && !isCancelled(); i--) {
            try {
                Thread.sleep(1000);
                curretnTime = i;
                onProgressUpdate(i);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        // here check weather its finished with true result or failed with 0
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);


        onUpdate.onUpdate(values[0]);


    }
}