package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import service.exception.ManagerSaveException;
import service.history.HistoryManager;
import service.history.InMemoryHistoryManager;

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

    public static Gson getGson() {
        return new GsonBuilder().create();
    }
}


