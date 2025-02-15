//package com.liqaa.server.util;
//
//import com.sun.net.httpserver.Request;
//import jakarta.xml.bind.JAXBContext;
//import jakarta.xml.bind.Unmarshaller;
//import okhttp3.*;
//import org.json.JSONArray;
//import org.json.JSONObject;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.concurrent.TimeUnit;
//
//
//
//public class GeminiAPI<APIConfig> {
//    private static final String CONFIG_FILE = "/gemini-api.xml";
//    private static final OkHttpClient client = new OkHttpClient.Builder()
//            .connectTimeout(10, TimeUnit.SECONDS)
//            .readTimeout(30, TimeUnit.SECONDS)
//            .build();
//
//    private static String apiUrl;
//
//    static {
//        try (InputStream is = GeminiAPI.class.getResourceAsStream(CONFIG_FILE)) {
//            JAXBContext context = JAXBContext.newInstance(APIConfig.class);
//            Unmarshaller unmarshaller = context.createUnmarshaller();
//            APIConfig config = (APIConfig) unmarshaller.unmarshal(is);
//            apiUrl = config.getUrl() + config.getKey();
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to load API config", e);
//        }
//    }
//
//    public static String getChatResponse(String userMessage) {
//        try {
//            JSONObject content = new JSONObject()
//                    .put("role", "user")
//                    .put("parts", new JSONArray()
//                            .put(new JSONObject()
//                                    .put("text", userMessage)));
//
//            JSONObject requestBody = new JSONObject()
//                    .put("contents", new JSONArray().put(content));
//
//            Request request = new Request.Builder()
//                    .url(apiUrl)
//                    .post(RequestBody.create(
//                            requestBody.toString(),
//                            MediaType.get("application/json; charset=utf-8")
//                    ))
//                    .build();
//
//            try (Response response = client.newCall(request).execute()) {
//                if (!response.isSuccessful()) {
//                    System.err.println("API Error: " + response.code() + " - " + response.message());
//                    return "I'm having trouble responding right now.";
//                }
//
//                JSONObject responseBody = new JSONObject(response.body().string());
//                return extractResponseText(responseBody);
//            }
//        } catch (IOException e) {
//            System.err.println("API Communication Error: " + e.getMessage());
//            return "Sorry, I can't connect to the chatbot service.";
//        }
//    }
//
//    private static String extractResponseText(JSONObject response) {
//        try {
//            return response.getJSONArray("candidates")
//                    .getJSONObject(0)
//                    .getJSONObject("content")
//                    .getJSONArray("parts")
//                    .getJSONObject(0)
//                    .getString("text");
//        } catch (Exception e) {
//            System.err.println("Failed to parse API response: " + e.getMessage());
//            return "I didn't understand that response.";
//        }
//    }
//}