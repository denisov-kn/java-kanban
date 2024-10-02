package service;

import model.Task;


import java.util.*;

public class InMemoryHistoryManager implements  HistoryManager {

    private HistoryHashMap<Task> browsingHistory;

    public InMemoryHistoryManager() {
        this.browsingHistory = new HistoryHashMap<>();
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
            final HistoryNode<T> newNode = new HistoryNode<>(task,null, oldTail);
            tail = newNode;
            if (oldTail == null)
                head = newNode;
            else {
                oldTail.next = newNode;
                newNode.prev = oldTail;
            }

            size++;
            hashMap.put(task.getId(),newNode);
        }

        public ArrayList<Task> getTasks () {
            ArrayList<Task> listTask= new ArrayList<>();
            HistoryNode<T> currentNode = head;
            while (currentNode != null) {
                listTask.add(currentNode.data);
                currentNode = currentNode.next;
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

            if (size == 1) { // Если в списке только одно значение
                head = null;
                tail = null;
                return;
            }


            // Если голова содержит удаляемое значение
            if (head.data == node.data) {
                head = head.next;
                HistoryNode<T> nodeNext = node.next;
                nodeNext.prev = null;
                return;
            }

            if (tail.data == node.data) {
                tail = tail.prev;
                HistoryNode<T> nodePrev = node.prev;
                nodePrev.next = null;
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
