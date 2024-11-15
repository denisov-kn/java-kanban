package service;

import model.*;
import service.exception.ManagerSaveException;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class FileBackedTaskManager extends InMemoryTaskManager  {

    File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }



    private void save() {

        try (Writer fileWriter = new FileWriter(file.getAbsolutePath())) {

            fileWriter.write("id,type,name,status,description,duration,startTime,epic\n");

            for (Task task: this.getTaskList()) {
                fileWriter.write(toString(task) + "\n");
            }

            for (Epic epic: this.getEpicList()) {
                fileWriter.write(toString(epic) + "\n");
            }

            for (SubTask subTask: this.getSubTaskList()) {
                fileWriter.write(toString(subTask) + "\n");
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка в файле:" + file.getAbsolutePath(), e);
            }
    }

    public static FileBackedTaskManager loadFromFile(File file) {

        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);

        List<String> backup = new ArrayList<>();
        if (file.exists()) {
            try (BufferedReader fileReader = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
                fileReader.readLine();

                while (fileReader.ready()) {
                          backup.add(fileReader.readLine());
                }
            } catch (IOException e) {
                throw new ManagerSaveException("Ошибка в файле:" + file.getAbsolutePath(), e);
            }

        }

        if (!backup.isEmpty()) {
            int maxId = 0;
            for (String backupLine : backup) {
                Task task = fileBackedTaskManager.fromString(backupLine);

                fileBackedTaskManager.backupTask(task);
                int taskId = task.getId();
                if (taskId > maxId) maxId = taskId;
            }

            fileBackedTaskManager.backupId(maxId);

        }

        return fileBackedTaskManager;
    }

    private String toString(Task task) {

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
                "," + task.getDescription() +
                "," + (task.getDuration() == null ? "null" : task.getDuration().toMinutes()) +
                "," + (task.getStartTime() == null ? "null" : task.getStartTime()
                .format(DateFormat.DATE_TIME_FORMAT.getFormatter())) + ",";
        if (type.equals(Type.SUBTASK))
            str += ((SubTask) task).getParentId();
        return str;

    }

    private Task fromString(String str) {
        String[] strList = str.split(",");

        if (strList[1].equals(Type.EPIC.name())) {

            Epic epic = new Epic(strList[2], strList[4]);
           epic.setId(Integer.parseInt(strList[0]));
           epic.setStatus(Status.valueOf(strList[3]));
           epic.setDuration(strList[5].equals("null") ? null : Long.parseLong(strList[5]));
           epic.setStartTime(strList[6].equals("null") ? null :
                   LocalDateTime.parse(strList[6], DateFormat.DATE_TIME_FORMAT.getFormatter())
           );

           return epic;

        } else if (strList[1].equals(Type.SUBTASK.name())) {

            SubTask subTask = new SubTask(strList[2],
                    strList[4],
                    Status.valueOf(strList[3]),
                    Integer.parseInt(strList[7]),
                    Long.parseLong(strList[5]),
                    LocalDateTime.parse(strList[6], DateFormat.DATE_TIME_FORMAT.getFormatter())
            );

            subTask.setId(Integer.parseInt(strList[0]));
            return subTask;

        } else {

            Task task = new Task(strList[2],
                    strList[4],
                    Status.valueOf(strList[3]),
                    Long.parseLong(strList[5]),
                    LocalDateTime.parse(strList[6], DateFormat.DATE_TIME_FORMAT.getFormatter())
            );
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
    public Task updateTask(Task task) {
        Task task1 = super.updateTask(task);
        save();
        return task1;
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
