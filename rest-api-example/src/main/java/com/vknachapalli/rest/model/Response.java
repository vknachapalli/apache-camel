package com.vknachapalli.rest.model;

import java.util.List;

public class Response {
    private String output;
    private List<Message> messages;

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "Response{" +
                "output='" + output + '\'' +
                ", messages=" + messages +
                '}';
    }
}
