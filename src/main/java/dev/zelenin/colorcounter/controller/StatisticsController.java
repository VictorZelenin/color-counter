package dev.zelenin.colorcounter.controller;

import dev.zelenin.colorcounter.storage.ColorDataStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class StatisticsController {
    private final ColorDataStorage dataStorage;

    @GetMapping("/colors/statistics")
    public Map<String, Long> fetchStatistics() {
        return dataStorage.findAll();
    }
}
