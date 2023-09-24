package com.example.Cloud.function;

import com.example.Cloud.entity.UserFcmTokenDetails;
import com.example.Cloud.service.UserFcmTokenService;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;



@Configuration
public class UserFcmTokenFunction {

    @Autowired
    private UserFcmTokenService userFcmTokenService;

    @Bean("createFcmToken")
    public Function<UserFcmTokenDetails, UserFcmTokenDetails> createFcmTokenDetails() {
        return saveFcmToken -> userFcmTokenService.createFcmTokenDetails(saveFcmToken);
    }
    @Bean("getFcmToken")
    public Function<String , UserFcmTokenDetails> getFcmTokenDetails() {
        return kid -> userFcmTokenService.getFcmTokenDetailsByKid(kid);
    }


    ///////////////////////////////////////////////////////////////
//    /////////////////////working////////////////////////
    @Bean("updateFcmToken")
    public Function<UserFcmTokenDetails, UserFcmTokenDetails> updateFcmTokenDetails() {
        return updateFcmToken -> {
            if (updateFcmToken != null) {
                return userFcmTokenService.updatedFcmTokenDetail(updateFcmToken);
            } else {
                return null;
            }
        };
    }

    /////////////////////////////////////////////////////////////////

//    @Bean("updateFcmToken")
//    public Function<UserFcmTokenDetails, HttpResponseMessage> updateFcmTokenDetails() {
//        return updateFcmToken -> {
//
//            if (updateFcmToken != null) {
//                // Call the service method to update the FCM token details
//                UserFcmTokenDetails updatedDetails = userFcmTokenService.updatedFcmTokenDetail(updateFcmToken);
//                return request.createResponseBuilder(HttpStatus.OK)
//                        .body(updatedDetails)
//                        .build();
//            } else {
//                // Optionally, you can log or handle the case where updateData is null.
//                // For example: logger.warn("Update data is missing. Aborting operation.");
//                return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
//                        .body("Update data is missing.")
//                        .build();
//            }
//        };
//    }

    //////////////////////////////////////////////////////////////////////////




    //////////////////////////////////////////////////////////////////////

@Bean("deleteFcmToken")
public Function<String, Void> deleteFcmTokenDetails() {
    return kid -> {
        if (kid != null) {
            userFcmTokenService.deleteFcmTokenDetails(kid);
        } else {
        System.out.println("Kid parameter is null.");
        }
        return null;
    };
}
    ///////////////////////////////////////////////


}
