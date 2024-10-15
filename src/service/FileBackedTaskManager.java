package service;

import model.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;


public class FileBackedTaskManager extends InMemoryTaskManager  implements TaskManager  {

    private File file;

    public FileBackedTaskManager (File file) {
        this.file = file;
    }

    public void save () {

        try(Writer fileWriter = new FileWriter("backup.txt")){


            fileWriter.write("id,type,name,status,description,epic\n");
            for (Task task: super.getTaskList()) {
                fileWriter.write(toStringForBacked(task));
            }

            for (Epic epic: super.getEpicList()) {
                fileWriter.write(toStringForBacked(epic));
            }

            for (SubTask subTask: super.getSubTaskList()) {
                fileWriter.write(toStringForBacked(subTask));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static FileBackedTaskManager loadFromFile(File file) {

    }

    private String toStringForBacked(Task task) {

        Type type;
        if (task instanceof Epic) {
            type = Type.EPIC;
        } else if (task instanceof SubTask) {
            type = Type.SUBTASK;
        } else {
            type = Type.TASK;
        }

        String str =  task.getId() +
                "," + type +
                "," + task.getSummary() +
                "," + task.getStatus() +
                "," + task.getDescription() + ",";
        if (type.equals(Type.SUBTASK))
            str += ((SubTask) task).getParentId();
        return str;

    }

    private Task fromString (String str) {
        String[] strList = str.split(",");

        if (strList[1].equals(Type.EPIC.name())) {

            Epic epic = new Epic(strList[2], strList[4]);
           epic.setId(Integer.parseInt(strList[0]));
           return epic;

        } else if (strList[1].equals(Type.SUBTASK.name())) {

            SubTask subTask = new SubTask(strList[2],
                    strList[4],
                    Status.valueOf(strList[3]),
                    Integer.parseInt(strList[5])
            );

            subTask.setId(Integer.parseInt(strList[0]));
            super.getEpic(Integer.parseInt(strList[5])).addSubTaskToEpic(subTask);
            return subTask;

        } else {

            Task task = new Task(strList[2], strList[4], Status.valueOf(strList[3]));
            task.setId(Integer.parseInt(strList[0]));
            return task;

        }


    }


    @Override
    public Epic create(Epic epic) {
        Epic epic1 = super.create(epic);
        save();
        return epic1;
    }

    @Override
    public SubTask create(SubTask subTask) {
        SubTask subTask1 = super.create(subTask);
        save();
        return  subTask1;
    }

    @Override
    public Task create(Task task) {

        Task task1 = super.create(task);
        save();
        return  task1;
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public Task removeTask(Integer taskId) {
        Task task = super.removeTask(taskId);
        save();
        return task;
    }

    @Override
    public SubTask removeSubTask(Integer subTaskId) {

        SubTask subTask = super.removeSubTask(subTaskId);
        save();
        return subTask;
    }

    @Override
    public Epic removeEpic(Integer epicId) {
        Epic epic = super.removeEpic(epicId);
        save();
        return  epic;
    }

    @Override
    public void removeAllTask() {
        super.removeAllTask();
        save();
    }

    @Override
    public void removeAllSubTask() {
        super.removeAllSubTask();
        save();
    }

    @Override
    public void removeAllEpic() {
        super.removeAllEpic();
        save();
    }


}
