package service;

public final class Managers {

    public static InMemoryTaskManager getDefaultManager() {
        return new InMemoryTaskManager(new InMemoryHistoryManager());
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}


