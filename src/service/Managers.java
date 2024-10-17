package service;

import java.io.File;

public final class Managers {

    public static TaskManager getDefaultManager() {
       return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTaskManager getDefaultBacked(File file) throws ManagerSaveException {
        return FileBackedTaskManager.loadFromFile(file);
    }
}


