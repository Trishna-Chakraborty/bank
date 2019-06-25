package com.example.bank;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class BankApplication {
    @Bean
    Queue postBankQueue() {

        Map<String, Object> args = new HashMap<String, Object>();
        args.put("x-dead-letter-exchange", "dead_exchange");
        args.put("x-message-ttl", 60000);
        //args.put("x-dead-letter-routing-key","post.#");
        return new Queue("postBank", false, false, false, args);
    }


    @Bean
    Queue updateBankQueue() {

        Map<String, Object> args = new HashMap<String, Object>();
        args.put("x-dead-letter-exchange", "dead_exchange");
        args.put("x-message-ttl", 60000);
        //args.put("x-dead-letter-routing-key","post.#");
        return new Queue("updateBank", false, false, false, args);
    }

    @Bean
    DirectExchange bankExchange() {
        return new DirectExchange("bank_exchange");
    }

    @Bean
    Binding postBankBinding(Queue postBankQueue, DirectExchange bankExchange) {
        return BindingBuilder.bind(postBankQueue).to(bankExchange).with("post.bank");
    }
    @Bean
    Binding updateBankBinding(Queue updateBankQueue, DirectExchange bankExchange) {
        return BindingBuilder.bind(updateBankQueue).to(bankExchange).with("update.bank");
    }
    public static void main(String[] args) {
        SpringApplication.run(BankApplication.class, args);
    }

}
