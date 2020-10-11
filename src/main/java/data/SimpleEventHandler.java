package data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.MessageEvent;
import controller.impl.RetrieveMessage;
import javafx.application.Platform;
import massage.Message;
import response.MessageResponse;

import java.lang.reflect.Type;
import java.sql.Date;
import java.time.Instant;
import java.util.List;

public class SimpleEventHandler implements EventHandler {

    public RetrieveMessage retrieveMessage;

    public void setRetrieveMessage(RetrieveMessage retrieveMessage) {
        this.retrieveMessage = retrieveMessage;
    }


    @Override
    public void onOpen() throws Exception {
        System.out.println("onOpen");
    }

    @Override
    public void onClosed() throws Exception {
        System.out.println("onClosed");
    }

    @Override
    public void onMessage(String event, MessageEvent messageEvent) throws Exception {
        messageEvent.getData();
        Gson gson = new Gson();
        Type listType = new TypeToken<List<MessageResponse>>(){}.getType();
        List<MessageResponse> responses = gson.fromJson( messageEvent.getData(), listType);
        retrieveMessage.retrieve(responses);
    }

    @Override
    public void onComment(String comment) throws Exception {
        System.out.println("onComment");
    }

    @Override
    public void onError(Throwable t) {
        System.out.println("onError");
    }

}