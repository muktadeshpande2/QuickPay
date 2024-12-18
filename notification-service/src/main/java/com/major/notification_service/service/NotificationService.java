package com.major.notification_service.service;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.json.simple.parser.ParseException;

import static com.major.notification_service.utils.Constants.TRANSACTION_COMPLETED;

@Service
public class NotificationService {

    @Autowired
    SimpleMailMessage simpleMailMessage;

    @Autowired
    JavaMailSender javaMailSender;

    @KafkaListener(topics = {TRANSACTION_COMPLETED}, groupId = "qp2")
    public void notify(String msg) throws ParseException {
        JSONObject jsonObject = (JSONObject) new JSONParser().parse(msg);
        String transactionId = (String) jsonObject.get("transactionId");
        String transactionStatus = (String) jsonObject.get("transactionStatus");
        Long amount = (Long) jsonObject.get("amount");
        String senderEmail = (String) jsonObject.get("senderEmail");
        String receiverEmail = (String) jsonObject.get("receiverEmail");

        String senderMsg = getSenderMessage(transactionStatus, amount, transactionId);
        String receiverMsg = getReceiverMessage(transactionStatus, amount, senderEmail);


        if(!senderMsg.isEmpty()) {
            simpleMailMessage.setTo(senderEmail);
            simpleMailMessage.setSubject("E-Wallet Transaction Update");
            simpleMailMessage.setFrom("m.desh6541@hotmail.com");
            simpleMailMessage.setText(senderMsg);
            javaMailSender.send(simpleMailMessage);
        }

        if(!receiverMsg.isEmpty()) {
            simpleMailMessage.setTo(receiverEmail);
            simpleMailMessage.setSubject("E-Wallet Transaction Update");
            simpleMailMessage.setFrom("m.desh6541@hotmail.com");
            simpleMailMessage.setText(senderMsg);
            javaMailSender.send(simpleMailMessage);
        }
    }

    private String getSenderMessage(String transactionStatus, Long amount, String transactionId) {
        String msg = " ";

        if(transactionStatus.equals("FAILURE")) {
            msg = "Hi! Your transaction of amount" + amount + " , transactionId = " + transactionId + " has failed";
        } else {
            msg = "Hi! Your account has been debited with amount " + amount + " , transactionId = " + transactionId;
        }

        return msg;
    }

    private String getReceiverMessage(String transactionStatus, Long amount, String senderEmail) {
        String msg = " ";

        if(transactionStatus.equals("SUCCESSFUL")) {
            msg = "Hi! Your account has been credited with amount " + amount + " for the transaction done by the user " + senderEmail;
        }

        return msg;
    }
}
