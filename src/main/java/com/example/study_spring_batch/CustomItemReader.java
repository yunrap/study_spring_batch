package com.example.study_spring_batch;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.ArrayList;
import java.util.List;

/* CunckConfiguration2 참조
 *  22.1.30
 * */

public class CustomItemReader implements ItemReader<Customer>{

    private List<Customer> list;

    public CustomItemReader(List<Customer> list){
        this.list = new ArrayList<>(list);
    }

    @Override
    public Customer read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        if(!list.isEmpty()){
            return list.remove(0);
        }

        return null;
    }
}
