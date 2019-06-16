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
    Queue bankQueue() {

        Map<String, Object> args = new HashMap<String, Object>();
        args.put("x-dead-letter-exchange", "dead_exchange");
        args.put("x-message-ttl", 60000);
        return new Queue("bank", false, false, false, args);
    }

    @Bean
    DirectExchange bankExchange() {
        return new DirectExchange("bank_exchange");
    }

    @Bean
    Binding bankBinding(Queue bankQueue, DirectExchange bankExchange) {
        return BindingBuilder.bind(bankQueue).to(bankExchange).with("");
    }
    public static void main(String[] args) {
        SpringApplication.run(BankApplication.class, args);
    }

}
