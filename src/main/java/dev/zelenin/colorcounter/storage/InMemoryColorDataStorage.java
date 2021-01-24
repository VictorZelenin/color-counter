package dev.zelenin.colorcounter.storage;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Component
public class InMemoryColorDataStorage implements ColorDataStorage {
    private final Map<String, AtomicLong> storage = new ConcurrentHashMap<>();

    @Override
    public void store(String color) {
        storage.computeIfAbsent(color, c -> new AtomicLong()).incrementAndGet();
    }

    @Override
    public Map<String, Long> findAll() {
        return storage.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get()));
    }
}
