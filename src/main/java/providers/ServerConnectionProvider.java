package providers;

import com.google.gson.reflect.TypeToken;
import data.CurrentUser;
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
    private static RestTemplate restTempl = new RestTemplate();
    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

    public static final String serverURL = "https://xettlena.herokuapp.com/"; //"http://localhost:8080/";

    public static ServerConnectionProvider getInstance() {
        if (instance == null) instance = new ServerConnectionProvider();
        return instance;
    }

    private ServerConnectionProvider() {
    }

    public ResponseEntity<SignupResponse> signUpRequest(SignupRequest requestEntity) {
        var url = serverURL + "users";
        return restTempl.postForEntity(url, requestEntity, SignupResponse.class);
    }

    public ResponseEntity<String> loginRequest(LoginRequest requestEntity) {
        var url = serverURL + "login";
        return restTempl.postForEntity(url, requestEntity, String.class);
    }

    public ResponseEntity<ChangePasswordResponse> getToken(UserRequest requestEntity) {
        var url = serverURL + "password";
        return restTempl.postForEntity(url, requestEntity, ChangePasswordResponse.class);
    }

    public ResponseEntity<String> changePassword(ChangePasswordRequest requestEntity) {
        var url = serverURL + "password/change";
        return restTempl.postForEntity(url, requestEntity, String.class);
    }

    public ResponseEntity<List<ChatResponse>> getUserChats() {
        var url = serverURL + "users/me/chats";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", CurrentUser.getAuthToken());
        Type listType = new TypeToken<List<ChatResponse>>() {
        }.getType();

        return restTempl.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<List<ChatResponse>>() {
        });
    }

    public ResponseEntity<ChatResponse> addChat(UserRequest request) {
        var url = serverURL + "users/me/chats";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", CurrentUser.getAuthToken());

        return restTempl.exchange(url, HttpMethod.POST, new HttpEntity<>(request, headers), ChatResponse.class);
    }

    public ResponseEntity<MessageResponse> sendMessage(SendMessageRequest request) {
        var url = serverURL + "users/me/messages";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", CurrentUser.getAuthToken());

        return restTempl.exchange(url, HttpMethod.POST, new HttpEntity<>(request, headers), MessageResponse.class);
    }

    public ResponseEntity<List<MessageResponse>> getChat(UserRequest request) {
        var url = serverURL + "users/me/chat";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", CurrentUser.getAuthToken());
        Type listType = new TypeToken<List<MessageResponse>>() {
        }.getType();

        return restTempl.exchange(url, HttpMethod.POST, new HttpEntity<>(request, headers), new ParameterizedTypeReference<List<MessageResponse>>() {
        });
    }

    public ResponseEntity<UserResponse> getUserInfo() {
        var url = serverURL + "users/me";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", CurrentUser.getAuthToken());

        return restTempl.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), UserResponse.class);
    }

    public ResponseEntity<UserResponse> updateUserPassword(UpdateUserPasswordRequest request){
        var url = serverURL + "users/me/password";

        requestFactory.setConnectTimeout(180000);
        requestFactory.setReadTimeout(180000);

        restTempl.setRequestFactory(requestFactory);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", CurrentUser.getAuthToken());

        return restTempl.exchange(url, HttpMethod.PATCH, new HttpEntity<>(request, headers), UserResponse.class);
    }

    public ResponseEntity<UserResponse> updateUserLogin(UpdateUserLoginRequest request){
        var url = serverURL + "users/me/login";

        requestFactory.setConnectTimeout(180000);
        requestFactory.setReadTimeout(180000);

        restTempl.setRequestFactory(requestFactory);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", CurrentUser.getAuthToken());

        return restTempl.exchange(url, HttpMethod.PATCH, new HttpEntity<>(request, headers), UserResponse.class);
    }

    public ResponseEntity<String> getTokenForChangingEmail(GetTokenForUpdateEmailRequest request) {
        var url = serverURL + "users/me/email";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", CurrentUser.getAuthToken());

        return restTempl.exchange(url, HttpMethod.POST, new HttpEntity<>(request, headers), String.class);
    }

    public ResponseEntity<String> changeEmail(ChangeEmailRequest request) {
        var url = serverURL + "users/me/email/change";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", CurrentUser.getAuthToken());

        return restTempl.exchange(url, HttpMethod.POST, new HttpEntity<>(request, headers), String.class);
    }

    public ResponseEntity<String> deleteAccount() {
        var url = serverURL + "users/me";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", CurrentUser.getAuthToken());

        return restTempl.exchange(url, HttpMethod.DELETE, new HttpEntity<>(headers), String.class);
    }

    public ResponseEntity<FindUserResponse> findUser(UserRequest request) {
        var url = serverURL + "users/find";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", CurrentUser.getAuthToken());

        return restTempl.exchange(url, HttpMethod.POST, new HttpEntity<>(request, headers), FindUserResponse.class);
    }
}