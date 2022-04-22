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
    private final Parser parser = new Parser();

    public String date() {
        ZonedDateTime now = ZonedDateTime.now();
        return DateTimeFormatter.RFC_1123_DATE_TIME.format(now);
    }

    public int size(String str) {
        return str.length();
    }


    public String bodyMake(List<String> result,String clinetIp) throws JsonProcessingException {
        String path = result.get(0).split(" ")[1];
        String host = result.get(1).split(" ")[1];
        ObjectNode node = mapper.createObjectNode();
        int headerSize = result.indexOf("{");

        if(path.contains("/ip")){
            node.put("origin", clinetIp);
        }
        if(path.contains("/get")) {
            node.putPOJO("args", parser.argParser(path));
            node.putPOJO("headers", parser.getHeaders(result, result.size()));
            node.put("origin", clinetIp);
            node.put("url", host + path);
        }
        if(path.contains("/post")){
            String data= "";//result.get(result.size()-1);
            node.putPOJO("args", parser.argParser(path));
            node.put("data",data);
            node.putPOJO("files","");
            node.putPOJO("form","");
            node.putPOJO("headers", parser.getHeaders(result,headerSize));
            node.putPOJO("json",parser.jsonParser(data));
            node.put("origin", clinetIp);
            node.put("url", host + path);
        }


        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
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

            String line =  br.readLine();
            List<String> result = new ArrayList<>();
            String disconnet = "";
            System.out.println(line);
            if(line.contains("POST"))
                disconnet ="}\n";
            result.add(line);
            while ((line = br.readLine()) != null) {
                result.add(line);
                System.out.println(line);
                if (line.equals(disconnet))  break;
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