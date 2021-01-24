package dev.zelenin.colorcounter;

import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.zelenin.colorcounter.messaging.KafkaProducer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Slf4j
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class ColorConsumerIntegrationTest {

    @Autowired
    private KafkaProducer producer;

    @Value("${app.topic}")
    private String topic;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void sendDataToKafka() {
        colors().forEach(color -> producer.send(topic, color));

        ResponseEntity<ObjectNode> response =
                restTemplate.getForEntity(String.format("http://localhost:%d/api/v1/colors/statistics", port), ObjectNode.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        ObjectNode statistics = response.getBody();
        assertEquals(6, statistics.get("red").asInt());
        assertEquals(5, statistics.get("green").asInt());
        assertEquals(2, statistics.get("blue").asInt());
    }

    /* Caution, this test never ends, only for simulation purpose.*/
    @Test
    @SneakyThrows
    public void sendDataUnlimitely() {
        var executor = Executors.newScheduledThreadPool(4);
        executor.scheduleAtFixedRate(() -> processStatistics(restTemplate.getForEntity(String.format("http://localhost:%d/api/v1/colors/statistics", port), ObjectNode.class)),
                5, 5, TimeUnit.SECONDS);

        generateColors().forEach(
                color -> producer.send(topic, color)
        );
    }

    private void processStatistics(ResponseEntity<ObjectNode> responseEntity) {
        log.info(responseEntity.getBody().toPrettyString());
    }

    // red = 6
    // green = 5
    // blue = 2
    private List<String> colors() {
        return List.of("red", "red", "red", "green", "red", "red", "green", "blue", "green", "red", "blue", "green",
                "green");
    }

    private Stream<String> generateColors() {
        var samples = List.of("red", "green", "blue");
        var random = new Random();

        return Stream.generate(() -> samples.get(random.nextInt(samples.size())));
    }

}
