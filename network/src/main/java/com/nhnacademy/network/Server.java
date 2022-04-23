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

import java.io.OutputStreamWriter;
import java.io.PrintStream;

import java.net.ServerSocket;
import java.net.Socket;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private final static ObjectMapper mapper = new ObjectMapper();
    private final Parser parser = new Parser();

    public String date() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("GMT"));
        return DateTimeFormatter.RFC_1123_DATE_TIME.format(now);
    }

    public int size(String str) {
        return str.length();
    }
    public String getContentType(List<String> result){

        for (String line: result) {
            if (line.contains("Content-Type"))
                return line.split(": ")[1];
        }return null;
    }

    public String bodyMake(List<String> result, String bodyData, String clinetIp) throws JsonProcessingException {
        String path = result.get(0).split(" ")[1];
        String host = result.get(1).split(" ")[1];
        ObjectNode node = mapper.createObjectNode();
        int headerSize = result.indexOf("{");

        if(path.contains("/ip")){
            node.put("origin", clinetIp);
        }
        if(path.contains("/get")) {
            node.putPOJO("args", parser.argParser(path));
            node.putPOJO("headers", parser.getHeaders(result));
            node.put("origin", clinetIp);
            node.put("url", host + path);
        }
        if(path.contains("/post")){
            //if(getContentType(result).equals("application/json")) {
                node.putPOJO("args", parser.argParser(path));
                node.put("data", bodyData);
                node.putPOJO("files", "");
                node.putPOJO("form", "");
                node.putPOJO("headers", parser.getHeaders(result));
                node.putPOJO("json", parser.jsonParser(bodyData));
                node.put("origin", clinetIp);
                node.put("url", host + path);
            //}
            //if(getContentType(result).equals("multipart/form-data")){
//                node.putPOJO("args", parser.argParser(path));
//                node.put("data", "");
//                node.putPOJO("files", parser.getFile());
//                node.putPOJO("form", "");
//                node.putPOJO("headers", parser.getHeaders(result));
//                node.putPOJO("json", parser.jsonParser(bodyData));
//                node.put("origin", clinetIp);
//                node.put("url", host + path);
            //}
        }


        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
    }
    public static boolean runTimeOut() throws InterruptedException {
        Thread.sleep(5000);
        return true;
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
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode node = mapper.createObjectNode();

            String firstLine =  br.readLine();
            String line = null;
            List<String> requestHeader = new ArrayList<>();
            requestHeader.add(firstLine);
//            System.out.println(firstLine);
//            byte[] byteArr = new byte[2048];
//            out.write(byteArr);
//            String s  = new String(byteArr);
//            System.out.println(s);
//            System.out.println(byteArr.toString());


            while ((line = br.readLine()) != null) {
                requestHeader.add(line);
                if (line.equals("")) break;
            }

            StringBuilder bodyData = new StringBuilder();
            if (firstLine.contains("/post")) {
                while (br.ready() && (line = br.readLine()) != null) {
                    bodyData.append(line);
                }
            }

            ////////////////////

            String[] splitLine = requestHeader.get(0).split(" ");
            String [] clientIp = clientSocket.getRemoteSocketAddress().toString().replace("/","").split(":");
            String body = splitLine[2] + " 200 OK\n" + "Date: " + server.date() +"\n" + "Content-Type: application/json\n" +
                    "Content-Length: " + server.size(server.bodyMake(requestHeader, bodyData.toString(), clientIp[0])) + "\n" +
                    "Connection: keep-alive\n" +
                    "Server: gunicorn/19.9.0\n" +
                    "Access-Control-Allow-Origin: *\n" +
                    "Access-Control-Allow-Credentials: true\n\n" + server.bodyMake(requestHeader, bodyData.toString(), clientIp[0])+"\n";

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