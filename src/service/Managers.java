package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import service.exception.ManagerSaveException;
import service.history.HistoryManager;
import service.history.InMemoryHistoryManager;
import service.server.GsonAdapters;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

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
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new GsonAdapters.LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new GsonAdapters.DurationTypeAdapter())
                .serializeNulls()
                .create();
    }
}


