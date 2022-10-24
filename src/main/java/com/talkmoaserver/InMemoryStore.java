package com.talkmoaserver;

import com.talkmoaserver.dto.FrequencyResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InMemoryStore {
    private final List<FrequencyResult> currentLow = new ArrayList<>();
    private final List<FrequencyResult> currentHigh = new ArrayList<>();

    public void saveLow(List<FrequencyResult> low) {
        currentLow.clear();
        currentLow.addAll(low);
    }

    public void saveHigh(List<FrequencyResult> high) {
        currentHigh.clear();
        currentHigh.addAll(high);
    }

    public List<FrequencyResult> getLow() {
        return currentLow;
    }

    public List<FrequencyResult> getHigh() {
        return currentHigh;
    }

    public void clear() {
        currentLow.clear();
        currentHigh.clear();
    }
}
