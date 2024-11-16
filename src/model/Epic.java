package model;

/* Епик содержит в себе ссылки на сабтаски. Статус эпика вычисляется через совокупность статусов сабтасок */

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {


    private List<SubTask> subTaskList;
    private LocalDateTime endTime;

    public Epic(String summary, String description) {
        super(summary, description, Status.NEW, 0L,LocalDateTime.MIN);
        this.setDuration(null);
        this.setStartTime(null);
        endTime = null;
        subTaskList = new ArrayList<>();
    }

    /* Обновляем расчетные поля эпика
       status
       startTime
       Duration
     */

    public void updateEpic() {
        updateStatus();
        updateTime();
    }

    /* Подсчет статуса (status):
    - если у эпика нет подзадач или все они имеют статус NEW, то статус должен быть NEW.
    - если все подзадачи имеют статус DONE, то и эпик считается завершённым — со статусом DONE.
    - во всех остальных случаях статус должен быть IN_PROGRESS.
    */


    private void updateStatus() {
        if (subTaskList.isEmpty()) {
            this.setStatus(Status.NEW);
            return;
        }
        int newCount = 0;
        int doneCount = 0;
        for (SubTask subTask : subTaskList) {
            switch (subTask.getStatus()) {
                case Status.NEW:
                    newCount++;
                    break;
                case Status.DONE:
                    doneCount++;
                    break;
            }
        }
        if (subTaskList.size() == newCount)
            this.setStatus(Status.NEW);
        else if (subTaskList.size() == doneCount)
            this.setStatus(Status.DONE);
        else
            this.setStatus(Status.IN_PROGRESS);
    }

    /*
     Подсчет продолжительности эпика (duration) - сумма продолжительностей всех его подзадач
     Время начала (startTime) — дата старта самой ранней подзадачи.
     Время завершения (endTime) - время окончания самой поздней из задач
    */

    private void updateTime() {
        if (subTaskList.isEmpty()) {
            this.setDuration(null);
            this.endTime = null;
            this.setStartTime(null);
            return;
        }

        LocalDateTime startDateTime = LocalDateTime.MAX;
        Duration epicDuration = Duration.ZERO;
        LocalDateTime currentEndTime = LocalDateTime.MIN;


        for (SubTask subTask : subTaskList) {

            if (subTask.getStartTime() != null) {
                if (subTask.getStartTime().isBefore(startDateTime)) startDateTime = subTask.getStartTime();
                if (subTask.getEndTime().isAfter(currentEndTime)) currentEndTime = subTask.getEndTime();
                epicDuration = epicDuration.plus(subTask.getDuration());
            }
        }

        //  Если внутри сабтасков были сабтаски с пустыми значениями
        if (startDateTime != LocalDateTime.MAX) this.setStartTime(startDateTime);
        else  this.setStartTime(null);

        if (epicDuration != Duration.ZERO)  this.setDuration(epicDuration.toMinutes());
        else this.setDuration(null);

        if (currentEndTime != LocalDateTime.MIN) this.endTime = currentEndTime;
        else this.endTime = null;
    }




    public void addSubTaskToEpic(SubTask subTask) {
        subTaskList.add(subTask);
    }

    public List<SubTask> getSubTaskList() {
        return new ArrayList<>(subTaskList);
    }

    public void removeSubTask(SubTask subTask) {

        subTaskList.remove(subTask);

    }

    public void removeSubTaskList() {
        subTaskList.clear();
    }

    //

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }
}

