package com.example.Cloud.repository;


import com.example.Cloud.entity.UserFcmTokenDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface UserFcmTokenRepository extends JpaRepository<UserFcmTokenDetails,String> {
public UserFcmTokenDetails findByKid(String kid);
}
