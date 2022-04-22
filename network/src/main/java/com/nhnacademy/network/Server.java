package com.nhnacademy.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
<<<<<<< HEAD
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
=======

import java.io.PrintStream;

import java.net.ServerSocket;
import java.net.Socket;

>>>>>>> 54255bb61617f3dc2a0f4b242911cc337f3330ff
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public String date() {
        ZonedDateTime now = ZonedDateTime.now();
        return DateTimeFormatter.RFC_1123_DATE_TIME.format(now);
    }

    public int size(String str) {
        return str.length();
    }
<<<<<<< HEAD

=======
>>>>>>> 54255bb61617f3dc2a0f4b242911cc337f3330ff
    //    public String json(List <String> jsonData){
//        String temp ="";
//        for (String data:jsonData) {
//            temp +=
//        }
//        return "";
//    }
    public String bodyMake(String path) {
<<<<<<< HEAD
        String temp = "";
        switch (path) {
            case "/ip":
                String ip = "1.253.143.122";
                temp = "{\"origin\": \"" + ip + "\"\n }";
=======
        String temp ="";
        switch(path){
            case "/get":
                String ip ="1.253.143.122";
                temp ="{\n" +
                    "  \"origin\": \"103.243.200.16\"\n" +
                    "}";
>>>>>>> 54255bb61617f3dc2a0f4b242911cc337f3330ff
                return temp;
        }
        return temp;
    }

    public String getMake(String path) {

        System.out.println(path);
        path = path.replace("\r\n", "");
        String host = path.split("Host: ")[1].split("User-Agent")[0];
        // replace(), split(),

        String result = "";
        result += "{\n";
        result += "\"args\": {},\n";
        result += "  \"headers\": {\n";
        result += " \"Accept\": \"*/*\",";
        result += "\"Host\": \"" + host + "\",\n";
        result += "\"User-Agent\": \"curl/7.64.1\"\n";
        result += " },\n";
        result += " \"origin\": \"1.253.143.122\",\n";
        result += " \"url\": \"http://" + host + "/get\"\n";
        result += "}\n";

        return result;
    }

    public String getMakeMessage(String path) {

        System.out.println(path);
        path = path.replace("\r\n", "");
        String host = path.split("Host: ")[1].split("User-Agent")[0];
        String data = path.split("get\\?")[1].split(" HTTP")[0];
        String key = data.split("=")[0];
        String value = data.split("=")[1];

        // replace(), split(),

        String result = "";
        result += "{\n" +
            "  \"args\": {\n" +
            "    \"" + key + "\": \"" + value + "\"\n" +
            "  },";
        result += "  \"headers\": {\n";
        result += " \"Accept\": \"*/*\",";
        result += "\"Host\": \"" + host + "\",\n";
        result += "\"User-Agent\": \"curl/7.64.1\"\n";
        result += " },\n";
        result += " \"origin\": \"1.253.143.122\",\n";
        result += " \"url\": \"http://" + host + "/get\"\n";
        result += "}\n";

        return result;
    }

    public String getMakeMessages(String path) {
        path = path.replace("\r\n", "");
        String host = path.split("Host: ")[1].split("User-Agent")[0];
        String[] data = path.split("get\\?")[1].split(" HTTP")[0].split("&");

        String result = "";
        result += "{\n" +
            "  \"args\": {\n";

        for (int i = 0; i < data.length; i++) {
            String key = data[i].split("=")[0];
            String value = data[i].split("=")[1];
            result += "    \"" + key + "\": \"" + value + "\"\n";
        }
        result += "  },";
        result += "  \"headers\": {\n";
        result += " \"Accept\": \"*/*\",";
        result += "\"Host\": \"" + host + "\",\n";
        result += "\"User-Agent\": \"curl/7.64.1\"\n";
        result += " },\n";
        result += " \"origin\": \"1.253.143.122\",\n";
        result += " \"url\": \"http://" + host + "/get\"\n";
        result += "}\n";

        return result;
    }

    public void connect() {
        Server server = new Server();

        try (ServerSocket serverSocket = new ServerSocket(80)) {
            Socket socket = serverSocket.accept();
            InputStream in = socket.getInputStream();
            byte[] bytes = null;
            String message = null;
            bytes = new byte[100];
            int readByteCount = in.read(bytes);
            message = new String(bytes, 0, readByteCount, "UTF-8");
            // 2-1
            String messageResult = getMake(message);
            //System.out.println(messageResult);
            // 2-2
            //System.out.println(getMakeMessage(message));
            // 2-3
            //System.out.println(getMakeMessages(message));


            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            OutputStream out = socket.getOutputStream();
            PrintStream ps = new PrintStream(out);

            String line = null;
            List<String> result = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                result.add(line);
<<<<<<< HEAD
                System.out.println(line);
                if (line.equals("")) {
                    break;
                }
=======
                if (line.equals("")) break;
>>>>>>> 54255bb61617f3dc2a0f4b242911cc337f3330ff
            }
            ////////////////////



            String[] splitLine = result.get(0).split(" ");

            String body =
<<<<<<< HEAD
                splitLine[2] + " 200 OK\n" + "Date: " + server.date() + "\n" +
                    "Content-Type: application/json\n" +
                    "Content-Length: " + server.size(server.getMake(splitLine[1])) + "\n" +
=======
                splitLine[2] + " 200 OK\n" + "Date: " + server.date() +"\n" + "Content-Type: application/json\n" +
                    "Content-Length: " + server.size(server.bodyMake(splitLine[1])) + "\n" +
>>>>>>> 54255bb61617f3dc2a0f4b242911cc337f3330ff
                    "Connection: keep-alive\n" +
                    "Server: gunicorn/19.9.0\n" +
                    "Access-Control-Allow-Origin: *\n" +
                    "Access-Control-Allow-Credentials: true\n\n" + server.getMake(splitLine[1]) +
                    "\n";

            System.out.println(body);

            System.out.println(body);

            ps.print(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server s = new Server();
        s.connect();
    }
}