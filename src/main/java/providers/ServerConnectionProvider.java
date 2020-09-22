package providers;

import data.ServerArgument;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

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

    public ResponseEntity<Integer> loginRequest(String serverFunction, List<ServerArgument> arguments, RequestType type) throws IOException {
        if(!serverURL.isBlank()) {

            String url = createURL(serverURL, serverFunction, arguments);

            RestTemplate template = new RestTemplate();
            ResponseEntity<Integer> result = null;
            try{
                result = template.getForEntity(url, Integer.class );
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
        StringBuffer url = new StringBuffer();
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

}
