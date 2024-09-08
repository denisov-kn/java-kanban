package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public interface TaskManager {



    ArrayList <SubTask> getSubTask();

    ArrayList <Task> getTaskList();

    ArrayList <Epic> getEpicList();

    Task create(Task task);

    Epic create(Epic epic);

    SubTask create(SubTask subTask);

    void updateTask(Task task);

    void updateEpic(Epic epic);


    void updateSubTask(SubTask subTask);

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
    public ArrayList<SubTask> getSubTasksByEpic (Integer epicId);

    ArrayList<Task> getHistory();
}
