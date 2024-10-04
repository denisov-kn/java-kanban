package model;

/* Епик содержит в себе ссылки на сабтаски. Статус эпика вычисляется через совокупность статусов сабтасок */

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {


    private List<SubTask> subTaskList;

    public Epic(String summary, String description) {
        super(summary, description, Status.NEW);
        subTaskList = new ArrayList<>();

    }
    /* Подсчет статуса:
    - если у эпика нет подзадач или все они имеют статус NEW, то статус должен быть NEW.
    - если все подзадачи имеют статус DONE, то и эпик считается завершённым — со статусом DONE.
    - во всех остальных случаях статус должен быть IN_PROGRESS.
    */
    public void updateStatus() {
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


}

