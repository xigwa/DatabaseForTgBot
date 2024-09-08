package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Bot extends TelegramLongPollingBot {
    private String botUsername;
    private String botToken;
    public Bot() {
        loadBotCredentials("key.txt");
    }

    private void loadBotCredentials(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            this.botUsername = reader.readLine();
            this.botToken = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            long chatID = message.getChatId();
            String getText = message.getText();

            IDB dataBase = DataBase.getInstance();

            if (getText.equals("/load")) {
                ArrayList<String> data = dataBase.loadData();
                StringBuilder mgss = new StringBuilder();
                for (String datum : data) {
                    mgss.append(datum).append("\n");
                }
                sendText(chatID, mgss.toString());
                return;
            }

            // Команда для удаления данных
            if (getText.startsWith("/delete ")) {
                String dataToDelete = getText.replace("/delete ", "");
                dataBase.deleteData(dataToDelete);
                sendText(chatID, "Удалено: " + dataToDelete);
                return;
            }

            // Команда для обновления данных
            if (getText.startsWith("/update ")) {
                String[] parts = getText.replace("/update ", "").split(" ", 2);
                if (parts.length == 2) {
                    String what = parts[0];  // что обновить
                    String to = parts[1];    // на что обновить
                    dataBase.updateData(what, to);
                    sendText(chatID, "Обновлено: " + what + " на " + to);
                } else {
                    sendText(chatID, "Ошибка: неправильный формат команды.");
                }
                return;
            }

            dataBase.saveData(getText);
        }
    }

    public void sendText(Long who, String what){
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString()) //Who are we sending a message to
                .text(what).build();    //Message content
        try {
            execute(sm);                        //Actually sending the message
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);      //Any error will be printed here
        }
    }

}


