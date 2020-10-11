package controller.impl;

import response.MessageResponse;

import java.util.List;

public interface RetrieveMessage {
    void retrieve(List<MessageResponse> responses);
}
