package com.example.mohammed.skyquestionbank.utils;

import android.os.AsyncTask;

import com.example.mohammed.skyquestionbank.interfaces.OnTimeProgressUpdate;

public class TimerAsyncTask extends AsyncTask<Void, Integer, Void> {

    private OnTimeProgressUpdate onUpdate;

    public TimerAsyncTask(OnTimeProgressUpdate onUpdate) {
        this.onUpdate = onUpdate;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        for (int i = 59; i >= 0 && !isCancelled(); i--) {
            try {
                Thread.sleep(1000);

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