//package com.liqaa.server.controllers.services.implementations;
//
//import com.liqaa.server.util.GeminiAPI;
//import com.liqaa.shared.models.APIConfig;
//
//public class ChatBotService {
//    private static ChatBotService instance;
//    private boolean botEnabled = false;
//
//    private ChatBotService() {}
//
//    public static synchronized ChatBotService getInstance() {
//        if (instance == null) {
//            instance = new ChatBotService();
//        }
//        return instance;
//    }
//
//    public void toggleBot(boolean enabled) {
//        this.botEnabled = enabled;
//    }
//
//    public boolean isBotEnabled() {
//        return botEnabled;
//    }
//
//    public String generateResponse(String userMessage) {
//        if (!botEnabled) return null;
//
//        try {
//            return GeminiAPI.getChatResponse(userMessage);
//        } catch (Exception e) {
//            System.err.println("Chatbot error: " + e.getMessage());
//            return null;
//        }
//    }
//}