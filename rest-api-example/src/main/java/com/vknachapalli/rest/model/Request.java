package com.vknachapalli.rest.model;

import java.util.List;

public class Request {
    private List<Customer> customers;

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    @Override
    public String toString() {
        return "Request{" +
                "customers=" + customers +
                '}';
    }
}
