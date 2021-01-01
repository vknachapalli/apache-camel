package com.vknachapalli.rest.router;

import com.vknachapalli.rest.model.Customer;
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
                .log("Original Body: ${body}")
                .to("sql:SELECT * FROM customer")
                .endRest();
    }
}
