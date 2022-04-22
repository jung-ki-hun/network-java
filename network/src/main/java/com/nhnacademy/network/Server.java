package com.nhnacademy.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.io.PrintStream;

import java.net.ServerSocket;
import java.net.Socket;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private final static ObjectMapper mapper = new ObjectMapper();

    public String date() {
        ZonedDateTime now = ZonedDateTime.now();
        return DateTimeFormatter.RFC_1123_DATE_TIME.format(now);
    }

    public int size(String str) {
        return str.length();
    }

    //    public String json(List <String> jsonData){
//        String temp ="";
//        for (String data:jsonData) {
//            temp +=
//        }
//        return "";
//    }
    public String bodyMake(List<String> result,String clinetIp) throws JsonProcessingException {
        String path = result.get(0).split(" ")[1];
        String host = result.get(1).split(" ")[1];
        ObjectNode node = mapper.createObjectNode();

        switch (path) {
            case "/ip":
                node.put("origin", clinetIp);
                break;
            case "/get":
                node.put("args", "");
                node.putPOJO("headers", getHeaders(result));
                node.put("origin", clinetIp);
                node.put("url", host+path);
//                temp = "{\n" +
//                    "  \"args\": {},\n" +
//                    "  \"headers\": {\n" +
//                    getHeaders(result) +
//                    "  },\n" +
//                    "  \"origin\": \"103.243.200.16\"\n" +
//                    "  \"url\": \"" + host + path + "\"\n" +
//                    "}";
                break;
        }
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
    }

    public static ObjectNode getHeaders(List<String> result){
        ObjectNode node = mapper.createObjectNode();
        int resultLength = result.size() - 1;
        for (int i = 1; i < resultLength; i++) {
            String[] split = result.get(i).split(": ");
            node.put(split[0], split[1]);
        }
        return node;
    }

    public void connect() {
        Server server = new Server();

        try (ServerSocket serverSocket = new ServerSocket(3000)) {
            Socket clientSocket = serverSocket.accept();
            InputStream in = clientSocket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            OutputStream out = clientSocket.getOutputStream();
            PrintStream ps = new PrintStream(out);

            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.createObjectNode();

            String line = null;
            List<String> result = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                result.add(line);
                if (line.equals("")) break;
            }
            ////////////////////

            String[] splitLine = result.get(0).split(" ");
            String clientIpTemp = clientSocket.getRemoteSocketAddress().toString().replace("/","");
            String [] clientIp = clientIpTemp.split(":");
            String body =
                splitLine[2] + " 200 OK\n" + "Date: " + server.date() +"\n" + "Content-Type: application/json\n" +
                    "Content-Length: " + server.size(server.bodyMake(result,clientIp[0])) + "\n" +
                    "Connection: keep-alive\n" +
                    "Server: gunicorn/19.9.0\n" +
                    "Access-Control-Allow-Origin: *\n" +
                    "Access-Control-Allow-Credentials: true\n\n" + server.bodyMake(result,clientIp[0])+"\n";

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