package com.example.bank;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class BankService {

    @Autowired
    BankRepository bankRepository;


    @Autowired
    LedgerRepository ledgerRepository;

    @Autowired
    BalanceRepository balanceRepository;

   /* @RabbitListener(queues="bank" )
    @SendTo("reply_queue")
    public  boolean addBalance(Double money,Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        System.out.println("Got request "+ money);
        Bank bank =new Bank();
        bank.setId(UUID.randomUUID());
        bank.setBalance(money);
        bankRepository.save(bank);
        channel.basicAck(tag,false);
        System.out.println("Sent response .");
        return true;

    }*/
    public List post(Bank bank){
        bankRepository.save(bank);
        return bankRepository.findAll();
    }

    @RabbitListener(queues="postBank" )
    @SendTo("reply_queue")
    public  String  postBank(String str,Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        ObjectMapper objectMapper= new ObjectMapper();
        Bank bank=objectMapper.readValue(str,Bank.class);
        System.out.println("Got request to post "+ bank);;
        bankRepository.save(bank);
        //channel.basicAck(tag,false);
        System.out.println("Sent response from post.");
        return  str;

    }

    @RabbitListener(queues="updateBank" )
    @SendTo("reply_queue")
    public  String  updateBank(String str,Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        ObjectMapper objectMapper= new ObjectMapper();
        Bank bank=objectMapper.readValue(str,Bank.class);
        System.out.println("Got request to update"+ bank);;
        //bankRepository.deleteById(bank.getId());
        bankRepository.save(bank);
        //channel.basicAck(tag,false);
        System.out.println("Sent response from update .");
        return  str;

    }

    @RabbitListener(queues="postLedgerUpdateBalance" )
    @SendTo("reply_queue")
    public  String  insertLedgerUpdateBalance(String str,Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        ObjectMapper objectMapper= new ObjectMapper();
        Ledger ledger=objectMapper.readValue(str,Ledger.class);
        System.out.println("Got request to insert ledger"+ ledger);;
        ledgerRepository.save(ledger);
        Balance balance= objectMapper.readValue(str,Balance.class);
        balanceRepository.save(balance);
        //channel.basicAck(tag,false);
        System.out.println("Sent response");
        return  str;

    }








}
