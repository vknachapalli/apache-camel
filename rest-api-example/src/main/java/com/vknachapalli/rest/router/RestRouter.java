package com.vknachapalli.rest.router;

import com.vknachapalli.rest.model.Customer;
import com.vknachapalli.rest.model.Request;
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
        restConfiguration().component("servlet").bindingMode(RestBindingMode.auto);

        rest()
                .path("/api")
                .consumes(MediaType.APPLICATION_JSON_VALUE)
                .produces(MediaType.APPLICATION_JSON_VALUE)
                .get("/customers")
                .type(Customer.class)
                .route()
                .log("Expect No Body, Original Body: ${body}")
                .to("sql:SELECT * FROM customer")
                .endRest();

        rest()
                .path("/api")
                .consumes(MediaType.APPLICATION_JSON_VALUE)
                .produces(MediaType.APPLICATION_JSON_VALUE)
                .post("/customers")
                .type(Request.class)
                .route()
                .log("Expect Body, Original Body: ${body}")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        Request request = exchange.getMessage().getBody(Request.class);
                        exchange.getMessage().setHeader("cid", request.getCustomer().getId());
                    }
                })
                .log("After Process")
                .log("${body}")
                .log("${header.cid}")
                .to("sql:select * from customer where id= :#${header.cid}")
                .endRest();
    }
}
