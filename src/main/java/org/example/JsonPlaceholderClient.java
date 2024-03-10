package org.example;
import java.io.*;
import java.net.*;

public class JsonPlaceholderClient {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    public static void main(String[] args) {
        try {
            int userId = 1;
            createNewUser();
            getAndSaveLatestPostComments(userId);
            displayOpenTasksForUser(userId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createNewUser() throws IOException {
        String newUserJson = "{\"name\": \"John Doe\", \"username\": \"johndoe\"}";
        String createdUserJson = sendPostRequest(BASE_URL + "/users", newUserJson);
        System.out.println("Створено нового користувача: " + createdUserJson);
    }

    private static void getAndSaveLatestPostComments(int userId) throws IOException {
        String latestPostCommentsJson = getLatestPostComments(userId);
        System.out.println("Коментарі до останнього поста: " + latestPostCommentsJson);
        String fileName = "user-" + userId + "-latest-post-comments.json";
        writeJsonToFile(latestPostCommentsJson, fileName);
        System.out.println("Коментарі записано у файл: " + fileName);
    }

    private static void displayOpenTasksForUser(int userId) throws IOException {
        String openTasksJson = TaskManager.getOpenTasksForUser(userId);
        System.out.println("Відкриті задачі для користувача: " + openTasksJson);
    }

    private static String sendPostRequest(String urlString, String postData) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            os.write(postData.getBytes());
        }

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            return response.toString();
        }
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

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            return response.toString();
        }
    }

    private static void writeJsonToFile(String json, String fileName) throws IOException {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(json);
        }
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

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            return response.toString();
        }
    }
}
