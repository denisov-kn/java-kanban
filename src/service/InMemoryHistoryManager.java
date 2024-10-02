package service;

import model.Task;
import org.junit.platform.engine.support.hierarchical.Node;

import java.util.*;

public class InMemoryHistoryManager implements  HistoryManager {

    private HistoryHashMap<Task> browsingHistory;

    public InMemoryHistoryManager() {
        this.browsingHistory = new HistoryHashMap<Task>();
    }

    @Override
    public <T extends Task> void add(T task) {
        if (task == null) {
            return;
        }

        if(browsingHistory.hashMap.containsKey(task.getId())) {
            remove(task.getId());
        }
        browsingHistory.linkLast(task);
    }


    @Override
    public List<Task> getHistory() {
        if (browsingHistory.isEmpty()) return Collections.emptyList();
        return browsingHistory.getTasks();
    }

    @Override
    public void remove(int id) {

        browsingHistory.removeNode(browsingHistory.hashMap.get(id));
        browsingHistory.hashMap.remove(id);
    }



    private class HistoryHashMap<T extends Task> {

        private Map<Integer, HistoryNode<T>> hashMap;
        private HistoryNode<T> head;
        private HistoryNode<T> tail;
        private int size = 0;


        public HistoryHashMap() {
            hashMap = new HashMap<>();
        }

        public void linkLast(T task){

            final HistoryNode<T> oldTail = tail;
            final HistoryNode<T> newNode = new HistoryNode<>(task, oldTail, null);
            tail = newNode;
            if (oldTail == null)
                head = newNode;
            else
                oldTail.next = newNode;
            size++;
            hashMap.put(task.getId(),newNode);
        }

        public ArrayList<Task> getTasks () {
            ArrayList<Task> listTask= new ArrayList<>();
            for(HistoryNode<T> node : hashMap.values()) {
                listTask.add(node.data);
            }

            return listTask;
        }

        public boolean isEmpty() {
            if (size == 0) return true;
            return false;
        }

        public  void removeNode(HistoryNode<T> node) {

            if (isEmpty()) {
                return;
            }

            // Если голова содержит удаляемое значение
            if (head.data == node.data) {
                head = head.next;
                return;
            }

            HistoryNode<T> current = head;
            while (current.next != null && current.next.data != node.data) {
                current = current.next;
            }

            // Если значение найдено
            if (current.next != null) {
                current.next = current.next.next;
            }

        }


    }
}
