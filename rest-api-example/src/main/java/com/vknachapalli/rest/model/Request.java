package com.vknachapalli.rest.model;

import java.util.List;

public class Request {

    private String input;
    private List<Message> messages;

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "Request{" +
                "input='" + input + '\'' +
                ", messages=" + messages +
                '}';
    }
}
