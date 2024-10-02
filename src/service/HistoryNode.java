package service;

public class HistoryNode <T> {

    public T data;
    public HistoryNode<T> next;
    public HistoryNode<T> prev;

    public HistoryNode(T data, HistoryNode<T> next, HistoryNode<T> prev) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }
}
