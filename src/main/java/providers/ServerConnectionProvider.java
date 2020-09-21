package providers;

import data.ServerArgument;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ServerConnectionProvider<T> {
    public static String ServerURL = "http://localhost:8080";

    public T sendRequest(String serverFunction , List<ServerArgument> arguments , RequestType type) throws IOException {
        if(!ServerURL.isBlank()) {

            StringBuffer url = new StringBuffer();
            url.append(ServerURL);
            url.append("/");
            url.append(serverFunction);
            url.append("?");

            for(int i = 0 ; i < arguments.size() ; i++){
                url.append(arguments.get(i).ToServerStyleString());
                if(i + 1 < arguments.size()){
                    url.append("&");
                }
            }

            URL obj = new URL(url.toString());

            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod(type.getType());

            ResponseEtity<T> restTemplate = new ResponseEtity<T>();

            T result = restTemplate.getForObject(obj, T);
            System.out.println(result);

            return answer.toString();
        }else{
            throw new IOException("The server url was not found.");
            //TODO : logger
        }
    }
}
