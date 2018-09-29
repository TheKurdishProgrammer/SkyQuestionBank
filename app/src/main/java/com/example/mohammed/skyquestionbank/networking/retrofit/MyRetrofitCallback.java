package com.example.mohammed.skyquestionbank.networking.retrofit;

import android.support.annotation.NonNull;

import com.example.mohammed.skyquestionbank.interfaces.OnResponseCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyRetrofitCallback<T> implements Callback<T> {
    private OnResponseCallback<T> onResponseCallback;

    public MyRetrofitCallback(OnResponseCallback<T> onResponseCallback) {
        this.onResponseCallback = onResponseCallback;
    }

    @Override
    public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {

        this.onResponseCallback.onResponse(response.body());

    }

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        onResponseCallback.onError(t);
    }
}