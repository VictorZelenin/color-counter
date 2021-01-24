package dev.zelenin.colorcounter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class ColorCounterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ColorCounterApplication.class, args);
    }

}
