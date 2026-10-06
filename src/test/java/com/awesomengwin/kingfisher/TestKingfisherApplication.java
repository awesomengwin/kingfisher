package com.awesomengwin.kingfisher;

import org.springframework.boot.SpringApplication;

public class TestKingfisherApplication {

    public static void main(String[] args) {
        SpringApplication.from(KingfisherApplication::main).with(TestcontainersConfiguration.class).run(args);
    }
}
