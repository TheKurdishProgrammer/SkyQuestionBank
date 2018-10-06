package com.example.mohammed.skyquestionbank.interfaces;

import com.example.mohammed.skyquestionbank.models.User;

import java.util.List;

public interface OnUserLoad {
    void onLoad(User user);

    void onLoad(List<User> users);
}
