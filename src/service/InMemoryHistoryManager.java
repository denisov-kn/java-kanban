package service;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements  HistoryManager {

    private HistoryHashMap browsingHistory;

    public InMemoryHistoryManager() {
        this.browsingHistory = new HistoryHashMap();
    }

    @Override
    public void add(Task task) {
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
        if (!browsingHistory.isEmpty()) {
            browsingHistory.removeNode(browsingHistory.hashMap.get(id));
            browsingHistory.hashMap.remove(id);
        }
    }



    private static class HistoryHashMap {

        private final Map<Integer, HistoryNode> hashMap;
        private HistoryNode head;
        private HistoryNode tail;
        private int size = 0;


        public HistoryHashMap() {
            hashMap = new HashMap<>();
        }

        public void linkLast(Task task){

            final HistoryNode oldTail = tail;
            final HistoryNode newNode = new HistoryNode(task,null, oldTail);
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
            HistoryNode currentNode = head;
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

        public  void removeNode(HistoryNode node) {

            if (size == 1) { // Если в списке только одно значение
                head = null;
                tail = null;
                size = 0;
                return;
            }

            // Если голова содержит удаляемое значение
            if (head.data == node.data) {
                head = head.next;
                HistoryNode nodeNext = node.next;
                nodeNext.prev = null;
            } else if (tail.data == node.data) {  // Если хвост содержит удаляемое значение
                tail = tail.prev;
                HistoryNode nodePrev = node.prev;
                nodePrev.next = null;

            } else {  // Если удаляемое значение возможно в середине
                HistoryNode current = head;
                while (current.next != null && current.next.data != node.data) {
                    current = current.next;
                }

                if (current.next != null) {   // Если значение найдено
                    current.next = current.next.next;
                }
            }
        }
    }
}
