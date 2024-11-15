package service;

import model.Epic;
import model.SubTask;
import model.Task;
import service.history.HistoryManager;

import java.util.List;

public interface TaskManager {

    List<SubTask> getSubTaskList();

    List<Task> getTaskList();

    List<Epic> getEpicList();

    Task create(Task task);

    Epic create(Epic epic);

    SubTask create(SubTask subTask);

    Task updateTask(Task task);

    void updateEpic(Epic epic);


    SubTask updateSubTask(SubTask subTask);

    Task getTask(Integer taskId);

    Epic getEpic(Integer epicId);

    SubTask getSubTask(Integer subTaskId);

    Task removeTask(Integer taskId);

    void removeAllTask();

    void removeAllEpic();

    void removeAllSubTask();


    SubTask removeSubTask(Integer subTaskId);

    Epic removeEpic(Integer epicId);

    // метод возвращает лист со списком ссылок на сабтаски эпика
    List<SubTask> getSubTasksByEpic(Integer epicId);

    List<Task> getHistory();

    public HistoryManager getHistoryManager();

    public List<Task> getPrioritizedTasks();


}
