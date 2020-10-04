package providers;

import com.google.gson.reflect.TypeToken;
import data.CurrentUser;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import request.*;
import response.ChangePasswordResponse;
import response.ChatResponse;
import response.SignupResponse;

import java.lang.reflect.Type;
import java.util.List;


public class ServerConnectionProvider {
    private static ServerConnectionProvider instance;
    private static String token;

    public static final String serverURL = "http://localhost:8080/";

    public static ServerConnectionProvider getInstance() {
        if(instance == null) instance = new ServerConnectionProvider();
        return instance;
    }

    private ServerConnectionProvider(){}


    public ResponseEntity<SignupResponse> signUpRequest(SignupRequest requestEntity){
        var url = serverURL + "users";
        RestTemplate restTempl = new RestTemplate();
        return restTempl.postForEntity(url, requestEntity, SignupResponse.class);
    }


    public ResponseEntity<String> loginRequest(LoginRequest requestEntity){
        var url = serverURL + "login";
        RestTemplate restTempl = new RestTemplate();
        //return restTempl.postForEntity(url, requestEntity, JwtResponse.class);
        return  restTempl.postForEntity(url, requestEntity, String.class);
    }

//TODO: RENAME
    //TODO: getToken -> sendToken
    //TODO: sendTokenForChangingPassword -> userPassword(get)
    public ResponseEntity<ChangePasswordResponse> getToken(SendChangePasswordTokenRequest requestEntity){
        var url = serverURL + "sendTokenForChangingPassword";
        RestTemplate restTempl = new RestTemplate();
        return restTempl.postForEntity(url, requestEntity, ChangePasswordResponse.class);
    }


    //TODO: submitChangingPassword -> userPassword(post)
    public ResponseEntity<String> changePassword(ChangePasswordRequest requestEntity){
        var url = serverURL + "submitChangingPassword";
        RestTemplate restTempl = new RestTemplate();
        return restTempl.postForEntity(url, requestEntity, String.class);
    }

    public ResponseEntity<List<ChatResponse>> getUserChats(){
        var url = serverURL + "users/me/chats";
        RestTemplate restTempl = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", CurrentUser.getAuthToken());
        Type listType = new TypeToken<List<ChatResponse>>() {
        }.getType();

        return restTempl.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<List<ChatResponse>>() {
        });
    }

    public ResponseEntity<ChatResponse> addChat(AddChatRequest request){
        var url = serverURL + "users/me/chats";
        RestTemplate restTempl = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", CurrentUser.getAuthToken());

        return restTempl.exchange(url, HttpMethod.POST, new HttpEntity<>(request, headers), ChatResponse.class);
    }

    //TODO: раскомментируй меня!!!!
//    public ResponseEntity<MessageResponse> sendMessage(SendMessageRequest request){
//        var url = serverURL + "users/me"; //TODO: CHANGE LINK
//        RestTemplate restTempl = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Bearer " + CurrentUser.getAuthToken());
//
//        return restTempl.exchange(url, HttpMethod.POST, new HttpEntity<>(headers), MessageResponse.class);
//    }



}
