package com.example.finaltask.model.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Integer id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String image;
}