package service;

public final class Managers {

    private static TaskManager inMemoryTaskManager = null;
    private static HistoryManager inMemoryHistoryManager = null;


    public static TaskManager getDefaultManager() {
        if (inMemoryTaskManager == null)
            inMemoryTaskManager = new InMemoryTaskManager();
       return inMemoryTaskManager;
    }

    public static HistoryManager getDefaultHistory() {
        if (inMemoryHistoryManager == null)
            inMemoryHistoryManager = new InMemoryHistoryManager();
        return inMemoryHistoryManager;
    }
}


