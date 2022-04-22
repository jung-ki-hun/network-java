package com.nhnacademy.network;

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
    public String date() {
        ZonedDateTime now = ZonedDateTime.now();
        return DateTimeFormatter.RFC_1123_DATE_TIME.format(now);
    }
    public int size(String str){
        return str.length();
    }
    //    public String json(List <String> jsonData){
//        String temp ="";
//        for (String data:jsonData) {
//            temp +=
//        }
//        return "";
//    }
    public String bodyMake(String path,List<String> require) {
        String temp ="";
        String ip ="1.253.143.122";
        switch(path){
            case "/ip":
                temp ="{\n" +
                    "  \"origin\": \"103.243.200.16\"\n" +
                    "}";
                return temp;
            case "/get":

            return temp;
        }
        return temp;
    }

    public void connect() {
        Server server = new Server();

        try (ServerSocket serverSocket = new ServerSocket(80)) {
            System.out.println("bye");
            Socket socket = serverSocket.accept();
            System.out.println("hi");
            InputStream in = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            OutputStream out = socket.getOutputStream();
            PrintStream ps = new PrintStream(out);

            String line = null;
            List<String> result = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                result.add(line);
                if (line.equals("")) break;
            }
            ////////////////////

            String[] splitLine = result.get(0).split(" ");
            String body="";
            if(splitLine[1] == "/ip") {
                body =
                    splitLine[2] + " 200 OK\n" + "Date: " + server.date() + "\n" +
                        "Content-Type: application/json\n" +
                        "Content-Length: " + server.size(server.bodyMake(splitLine[1],result) + "\n" +
                        "Connection: keep-alive\n" +
                        "Server: gunicorn/19.9.0\n" +
                        "Access-Control-Allow-Origin: *\n" +
                        "Access-Control-Allow-Credentials: true\n\n" +
                        server.bodyMake(splitLine[1],result) + "\n";

                System.out.println(body);
            }
            if(splitLine[1]== "/get"){
                body = server.bodyMake(splitLine[1],result);
            }
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