package com.nhnacademy.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.List;

public class Parser {
    private static final ObjectMapper mapper = new ObjectMapper();
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
            for (String s : pathUrl2) {
                String[] temp = s.split("=");
                node.put(temp[0], temp[1]);
            }
        }
        return node;
    }

    public ObjectNode jsonParser(List<String> str)
    {
        ObjectNode node = null;
        StringBuilder sb = new StringBuilder();
        str.forEach(sb::append);
        try {
            node = mapper.readValue(sb.toString(), ObjectNode.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return node;
    }

    public String getFile(List<String> str) {
        int index = str.indexOf("");
        str.forEach(System.out::println);
        return str.get(index +1);
    }

}
