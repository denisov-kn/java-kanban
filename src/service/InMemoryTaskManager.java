package service;

import model.Epic;
import model.SubTask;
import model.Task;
import service.exception.InteractionException;
import service.history.HistoryManager;

import java.util.*;

/* Класс который, занимается управлением задачами. Задачи складываются по типу - каждый в свою хешмапу.
В параметрах есть Id - сквозной для всех задач (всех типов) */

public class InMemoryTaskManager implements TaskManager {

    private final Map<Integer, Task> taskList;
    private final Map<Integer, Epic> epicList;
    private final Map<Integer, SubTask> subTaskList;
    private Integer id = 0;
    private final HistoryManager historyManager;
    private final Set<Task> prioritizedTasks;



    public InMemoryTaskManager() {
        epicList = new HashMap<>();
        subTaskList = new HashMap<>();
        taskList = new HashMap<>();
        prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
        this.historyManager = Managers.getDefaultHistory();
    }

    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    public List<SubTask> getSubTaskList() {

        return new ArrayList<>(subTaskList.values());
    }

    @Override
    public List<Task> getTaskList() {
        return new ArrayList<>(taskList.values());
    }

    @Override
    public List<Epic> getEpicList() {
        return new ArrayList<>(epicList.values());
    }

    @Override
    public Task create(Task task) {

        checkIntersect(task);
        int currentId = generateId();
        task.setId(currentId);
        taskList.put(currentId, task);
        addToSortedTree(task);
        return task;
    }

    @Override
    public Epic create(Epic epic) {

        int currentId = generateId();
        epic.setId(currentId);
        epicList.put(currentId, epic);
        return epic;
    }

    @Override
    public SubTask create(SubTask subTask) {


        checkIntersect(subTask);
        int currentId = generateId();

        Epic epic = epicList.get(subTask.getParentId());
        if (epic == null) return null;

        subTask.setId(currentId);
        epic.addSubTaskToEpic(subTask);
        epic.updateEpic();
        subTaskList.put(currentId, subTask);
        addToSortedTree(subTask);

        return subTask;
    }

    @Override
    public Task updateTask(Task task) {
        checkIntersect(task);
        final Task taskCheck = taskList.get(task.getId());
        if (taskCheck == null) return null;
        taskList.put(task.getId(),task);
        prioritizedTasks.remove(taskCheck);
        addToSortedTree(task);
        return task;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        Epic currentEpic = epicList.get(epic.getId());
        if (currentEpic == null) return null;
        currentEpic.setSummary(epic.getSummary());
        currentEpic.setDescription(epic.getDescription());
        return epic;
    }

    @Override
    public SubTask updateSubTask(SubTask subTask) {

        checkIntersect(subTask);

        final SubTask currentSubTask = subTaskList.get(subTask.getId());
        if (currentSubTask == null) return null;

        // проверяем что к нам пришел на обновление сабтаск, у которого верный parentId
        Integer currentParentId = currentSubTask.getParentId();
        Integer subTaskParentId = subTask.getParentId();
        if (!currentParentId.equals(subTaskParentId)) return null;


        Epic epic = epicList.get(currentParentId); //
        epic.removeSubTask(currentSubTask); // удаляем сабтаску из списка эпиков
        epic.addSubTaskToEpic(subTask); // добавляем сабтаску в список эпиков
        epic.updateEpic(); // обновляем статус эпика
        subTaskList.put(subTask.getId(),subTask);
        prioritizedTasks.remove(currentSubTask);
        addToSortedTree(subTask);
        return subTask;

    }

    @Override
    public Task getTask(Integer taskId) {
        Task task = taskList.get(taskId);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpic(Integer epicId) {
        Epic epic = epicList.get(epicId);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public SubTask getSubTask(Integer subTaskId) {
        SubTask subTask = subTaskList.get(subTaskId);
        historyManager.add(subTask);
        return subTaskList.get(subTaskId);
    }

    @Override
    public Task removeTask(Integer taskId) {
        historyManager.remove(taskId);
        prioritizedTasks.remove(getTask(taskId));
        return taskList.remove(taskId);
    }

    @Override
    public void removeAllTask() {
        clearAllTasksInHistory(taskList);
        for (Task task : taskList.values()) prioritizedTasks.remove(task);
        taskList.clear();
    }

    @Override
    public void removeAllEpic() {
        clearAllTasksInHistory(subTaskList);
        clearAllTasksInHistory(epicList);
        for (SubTask subTask : subTaskList.values()) prioritizedTasks.remove(subTask);
        subTaskList.clear();
        epicList.clear();
    }

    @Override
    public void removeAllSubTask() {

        for (Epic epic : epicList.values()) {
            epic.removeSubTaskList();     // очищаем лист с подзадачами у эпиков
            epic.updateEpic(); // обновляем статусы
        }
        clearAllTasksInHistory(subTaskList);
        for (SubTask subtask : subTaskList.values()) prioritizedTasks.remove(subtask);
        subTaskList.clear();
    }

    @Override
    public SubTask removeSubTask(Integer subTaskId) {

        SubTask subTaskToDelete = subTaskList.get(subTaskId);
        if (subTaskToDelete == null) return null;

        Integer epicId = subTaskToDelete.getParentId();
        Epic epic = epicList.get(epicId);
        epic.removeSubTask(subTaskToDelete); // удаляем сабтаску из списка в эпике
        epic.updateEpic(); // обновляем статус эпика

        historyManager.remove(subTaskId);
        prioritizedTasks.remove(getSubTask(subTaskId));
        return subTaskList.remove(subTaskId);
    }

    @Override
    public Epic removeEpic(Integer epicId) {

        Epic epic = epicList.get(epicId);
        if (epic == null) return null;

        // удаление подзадач
        List<SubTask> subTaskToRemove = epic.getSubTaskList();
        for (SubTask subTask : subTaskToRemove) {
            historyManager.remove(subTask.getId());
            prioritizedTasks.remove(subTask);
            subTaskList.remove(subTask.getId());
        }


        historyManager.remove(epicId);
        return epicList.remove(epicId);
    }

     // метод возвращает лист со списком ссылок на сабтаски эпика
    @Override
    public List<SubTask> getSubTasksByEpic(Integer epicId) {

        return epicList.get(epicId).getSubTaskList();
    }

    // метод для обновления id
    private int generateId() {
        return id++;
    }

    private void clearAllTasksInHistory(Map<Integer, ? extends Task> taskList) {

        if (!historyManager.getHistory().isEmpty()) {
            for (Integer taskId : taskList.keySet())
                historyManager.remove(taskId);
        }
    }

    protected void backupTask(Task task) {
        if (task instanceof Epic) {
            epicList.put(task.getId(), (Epic) task);
        } else if (task instanceof SubTask) {
            subTaskList.put(task.getId(), (SubTask) task);
            epicList.get(((SubTask) task).getParentId()).addSubTaskToEpic((SubTask) task);
        } else {
            taskList.put(task.getId(), task);
        }
    }

    protected void backupId(int id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    // метод поиска пересечения времени выполнения для двух задач
    private boolean isIntersect(Task task1, Task task2) {

        return (task1.getStartTime().isBefore(task2.getEndTime()) && task1.getEndTime().isAfter(task2.getStartTime()));


    }

    private void checkIntersect(Task task) {

        if (task.getStartTime() == null) return;
        if (prioritizedTasks.stream()
                .filter(currentTask -> currentTask.getId() == null || !currentTask.getId().equals(task.getId()))
                .anyMatch(task1 -> isIntersect(task1, task)))
            throw new InteractionException("Задача: " +  task
                    + " пересекается с уже существующей по времени выполнения");
    }

    private void addToSortedTree(Task task) {
        if (task.getStartTime() != null) prioritizedTasks.add(task);
    }
}
