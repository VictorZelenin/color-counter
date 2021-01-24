package dev.zelenin.colorcounter.listener;

import dev.zelenin.colorcounter.storage.ColorDataStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ColorListener {
    private final ColorDataStorage colorDataStorage;

    @KafkaListener(topics = "${app.topic}")
    public void listen(ConsumerRecord<String, String> record) {
        log.debug("Got new color message.");

        colorDataStorage.store(record.value());
    }
}
