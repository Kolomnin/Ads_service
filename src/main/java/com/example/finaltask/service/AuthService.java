package com.example.finaltask.service;

import com.example.finaltask.dto.RegisterReq;
import com.example.finaltask.dto.Role;

public interface AuthService {
    boolean login(String userName, String password);
    boolean register(RegisterReq registerReq, Role role);
}
