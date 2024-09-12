package service;

import model.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements  HistoryManager {

    private ArrayList<Task> browsingHistory;

    public InMemoryHistoryManager() {
        this.browsingHistory = new ArrayList<>();
    }

    @Override
    public <T extends Task> void add(T task) {
        if (browsingHistory.size() == 10) browsingHistory.removeFirst();
        browsingHistory.add(task);
    }


    @Override
    public ArrayList<Task> getHistory() {
        if (browsingHistory.isEmpty()) return null;
        return browsingHistory;
    }


}
