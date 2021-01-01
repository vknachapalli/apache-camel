package com.vknachapalli.rest.router;

import com.vknachapalli.rest.model.Customer;
import com.vknachapalli.rest.model.Request;
import com.vknachapalli.rest.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
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
                .split().simple("${body.customers}").aggregationStrategy(new CustomerAggregationStrategy())
                .log("Expect Body, Body in parts : ${body}")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        Customer customer = exchange.getMessage().getBody(Customer.class);
                        exchange.getMessage().setHeader("cid", customer.getId());
                    }
                })
                .to("sql:select * from customer where id= :#${header.cid}")
                .log("Output: ${body}")
                .endRest();
    }
}

/*
* .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        List<Customer> customerList = new ArrayList<>();
                        List<Map<String, String>> dataTable = (ArrayList<Map<String, String>>) exchange.getMessage().getBody();
                        dataTable.forEach(item -> {
                            Customer customer = new Customer();
                            customer.setId(Long.valueOf(item.get("id")));
                            customer.setName(item.get("name"));
                            customer.setCreatedDateTime(Instant.parse(item.get("created_timestamp")));
                            customerList.add(customer);
                        });
                        Response response = new Response();
                        response.setCustomers(customerList);
                        exchange.getMessage().setBody(response);
                    }
                })
* */