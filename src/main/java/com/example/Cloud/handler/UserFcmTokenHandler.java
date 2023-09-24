package com.example.Cloud.handler;
import com.example.Cloud.repository.UserFcmTokenRepository;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.example.Cloud.entity.UserFcmTokenDetails;
import com.example.Cloud.service.UserFcmTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.BindingName;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.adapter.azure.FunctionInvoker;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class UserFcmTokenHandler {
    @Autowired
    private UserFcmTokenService userFcmTokenService;
    @Autowired
    private FunctionCatalog functionCatalog;
    @Autowired
    private UserFcmTokenRepository repository;

@FunctionName("createFcmToken")
public HttpResponseMessage save(
        @HttpTrigger(name = "userSaveRequest", methods = {HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS)
        HttpRequestMessage<Optional<String>> request,
        ExecutionContext context) {

    try {
        String jsonString = request.getBody().orElse("");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.registerModule(new JavaTimeModule());
        UserFcmTokenDetails userFcmTokenDetails = objectMapper.readValue(jsonString, UserFcmTokenDetails.class);
        Function composed = this.functionCatalog.lookup("createFcmToken");
        UserFcmTokenDetails result = (UserFcmTokenDetails) composed.apply(userFcmTokenDetails);
        String resultJson = objectMapper.writeValueAsString(result);
        return request.createResponseBuilder(HttpStatus.OK).body(resultJson).build();
    } catch (Exception ex) {
        Logger logger = context.getLogger();
        logger.log(Level.SEVERE, "Processing Error: " + ex.getMessage());
        return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: Unable to process data").build();
    }
}

    @FunctionName("getFcmToken")
    public HttpResponseMessage get(
            @HttpTrigger(name = "userGetRequest", methods = {HttpMethod.GET}, authLevel = AuthorizationLevel.ANONYMOUS)
            HttpRequestMessage<Optional<String>> request,
            @BindingName("kid") String kid, ExecutionContext context) {

        Function<String, UserFcmTokenDetails> composed = this.functionCatalog.lookup("getFcmToken");
        if (composed != null) {
            UserFcmTokenDetails tokenDetails = composed.apply(kid);
            if (tokenDetails == null) {
                return request.createResponseBuilder(HttpStatus.NOT_FOUND)
                        .body("UserFcmTokenDetails not found for kid: " + kid)
                        .build();
            }
            ObjectMapper objectMapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addSerializer(OffsetDateTime.class, new OffsetDateTimeSerializer());
            objectMapper.registerModule(module);

            String responseJson;
            try {
                responseJson = objectMapper.writeValueAsString(tokenDetails);
            } catch (Exception e) {
                context.getLogger().severe("Error converting data to JSON: " + e.getMessage());

                return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error converting data to JSON.")
                        .build();
            }
            return request.createResponseBuilder(HttpStatus.OK)
                    .header("Content-Type", "application/json")
                    .body(responseJson)
                    .build();
        } else {
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Function 'getFcmTokenDetails' not found")
                    .build();
        }
    }
    private static class OffsetDateTimeSerializer extends JsonSerializer<OffsetDateTime> {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").withZone(ZoneOffset.UTC);

        @Override
        public void serialize(OffsetDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(formatter.format(value));
        }
    }
@FunctionName("deleteFcmToken")
public HttpResponseMessage delete(
        @HttpTrigger(name = "userDeleteRequest", methods = {HttpMethod.DELETE}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage request,
        @BindingName("kid") String kid, ExecutionContext context) {

    if (kid == null) {
         return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Kid parameter is missing.").build();
    }

    Function<String, Void> composed = this.functionCatalog.lookup("deleteFcmToken");

    if (composed == null) {

  return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR).body("Function not found.").build();
    }

    composed.apply(kid);
    return request.createResponseBuilder(HttpStatus.OK).build();
}

//    @FunctionName("updateFcmToken")
//    public HttpResponseMessage update(
//            @HttpTrigger(name = "userUpdateRequest", methods = {HttpMethod.PUT}, authLevel = AuthorizationLevel.ANONYMOUS)
//            HttpRequestMessage<Optional<String>> request,
//            @BindingName("kid") String kid,
//            ExecutionContext context) {
//        Function composed = this.functionCatalog.lookup("updateFcmToken");
//        return (HttpResponseMessage) composed.apply(request.getBody().get());
//    }
    /////////////////////////////////////////////////////////////
    //////////////////working update////////////////////////////////
//@FunctionName("updateFcmToken")
//public HttpResponseMessage update(
//        @HttpTrigger(name = "userUpdateRequest", methods = {HttpMethod.PUT}, authLevel = AuthorizationLevel.ANONYMOUS)
//        HttpRequestMessage<Optional<String>> request,
//        @BindingName("kid") String kid,
//        ExecutionContext context) {
//
//    // Check if the "kid" parameter is null or empty and handle the error condition
//    if (kid == null || kid.isEmpty()) {
//        return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
//                .body("The 'kid' parameter is missing or empty.")
//                .build();
//    }
//
//    try {
//        // Get the JSON payload from the request body
//        Optional<String> requestBody = request.getBody();
//        if (!requestBody.isPresent()) {
//            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
//                    .body("Request body is missing.")
//                    .build();
//        }
//
//        // Parse the JSON payload into a UserFcmTokenDetails object
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule()); // Register the JavaTimeModule to handle OffsetDateTime
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // Ignore unknown properties during deserialization
//        UserFcmTokenDetails updateData = objectMapper.readValue(requestBody.get(), UserFcmTokenDetails.class);
//
//        // Perform some basic validation of the updateData object (e.g., check required fields)
//        if (updateData == null) {
//            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
//                    .body("Invalid JSON data in the request body.")
//                    .build();
//        }
//
//        // Check if the "kid" parameter from the URL matches the "kid" in the request body
//        if (!kid.equals(updateData.getKid())) {
//            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
//                    .body("The 'kid' parameter in the URL does not match the 'kid' in the request body.")
//                    .build();
//        }
//
//        // Call the service method to update the FCM token details
//        UserFcmTokenDetails updatedDetails = userFcmTokenService.updatedFcmTokenDetail(updateData);
//
//        if (updatedDetails != null) {
//            // Return success response with the updated details
//            return request.createResponseBuilder(HttpStatus.OK)
//                    .body(updatedDetails)
//                    .build();
//        } else {
//            // Return error response for the case when the record with the given kid is not found
//            return request.createResponseBuilder(HttpStatus.NOT_FOUND)
//                    .body("Record with the given kid not found.")
//                    .build();
//        }
//    } catch (Exception e) {
//        // Log the exception with stack trace for debugging purposes
//        context.getLogger().severe("An error occurred while processing the request: " + e.getMessage());
//        StringWriter sw = new StringWriter();
//        PrintWriter pw = new PrintWriter(sw);
//        e.printStackTrace(pw);
//        context.getLogger().severe("Exception stack trace: " + sw.toString());
//
//        // Return error response
//        return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body("An error occurred while processing the request.")
//                .build();
//    }
//}
//////////////////////////////////////////////////////working more good
@FunctionName("updateFcmToken")
public HttpResponseMessage update(
        @HttpTrigger(name = "userUpdateRequest", methods = {HttpMethod.PATCH}, authLevel = AuthorizationLevel.ANONYMOUS)
        HttpRequestMessage<Optional<String>> request,
        @BindingName("kid") String kid,
        ExecutionContext context) throws JsonProcessingException {
    Function composed = this.functionCatalog.lookup("updateFcmToken");
    ObjectMapper objectMapper = new ObjectMapper();
    JavaTimeModule module = new JavaTimeModule();
    module.addSerializer(OffsetDateTime.class,new OffsetDateTimeSerializer());
    objectMapper.registerModule(module);

    String jsonString = request.getBody().orElse("");
    UserFcmTokenDetails result = (UserFcmTokenDetails) composed.apply(userFcmTokenService);
    String fcmJson = objectMapper.writeValueAsString(result);
    return request.createResponseBuilder(HttpStatus.OK).body(fcmJson).build();


}


    ////////////////////////////////////////////////////////////////
//    @FunctionName("updateFcmToken")
//    public UserFcmTokenDetails update(
//            @HttpTrigger(name = "userUpdateRequest", methods = {HttpMethod.PUT}, authLevel = AuthorizationLevel.ANONYMOUS)
//            HttpRequestMessage<Optional<String>> request,
//            @BindingName("kid") String kid,
//            ExecutionContext context) {
//
//        // Create ObjectMapper and register the custom serializer and deserializer for OffsetDateTime
//        ObjectMapper objectMapper = new ObjectMapper();
//        SimpleModule module = new SimpleModule();
//        module.addSerializer(OffsetDateTime.class, new OffsetDateTimeSerializer());
////        module.addDeserializer(OffsetDateTime.class, new OffsetDateTimeDeserializer());
//        objectMapper.registerModule(module);
//
//        // Retrieve the JSON data from the request body
//        Optional<String> requestBody = request.getBody();
//
//        if (requestBody.isPresent()) {
//            try {
//                // Deserialize the JSON data to UserFcmTokenDetails object
//                UserFcmTokenDetails updateData = objectMapper.readValue(requestBody.get(), UserFcmTokenDetails.class);
//
//                // Now you have the deserialized object and can proceed with your existing logic
//
//                Function<UserFcmTokenDetails, UserFcmTokenDetails> composed = this.functionCatalog.lookup("updateFcmToken");
//                UserFcmTokenDetails updatedDetails = composed.apply(updateData);
//
//                // If everything goes well, return the updated details
//                return updatedDetails;
//            } catch (JsonProcessingException e) {
//                // Handle any JSON processing errors, if needed
//                e.printStackTrace();
//                return null; // Or return an appropriate error response
//            }
//        } else {
//            // Handle the case when the request body is empty
//            // You can return an appropriate error response here
//            return null;
//        }
//    }
    /////////////////////////////////////////////////////


    @FunctionName("healthCheck")
    public HttpResponseMessage healthCheck(
            @HttpTrigger(name = "healthCheckRequest", methods = {HttpMethod.GET},
                    authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            ExecutionContext context) {
        context.getLogger().info("Performing health check...");

        try {
            long count = repository.count();
            String message = "Health check successful. Total records: " + count;
            return request.createResponseBuilder(HttpStatus.OK).body(message).build();
        } catch (Exception e) {
            // If there was an exception while accessing the database, return an error response.
            String errorMessage = "Health check failed. Error: " + e.getMessage();
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage).build();
        }
    }

}
