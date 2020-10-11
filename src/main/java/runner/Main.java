package runner;

import controller.ApplicationController;
import data.CurrentUser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    public static Thread t1;
    Logger logger = LoggerFactory.getLogger(ApplicationController.class);

    @Override
    public void start (Stage stage) throws Exception {
        logger.info("Application start");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/logIn.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root, 620, 680));
        stage.show();
        logger.info("login.xml loaded");
    }

        //TODO
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        StopThread();
    }

    public static void main(String[] args){
        launch(args);
    }

    public static void StopThread(){
        if(t1 != null){
            t1.stop();
        }
    }

}


