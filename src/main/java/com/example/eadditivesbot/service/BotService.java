package com.example.eadditivesbot.service;

import com.example.eadditivesbot.config.BotConfig;
import org.apache.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.nio.charset.Charset;

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
    public void onUpdateReceived(Update update) {

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
    private void getByCode(String code, long chatId){
        String uri = "http://localhost:8080/code?code=" + code;
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);

        String uri_ = "http://localhost:3000";
        RestTemplate restTemplate_ = new RestTemplate();
        restTemplate_.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        String result_ = restTemplate_.postForObject(uri_, result, String.class);
        sendMessage(chatId, result_);
    }
    private void startCommandReceived(long chatId, String name){
        String answer = "Hi, " + name +", nice to meet you!";
        sendMessage(chatId, answer);
    }
    private void sendMessage(long chatId, String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        message.enableHtml(true);

        try{
            execute(message);
        }
        catch (TelegramApiException e){

        }
    }
}
