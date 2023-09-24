package com.example.Cloud.service;


import com.example.Cloud.entity.UserFcmTokenDetails;
import com.example.Cloud.repository.UserFcmTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserFcmTokenService {
    @Autowired
    private UserFcmTokenRepository repository;

    public UserFcmTokenDetails createFcmTokenDetails(UserFcmTokenDetails userFcmTokenDetails){
        UserFcmTokenDetails saveDetails = repository.save(userFcmTokenDetails);
        return saveDetails;
    }
    public  UserFcmTokenDetails getFcmTokenDetailsByKid(String kid){
        UserFcmTokenDetails getDetails =repository.findById(kid).orElse(null);
        return getDetails;
    }
    ////////////////////////////////////////////////////////////////////////////

//    public UserFcmTokenDetails getFcmTokenDetailsByKid(String kid) {
//        UserFcmTokenDetails getDetails = repository.findById(kid).orElse(null);
//
//        // Check if the record was found in the database
//        if (getDetails == null) {
//            // You can throw an exception or return a custom response here.
//            // For simplicity, let's return a ResponseEntity with HTTP status 404 Not Found.
//            return UserFcmTokenDetails;
//        }
//
//        return getDetails;
//    }
    //////////////////////////////////////////////////////////////////////////////
//    public UserFcmTokenDetails updatedFcmTokenDetail(String kid, UserFcmTokenDetails userFcmTokenDetails){
//        UserFcmTokenDetails updateUserFcmTokenDetails = repository.findById(kid).get();
//        updateUserFcmTokenDetails.setFcmToken(userFcmTokenDetails.getFcmToken());
//        updateUserFcmTokenDetails.setUpdatedTime(userFcmTokenDetails.getUpdatedTime());
//        updateUserFcmTokenDetails.setDeviceType(userFcmTokenDetails.getDeviceType());
//        updateUserFcmTokenDetails.setDeviceUuid(userFcmTokenDetails.getDeviceUuid());
//        updateUserFcmTokenDetails.setProject(userFcmTokenDetails.getProject());
//        updateUserFcmTokenDetails.setId(userFcmTokenDetails.getId());
//        UserFcmTokenDetails updateDetails = repository.save(userFcmTokenDetails);
//        return updateDetails;
//
//    }
    ///////////////////////////////update/////////////////////////
public UserFcmTokenDetails updatedFcmTokenDetail(UserFcmTokenDetails updateData) {
    String kid = updateData.getKid();
    UserFcmTokenDetails updateUserFcmTokenDetails = repository.findById(kid).orElse(null);

    if (updateUserFcmTokenDetails != null) {
        updateUserFcmTokenDetails.setFcmToken(updateData.getFcmToken());
        updateUserFcmTokenDetails.setUpdatedTime(updateData.getUpdatedTime());
        updateUserFcmTokenDetails.setDeviceType(updateData.getDeviceType());
        updateUserFcmTokenDetails.setDeviceUuid(updateData.getDeviceUuid());
        updateUserFcmTokenDetails.setProject(updateData.getProject());
        updateUserFcmTokenDetails.setId(updateData.getId());

        UserFcmTokenDetails updateDetails = repository.save(updateUserFcmTokenDetails);
        return updateDetails;
    } else {

        return null;
    }
}
    ////////////////////////////////////////////////////////////////////////

    public void deleteFcmTokenDetails(String kid){
        UserFcmTokenDetails findById = repository.findById(kid).get();
        repository.deleteById(kid);
    }

}
