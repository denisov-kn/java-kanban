package service;

import model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InMemoryHistoryManager implements  HistoryManager {

    private List<Task> browsingHistory;

    public InMemoryHistoryManager() {
        this.browsingHistory = new ArrayList<>();
    }

    @Override
    public <T extends Task> void add(T task) {
        if (task != null) {
            if (browsingHistory.size() == 10) browsingHistory.removeFirst();
            browsingHistory.add(task);
        }
    }


    @Override
    public List<Task> getHistory() {
        if (browsingHistory.isEmpty()) return Collections.emptyList();
        return new ArrayList<>(browsingHistory);
    }


}
