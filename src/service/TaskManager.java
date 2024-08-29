package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* Класс который занимается упралением задачами. Задачи складываеются по типу каждый с вою хешмап.
В параметрах есть Id, сквозной для всех здач (всех типов) */

public class TaskManager  {

    private final HashMap <Integer, Task> taskList;
    private final HashMap <Integer, Epic> epicList;
    private final HashMap <Integer, SubTask> subTaskList;
    private Integer id = 0;


    public TaskManager() {
        epicList = new HashMap<>();
        subTaskList = new HashMap<>();
        taskList = new HashMap<>();

    }

    public HashMap<Integer, SubTask> getSubTask() {
        return subTaskList;
    }

    public HashMap<Integer, Task> getTaskList() {
        return taskList;
    }

    public HashMap<Integer, Epic> getEpicList() {
        return epicList;
    }

    public void addTask(Task task) {

        int currentId = generateId();
        task.setId(currentId);
        taskList.put(currentId, task);
    }

    public void addEpic(Epic epic) {
        int currentId = generateId();
        epic.setId(currentId);
        epicList.put(currentId, epic);
    }

    public void addSubTask(SubTask subTask, Integer epicId) {
        int currentId = generateId();
        Epic epic = epicList.get(epicId);
        subTask.setId(currentId);
        subTask.setParentId(epicId);
        epic.addSubTaskToEpic(subTask);
        epic.updateStatus();
        subTaskList.put(currentId, subTask);
    }

    public void updateTask(Integer taskId, Task task) {
        task.setId(taskId);
        taskList.put(taskId,task);
    }

    public void updateEpic(Integer epicId, Epic epic) {

        Epic previousEpic = epicList.get(epicId);
        epic.setId(epicId);
        epic.setSubTaskList(previousEpic.getSubTaskList()); // меняем ссылки на сабтаски внутри уже нового эпика
        epicList.put(epicId,epic);
    }

    public void updateSubTask(Integer previousSubTaskId, SubTask newSubTask) {

        newSubTask.setId(previousSubTaskId); // меняем id у нового сабтаска id

        SubTask previousSubTask = subTaskList.get(previousSubTaskId); // достаем старый сабтаск по id

        Integer epicId = previousSubTask.getParentId();
        Epic epic = epicList.get(epicId);
        epic.removeSubTask(previousSubTask); // удаляем сабтаску из списка эпиков

        newSubTask.setParentId(epicId);
        epic.addSubTaskToEpic(newSubTask);
        epic.updateStatus(); // обновляем статус эпика
        subTaskList.put(previousSubTaskId,newSubTask);

    }

    public Task getTask(Integer taskId) {
        return taskList.get(taskId);
    }

    public Epic getEpic(Integer epicId) {
        return epicList.get(epicId);
    }

    public SubTask getSubTask(Integer subTaskId) {
        return subTaskList.get(subTaskId);
    }

    public Task removeTask(Integer taskId) {
        return taskList.remove(taskId);
    }

    public void removeAllTask() {
        taskList.clear();
    }

    public void removeAllEpic(){
        removeAllSubTask();
        taskList.clear();
    }

    public void removeAllSubTask(){

        for (Map.Entry<Integer, Epic> integerEpicEntry : epicList.entrySet()) {
            integerEpicEntry.getValue().getSubTaskList().clear();
        }
        subTaskList.clear();
    }



    public SubTask removeSubTask(Integer subTaskId) {

        SubTask subTaskToDelete = subTaskList.get(subTaskId);
        Integer epicId = subTaskToDelete.getParentId();
        Epic epic = epicList.get(epicId);
        epic.removeSubTask(subTaskToDelete); // удаляем сабтаску из списка в эпике
        epic.updateStatus(); // обновляем статус эпика

        return subTaskList.remove(subTaskId);
    }

    public Epic removeEpic(Integer epicId) {
        ArrayList<SubTask> subTaskToRemove = epicList.get(epicId).getSubTaskList();   // удаление подзадач
        for (SubTask subTask : subTaskToRemove) {
            subTaskList.remove(subTask.getId());
        }

        return epicList.remove(epicId);
    }

     // метод возвращяет лист со спском сссылок на сабтаски эпика
    public ArrayList<SubTask> getSubTasksByEpic (Integer epicId) {

        return epicList.get(epicId).getSubTaskList();
    }

    // метод для обновление обновления id
    private int generateId(){
        return id ++;
    }



}
