package dev.zelenin.colorcounter.storage;

import java.util.Map;

public interface ColorDataStorage {
    void store(String color);

    Map<String, Long> findAll();
}
