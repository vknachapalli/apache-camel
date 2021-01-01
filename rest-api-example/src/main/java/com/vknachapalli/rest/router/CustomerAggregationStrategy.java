package com.vknachapalli.rest.router;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomerAggregationStrategy implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (oldExchange == null) {
            return newExchange;
        }
        List<LinkedCaseInsensitiveMap<Object>> body = (ArrayList) newExchange.getIn().getBody();
        List<LinkedCaseInsensitiveMap<Object>> existing = (ArrayList) oldExchange.getIn().getBody();
        oldExchange.getIn().setBody(Stream.concat(existing.stream(), body.stream()).collect(Collectors.toList()));
        return oldExchange;
    }
}
