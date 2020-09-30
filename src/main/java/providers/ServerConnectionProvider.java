package providers;

import data.ServerArgument;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import request.*;
import response.ChangePasswordResponse;
import response.JwtResponse;
import response.LoginResponse;
import response.SignupResponse;

import java.util.List;


public class ServerConnectionProvider {
    private static ServerConnectionProvider instance;

    public static final String serverURL = "http://localhost:8080/";

    public static ServerConnectionProvider getInstance() {
        if(instance == null) instance = new ServerConnectionProvider();
        return instance;
    }

    private ServerConnectionProvider(){}

    private String createURL(String serverURL, String serverFunction, List<ServerArgument> arguments){
        StringBuilder url = new StringBuilder();
        url.append(ServerConnectionProvider.serverURL);
        url.append("/");
        url.append(serverFunction);
        url.append("?");

        for(int i = 0 ; i < arguments.size() ; i++){
            url.append(arguments.get(i).toServerStyleString());
            if(i + 1 < arguments.size()){
                url.append("&");
            }
        }
        return url.toString();
    }

    public ResponseEntity<SignupResponse> signUpRequest(SignupRequest requestEntity){
        var url = serverURL + "signUp";
        RestTemplate restTempl = new RestTemplate();
        return restTempl.postForEntity(url, requestEntity, SignupResponse.class);
    }

    public ResponseEntity<JwtResponse> loginRequest(LoginRequest requestEntity){
        var url = serverURL + "login";
        RestTemplate restTempl = new RestTemplate();
        return restTempl.postForEntity(url, requestEntity, JwtResponse.class);
    }

    public ResponseEntity<ChangePasswordResponse> getToken(SendChangePasswordTokenRequest requestEntity){
        var url = serverURL + "sendTokenForChangingPassword";
        RestTemplate restTempl = new RestTemplate();
        return restTempl.postForEntity(url, requestEntity, ChangePasswordResponse.class);
    }

    public ResponseEntity<String> changePassword(ChangePasswordRequest requestEntity){
        var url = serverURL + "submitChangingPassword";
        RestTemplate restTempl = new RestTemplate();
        return restTempl.postForEntity(url, requestEntity, String.class);
    }

    public ResponseEntity<String> getUserChats(JwtResponse requestEntity){
        RestTemplate restTempl = new RestTemplate();
        var url = serverURL + "getUserChats";

        LoginRequest req = new LoginRequest();
        req.setUsername("bestvineco");
        req.setPassword(" ");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJiZXN0dmluZWNvIiwiaWF0IjoxNjAxNTAwNzQ2LCJleHAiOjE2MDE1ODcxNDZ9.Fr-1TDA2glYMfHuDK8VJx__kNrFto82IZ6rbl6DMlMs7r-F7cPE0JBO7562Sn6xVxDRimUJAkAmPtIWWMiAbNQ");

        // create request
        HttpEntity<LoginRequest> request = new HttpEntity(req , headers);



        // make a request
        ResponseEntity<Object> response = new RestTemplate().exchange(url, HttpMethod.GET, request, Object.class);

        // get JSON response
        Object json = response.getBody();
        return null;
    }

}
