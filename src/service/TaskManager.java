package service;
import model.Epic;
import model.SubTask;
import model.Task;
import java.util.ArrayList;
import java.util.HashMap;

/* Класс который, занимается управлением задачами. Задачи складываются по типу - каждый свою хешмапу.
В параметрах есть Id - сквозной для всех задач (всех типов) */

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

    public ArrayList <SubTask> getSubTask() {

        return new ArrayList<>(subTaskList.values());
    }

    public ArrayList <Task> getTaskList() {
        return new ArrayList<>(taskList.values());
    }

    public ArrayList <Epic> getEpicList() {
        return new ArrayList<>(epicList.values());
    }

    public Task create(Task task) {

        int currentId = generateId();
        task.setId(currentId);
        taskList.put(currentId, task);

        return task;
    }

    public Epic create(Epic epic) {
        int currentId = generateId();
        epic.setId(currentId);
        epicList.put(currentId, epic);

        return epic;
    }

    public SubTask create(SubTask subTask) {


        int currentId = generateId();

        Epic epic = epicList.get(subTask.getParentId());
        if(epic == null) return null;

        subTask.setId(currentId);
        epic.addSubTaskToEpic(subTask);
        epic.updateStatus();
        subTaskList.put(currentId, subTask);

        return subTask;
    }

    public void updateTask(Task task) {
        Task taskCheck = taskList.get(task.getId());
        if (taskCheck == null) return;
        taskList.put(task.getId(),task);
    }

    public void updateEpic(Epic epic) {

        Epic currentEpic = epicList.get(epic.getId());
        if (currentEpic == null) return;

        currentEpic.setSummary(epic.getSummary());
        currentEpic.setDescription(epic.getDescription());

    }

    public void updateSubTask(SubTask subTask) {

        SubTask currentSubTask = subTaskList.get(subTask.getId());
        if (currentSubTask == null) return;

        // проверяем что к нам пришел на обновление сабтаск, у которого верный parentId
        Integer currentParentId = currentSubTask.getParentId();
        Integer subTaskParentId = subTask.getParentId();
        if (!currentParentId.equals(subTaskParentId)) return;


        Epic epic = epicList.get(currentParentId); //
        epic.removeSubTask(currentSubTask); // удаляем сабтаску из списка эпиков
        epic.addSubTaskToEpic(subTask); // добавляем сабтаску в список эпиков
        epic.updateStatus(); // обновляем статус эпика
        subTaskList.put(subTask.getId(),subTask);

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
        subTaskList.clear();
        epicList.clear();
    }

    public void removeAllSubTask(){

        for (Epic epic : epicList.values()) {
            epic.removeSubTaskList();     // очищаем лист с подзадачами у эпиков
            epic.updateStatus(); // обновляем статусы
        }
        subTaskList.clear();
    }



    public SubTask removeSubTask(Integer subTaskId) {

        SubTask subTaskToDelete = subTaskList.get(subTaskId);
        if(subTaskToDelete == null) return null;

        Integer epicId = subTaskToDelete.getParentId();
        Epic epic = epicList.get(epicId);
        epic.removeSubTask(subTaskToDelete); // удаляем сабтаску из списка в эпике
        epic.updateStatus(); // обновляем статус эпика

        return subTaskList.remove(subTaskId);
    }

    public Epic removeEpic(Integer epicId) {

        Epic epic = epicList.get(epicId);
        if (epic == null) return null;

        // удаление подзадач
        ArrayList<SubTask> subTaskToRemove = epic.getSubTaskList();
        for (SubTask subTask : subTaskToRemove) {
            subTaskList.remove(subTask.getId());
        }

        return epicList.remove(epicId);
    }

     // метод возвращает лист со списком ссылок на сабтаски эпика
    public ArrayList<SubTask> getSubTasksByEpic (Integer epicId) {

        return epicList.get(epicId).getSubTaskList();
    }

    // метод для обновления id
    private int generateId(){
        return id ++;
    }



}
