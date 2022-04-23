package com.nhnacademy.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Parser {
    private final static ObjectMapper mapper = new ObjectMapper();
    public String date() {

        String input = LocalDateTime.now().toString();
        DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
        LocalDateTime date = LocalDateTime.parse(input, formatter);

        return date.toString();
    }
    public ObjectNode getHeaders(List<String> result){
        ObjectNode node = mapper.createObjectNode();
        int resultLength = result.size() - 1;
        for (int i = 1; i < resultLength; i++) {
            String[] split = result.get(i).split(": ");
            node.put(split[0], split[1]);
        }
        return node;
    }
    public ObjectNode argParser(String str){
        ObjectNode node = mapper.createObjectNode();

        if(str.contains("&")){
        String [] pathUrl = str.split("\\?");
        String [] pathUrl2 = pathUrl[1].split("&");
        for (int i = 0; i <pathUrl2.length; i++) {
            String[] temp=pathUrl2[i].split("=");
            node.put(temp[0],temp[1]);
        }
        return node;
        }
        return node;
    }

    public ObjectNode jsonParser(String str)
    {
        ObjectNode node = null;
        try {
            node = mapper.readValue(str, ObjectNode.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return node;
    }

    public ObjectNode getFile() {
        ObjectNode node = null;

        return node;
    }
//    public static void main(String[] args) {
//        Parser parser = new Parser();
//        System.out.println(parser.date());
//    }

}
