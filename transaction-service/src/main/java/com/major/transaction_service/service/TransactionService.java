package com.major.transaction_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.major.transaction_service.dao.TransactionDao;
import com.major.transaction_service.dao.WalletInterface;
import com.major.transaction_service.dto.TransactionCreateRequest;
import com.major.transaction_service.model.Transaction;
import com.major.transaction_service.model.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.json.simple.JSONObject;

import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    TransactionDao transactionDao;

    @Autowired
    WalletInterface walletInterface;

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();

    public String transact(TransactionCreateRequest transactionCreateRequest, String senderId) throws JsonProcessingException {
        Transaction transaction = Transaction.builder()
                .senderId(senderId)
                .receiverId(transactionCreateRequest.getReceiverId())
                .reason(transactionCreateRequest.getReason())
                .amount(transactionCreateRequest.getAmount())
                .transactionStatus(TransactionStatus.PENDING)
                .externalTransactionId(UUID.randomUUID().toString())
                                            .build();

        transactionDao.save(transaction);

        TransactionStatus transactionStatus = null;

        //update wallet using an endpoint
        ResponseEntity<String> responseEntity = this.walletInterface.updateWallet(transaction.getSenderId(),
                                                                                    transaction.getReceiverId(),
                                                                                    transactionCreateRequest.getAmount());

        if(responseEntity.getStatusCode().is5xxServerError()) {
            //FAILED TRANSACTION
            transactionStatus = TransactionStatus.FAILURE;
        } else {
            transactionStatus = TransactionStatus.SUCCESSFUL;
        }

        transactionDao.updateTransaction(transactionStatus, transaction.getExternalTransactionId());

        String senderEmail = "m.desh6541@hotmail.com";
        String receiverEmail = "m.desh6541@hotmail.com";

        //TODO - send notification using notification service
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("transactionId", transaction.getExternalTransactionId());
        jsonObject.put("transactionStatus", transactionStatus);
        jsonObject.put("amount", transaction.getAmount());
        jsonObject.put("senderEmail", senderEmail);
        jsonObject.put("receiverEmail", receiverEmail);

        this.kafkaTemplate.send("transaction_completed", objectMapper.writeValueAsString(jsonObject));
        return transaction.getExternalTransactionId();
    }
}
