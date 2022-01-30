package com.example.study_spring_batch;

import org.springframework.batch.item.ItemProcessor;

/* CunckConfiguration2 참조
 *  22.1.30
 * */

public class CustomItemProcessor implements ItemProcessor<Customer, Customer> {

    @Override
    public Customer process(Customer customer) throws Exception {

        customer.setName(customer.getName().toUpperCase());
        return customer;
    }
}
