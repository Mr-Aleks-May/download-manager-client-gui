package com.mraleksmay.projects.download_manager.common.manager;


import com.mraleksmay.projects.download_manager.common.exception.UserAlreadyExists;
import com.mraleksmay.projects.download_manager.common.exception.UserNotFoundException;

public interface RESTClient {
    String signUp(String login, String password) throws UserAlreadyExists;

    String signIn(String login, String password) throws UserNotFoundException;
}
