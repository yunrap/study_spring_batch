package com.example.study_spring_batch;

import org.springframework.batch.item.ItemWriter;

import java.util.List;

/* CunckConfiguration2 참조
 *  22.1.30
 * */

public class CustomItemWriter implements ItemWriter<Customer> {
    @Override
    public void write(List<? extends Customer> items) throws Exception {
        items.forEach(item -> System.out.println(item));
    }
}
