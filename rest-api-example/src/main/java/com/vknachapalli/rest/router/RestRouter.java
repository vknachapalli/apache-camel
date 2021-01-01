package com.vknachapalli.rest.router;

import com.vknachapalli.rest.model.Message;
import com.vknachapalli.rest.model.Request;
import com.vknachapalli.rest.model.Response;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class RestRouter extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.auto);

        rest()
                .path("/api")
                .consumes(MediaType.APPLICATION_JSON_VALUE)
                .produces(MediaType.APPLICATION_JSON_VALUE)
                .post()
                .type(Request.class)
                .route()
                .log("Original Body: ${body}")
                .split().simple("${body.getMessages}")
                .log("Body parts: ${body}")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        Message message = exchange.getMessage().getBody(Message.class);
                        message.setMessage("Message is " + message.getMessage());
                    }
                })
                .end()
                .log("Aggregate ${body}")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        Request request = exchange.getMessage().getBody(Request.class);
                        Response response = new Response();
                        response.setOutput("Thanks for submitting your request with input: '" + request.getInput() + "'");
                        response.setMessages(request.getMessages());
                        exchange.getMessage().setBody(response);
                    }
                })
                .endRest();
    }
}
