package service;

import model.Task;

public class HistoryNode {

    public Task data;
    public HistoryNode next;
    public HistoryNode prev;

    public HistoryNode(Task data, HistoryNode next, HistoryNode prev) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }
}
