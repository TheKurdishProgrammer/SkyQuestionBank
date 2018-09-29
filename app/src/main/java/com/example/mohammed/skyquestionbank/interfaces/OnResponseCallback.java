package com.example.mohammed.skyquestionbank.interfaces;

public interface OnResponseCallback<T> {
    void onResponse(T response);

    void onError(Throwable throwable);
}
