package com.nhnacademy.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static final ObjectMapper mapper = new ObjectMapper();
    private final Parser parser = new Parser();

    public String date() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("GMT"));
        return DateTimeFormatter.RFC_1123_DATE_TIME.format(now);
    }

    public int size(String str) {
        return str.length();
    }

    public String getContentType(List<String> result) {

        for (String line : result) {
            if (line.contains("Content-Type")) {
                return line.split(": ")[1];
            }
        }
        return null;
    }


    public String bodyMake(List<String> result, List<String> bodyData, String clientIp)
        throws JsonProcessingException {
        String path = result.get(0).split(" ")[1];
        String host = result.get(1).split(" ")[1];
        ObjectNode node = mapper.createObjectNode();

        if (path.contains("/ip")) {
            node.put("origin", clientIp);
        }
        if (path.contains("/get")) {
            node.putPOJO("args", parser.argParser(path));
            node.putPOJO("headers", parser.getHeaders(result));
            node.put("origin", clientIp);
            node.put("url", host + path);
        }
        if (path.contains("/post")) {
            if (getContentType(result).contains("application/json")) {
                StringBuilder sb = new StringBuilder();
                bodyData.forEach(sb::append);
                node.putPOJO("args", parser.argParser(path));
                node.put("data", sb.toString());
                node.putPOJO("files", "");
                node.putPOJO("form", "");
                node.putPOJO("headers", parser.getHeaders(result));
                node.putPOJO("json", parser.jsonParser(bodyData));
                node.put("origin", clientIp);
                node.put("url", host + path);
            }
            if (getContentType(result).contains("multipart/form-data")) {
                node.putPOJO("args", parser.argParser(path));
                node.put("data", "");
                String fileData = parser.getFile(bodyData);
                node.putPOJO("files", fileData);
                node.putPOJO("form", "");
                node.putPOJO("headers", parser.getHeaders(result));
                node.putPOJO("json", null);
                node.put("origin", clientIp);
                node.put("url", host + path);
            }
        }


        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
    }


    public void connect() {
        Server server = new Server();

        try (ServerSocket serverSocket = new ServerSocket(3000);
             Socket clientSocket = serverSocket.accept();
             InputStream in = clientSocket.getInputStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(in));
             OutputStream out = clientSocket.getOutputStream();
        ) {

            PrintStream ps = new PrintStream(out);

            String firstLine = br.readLine();
            String line = null;
            List<String> requestHeader = new ArrayList<>();
            requestHeader.add(firstLine);
            while ((line = br.readLine()) != null) {
                requestHeader.add(line);
                if (line.equals("")) {
                    break;
                }
            }

            List<String> bodyData = new ArrayList<>();
            if (firstLine.contains("/post")) {
                while (br.ready() && (line = br.readLine()) != null) {
                    bodyData.add(line);
                }
            }


            String[] splitLine = requestHeader.get(0).split(" ");
            String[] clientIp =
                clientSocket.getRemoteSocketAddress().toString().replace("/", "").split(":");
            String body = splitLine[2] + " 200 OK\n" + "Date: " + server.date() + "\n" +
                "Content-Type: application/json\n" +
                "Content-Length: " +
                server.size(server.bodyMake(requestHeader, bodyData, clientIp[0])) +
                "\n" +
                "Connection: keep-alive\n" +
                "Server: gunicorn/19.9.0\n" +
                "Access-Control-Allow-Origin: *\n" +
                "Access-Control-Allow-Credentials: true\n\n" +
                server.bodyMake(requestHeader, bodyData, clientIp[0]) + "\n";

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