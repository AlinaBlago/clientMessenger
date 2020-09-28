package providers;

import data.ServerArgument;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import request.LoginRequest;
import request.SignupRequest;
import response.LoginResponse;
import response.SignupResponse;

import java.io.IOException;

import java.util.List;


public class ServerConnectionProvider {
    private static ServerConnectionProvider instance;

    public static final String serverURL = "http://localhost:8080";

    public static ServerConnectionProvider getInstance() {
        if(instance == null) instance = new ServerConnectionProvider();
        return instance;
    }

    private ServerConnectionProvider(){}

    public ResponseEntity loginRequest(String serverFunction, List<ServerArgument> arguments, RequestType type) throws IOException {
        if(!serverURL.isBlank()) {
            String url = createURL(serverURL, serverFunction, arguments);
          //  RestTemplate template = new RestTemplate();
            ClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
            RestTemplate restTemplate = new RestTemplate(factory);
            ResponseEntity result = null;
            try{
                result = restTemplate.getForEntity(url, HttpStatus.class);
            } catch (Exception e){
                //TODO : logger
               e.printStackTrace();
            }
            return result;
        } else{
            throw new IOException("The server url was not found.");
            //TODO : logger
        }
    }

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

    public ResponseEntity<LoginResponse> loginRequest(LoginRequest requestEntity){
        var url = serverURL + "login";
        RestTemplate restTempl = new RestTemplate();
        return restTempl.postForEntity(url, requestEntity, LoginResponse.class);
    }

}
