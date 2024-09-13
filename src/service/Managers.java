package service;

public final class Managers {

    public static InMemoryTaskManager getDefaultManager() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}


