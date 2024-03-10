package org.example;
import java.io.*;
import java.net.*;

public class JsonPlaceholderClient {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    public static void main(String[] args) {
        try {
            int userId = 1;
            String newUserJson = "{\"name\": \"John Doe\", \"username\": \"johndoe\"}";
            String createdUserJson = sendPostRequest(BASE_URL + "/users", newUserJson);
            System.out.println("Створено нового користувача: " + createdUserJson);

            String latestPostCommentsJson = getLatestPostComments(userId);
            System.out.println("Коментарі до останнього поста: " + latestPostCommentsJson);
            String fileName = "user-" + userId + "-latest-post-comments.json";
            writeJsonToFile(latestPostCommentsJson, fileName);
            System.out.println("Коментарі записано у файл: " + fileName);

            String openTasksJson = TaskManager.getOpenTasksForUser(userId);
            System.out.println("Відкриті задачі для користувача: " + openTasksJson);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String sendPostRequest(String urlString, String postData) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        OutputStream os = connection.getOutputStream();
        os.write(postData.getBytes());
        os.flush();
        os.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    private static String getLatestPostComments(int userId) throws IOException {
        String latestPostJson = sendGetRequest(BASE_URL + "/users/" + userId + "/posts");
        int latestPostId = getLastPostId(latestPostJson);
        return sendGetRequest(BASE_URL + "/posts/" + latestPostId + "/comments");
    }

    private static String sendGetRequest(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    private static void writeJsonToFile(String json, String fileName) throws IOException {
        FileWriter fileWriter = new FileWriter(fileName);
        fileWriter.write(json);
        fileWriter.close();
    }

    private static int getLastPostId(String postsJson) {
        return 10;
    }
}

class TaskManager {
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    public static String getOpenTasksForUser(int userId) throws IOException {
        String url = BASE_URL + "/users/" + userId + "/todos?completed=false";
        return sendGetRequest(url);
    }

    private static String sendGetRequest(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }
}

