package com.sample.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Sender {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {

        startThread();
        System.out.println("Press x to stop sending.");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String msg = "Welcome to Ootty!";
            Random rd = new Random();
            while (true) {
                String message = msg + " " + rd.nextDouble();
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
                System.out.println(" [x] Sent '" + message + "'");
                Thread.sleep(1000);
            }
            
        }
    }
    
    public static void startThread() {
        new Thread(() -> {
            try (Scanner sc = new Scanner(System.in)) {
                String str = sc.next();
                if (Arrays.asList("exit", "stop", "x").contains(str)) {
                    System.exit(0);
                }
            }
        }).start();
    }
}
