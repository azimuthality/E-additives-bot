package com.example.eadditivesbot.service;

import com.example.eadditivesbot.config.BotConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.concurrent.Immutable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Component
public class BotService extends TelegramLongPollingBot {
    final BotConfig config;
    public BotService(BotConfig config){
        this.config = config;
    }

    @Override
    public String getBotUsername(){
        return config.getBotName();
    }
    @Override
    public String getBotToken(){
        return config.getToken();

    }
    @Override
    public void onUpdateReceived(Update update)  {

        if(update.hasMessage() && update.getMessage().hasText()){
            String messaheText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (messaheText){
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                default:
                    getByCode(messaheText, chatId);


            }
        }
    }
    private void getByCode(String code, long chatId) {
        System.out.println("text");
        String uri = "http://localhost:8080/code?code=" + code;
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);
        sendMessage(chatId, result);
    }
    private void startCommandReceived(long chatId, String name){
        String answer = "Hi, " + name +", nice to meet you!";
        sendMessage(chatId, answer);
    }
    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.enableHtml(true);
        message.setChatId(String.valueOf(chatId));

        //String s = "<b>" + 1 + "</b>";
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {

        }
    }
}
