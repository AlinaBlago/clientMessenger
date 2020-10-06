package providers;

import com.google.gson.reflect.TypeToken;
import data.CurrentUser;
import massage.Message;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import request.*;
import response.*;

import java.lang.reflect.Type;
import java.util.List;


public class ServerConnectionProvider {
    private static ServerConnectionProvider instance;
    private static String token;

    public static final String serverURL = "http://localhost:8080/";

    public static ServerConnectionProvider getInstance() {
        if (instance == null) instance = new ServerConnectionProvider();
        return instance;
    }

    private ServerConnectionProvider() {
    }


    public ResponseEntity<SignupResponse> signUpRequest(SignupRequest requestEntity) {
        var url = serverURL + "users";
        RestTemplate restTempl = new RestTemplate();
        return restTempl.postForEntity(url, requestEntity, SignupResponse.class);
    }


    public ResponseEntity<String> loginRequest(LoginRequest requestEntity) {
        var url = serverURL + "login";
        RestTemplate restTempl = new RestTemplate();
        return restTempl.postForEntity(url, requestEntity, String.class);
    }

    public ResponseEntity<ChangePasswordResponse> getToken(SendChangePasswordTokenRequest requestEntity) {
        var url = serverURL + "password";
        RestTemplate restTempl = new RestTemplate();
        return restTempl.postForEntity(url, requestEntity, ChangePasswordResponse.class);
    }

    public ResponseEntity<String> changePassword(ChangePasswordRequest requestEntity) {
        var url = serverURL + "password/change";
        RestTemplate restTempl = new RestTemplate();
        return restTempl.postForEntity(url, requestEntity, String.class);
    }

    public ResponseEntity<List<ChatResponse>> getUserChats() {
        var url = serverURL + "users/me/chats";
        RestTemplate restTempl = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", CurrentUser.getAuthToken());
        Type listType = new TypeToken<List<ChatResponse>>() {
        }.getType();

        return restTempl.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<List<ChatResponse>>() {
        });
    }

    public ResponseEntity<ChatResponse> addChat(AddChatRequest request) {
        var url = serverURL + "users/me/chats";
        RestTemplate restTempl = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", CurrentUser.getAuthToken());

        return restTempl.exchange(url, HttpMethod.POST, new HttpEntity<>(request, headers), ChatResponse.class);
    }

    public ResponseEntity<String> sendMessage(SendMessageRequest request) {
        var url = serverURL + "users/me/messages";
        RestTemplate restTempl = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", CurrentUser.getAuthToken());

        return restTempl.exchange(url, HttpMethod.POST, new HttpEntity<>(request, headers), String.class);
    }

    public ResponseEntity<List<MessageResponse>> getChat(GetChatRequest request) {
        var url = serverURL + "users/me/chat";
        RestTemplate restTempl = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", CurrentUser.getAuthToken());
        Type listType = new TypeToken<List<MessageResponse>>() {
        }.getType();

        return restTempl.exchange(url, HttpMethod.POST, new HttpEntity<>(request, headers), new ParameterizedTypeReference<List<MessageResponse>>() {
        });
    }

    public ResponseEntity<UserResponse> getUserInfo() {
        var url = serverURL + "users/me";
        RestTemplate restTempl = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", CurrentUser.getAuthToken());

        return restTempl.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), UserResponse.class);
    }

    public ResponseEntity<UserResponse> updateUser(UpdateUserRequest request){
        var url = serverURL + "users/me";
        //TODO: ONLY ONE ENTITY FOR ALL METHODS
        RestTemplate restTempl = new RestTemplate();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(180000);
        requestFactory.setReadTimeout(180000);

        restTempl.setRequestFactory(requestFactory);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", CurrentUser.getAuthToken());

        return restTempl.exchange(url, HttpMethod.PATCH, new HttpEntity<>(request, headers), UserResponse.class);
    }

    public ResponseEntity<String> getTokenForChangingEmail(GetTokenForUpdateEmailRequest request) {
        var url = serverURL + "users/me/email";
        RestTemplate restTempl = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", CurrentUser.getAuthToken());

        return restTempl.exchange(url, HttpMethod.POST, new HttpEntity<>(request, headers), String.class);
    }

    public ResponseEntity<String> changeEmail(ChangeEmailRequest request) {
        var url = serverURL + "users/me/email/change";
        RestTemplate restTempl = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", CurrentUser.getAuthToken());

        return restTempl.exchange(url, HttpMethod.POST, new HttpEntity<>(request, headers), String.class);
    }
}