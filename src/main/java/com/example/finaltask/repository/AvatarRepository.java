package com.example.finaltask.repository;

import com.example.finaltask.model.entity.UserAvatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvatarRepository extends JpaRepository<UserAvatar,Long> {

}