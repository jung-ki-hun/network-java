package com.nhnacademy.network;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Parser {
    public String date() {

        String input = LocalDateTime.now().toString();
        DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
        LocalDateTime date = LocalDateTime.parse(input, formatter);

        return date.toString();
    }

    public static void main(String[] args) {
        Parser parser = new Parser();
//        parser.date();
        System.out.println(parser.date());
    }

}
