package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

@DisplayName("Эпик")
class EpicTest {

    Epic epic;
    SubTask subTask1;
    SubTask subTask2;
    SubTask subTask3;
    LocalDateTime dateTimeSubTask1;
    LocalDateTime dateTimeSubTask2;
    LocalDateTime dateTimeSubTask3;

    Long subTask1Duration;
    Long subTask2Duration;
    Long subTask3Duration;

    @BeforeEach
    public void BeforeEach(){

        dateTimeSubTask1 = LocalDateTime.of(2024,1, 1, 1,0,0);
        dateTimeSubTask2 = LocalDateTime.of(2024,5, 1, 3,0,0);
        dateTimeSubTask3 = LocalDateTime.of(2024,4, 1, 6,0,0);
        subTask1Duration = 10L;
        subTask2Duration = 15L;
        subTask3Duration = 30L;

        epic = new Epic("Эпик 1", "Описание 2");
        epic.setId(1);
        subTask1 = new SubTask("Сабтаск 1",
                "Описание 1",
                Status.NEW,
                epic.getId(),
                subTask1Duration,
                dateTimeSubTask1
        );
        subTask2 = new SubTask("Сабтаск 2",
                "Описание 2",
                Status.NEW,
                epic.getId(),
                subTask2Duration,
                dateTimeSubTask2
        );
        subTask3 = new SubTask("Сабтаск 3",
                "Описание 3",
                Status.NEW,
                epic.getId(),
                subTask3Duration,
                dateTimeSubTask2
        );
        epic.addSubTaskToEpic(subTask1);
        epic.addSubTaskToEpic(subTask2);
        epic.addSubTaskToEpic(subTask3);
    }


    @Test
    @DisplayName("должен иметь статус NEW если все задачи имеют статус NEW")
    public void shouldEpicNewStatusIfAllSubTaskAreNew() {
        epic.updateEpic();
        Assertions.assertEquals(Status.NEW, epic.getStatus());
    }


    @Test
    @DisplayName("должен иметь статус IN PROGRESS если хоть одна задача не имеет статус  DONE")
    public void shouldEpicInProgressStatusIfAllSubTaskAreNotDone() {

        subTask1.setStatus(Status.IN_PROGRESS);
        subTask3.setStatus(Status.DONE);
        epic.updateEpic();
        Assertions.assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    @DisplayName("должен иметь статус DONE если все задачи имеют статус DONE")
    public void shouldEpicDoneStatusIfAllSubTaskAreDone() {
        subTask1.setStatus(Status.DONE);
        subTask2.setStatus(Status.DONE);
        subTask3.setStatus(Status.DONE);
        epic.updateEpic();
        Assertions.assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    @DisplayName("должен иметь статус IN PROGRESS если все задачи имеют статус IN PROGRESS")
    public void shouldEpicInProgressStatusIfAllSubTaskAreInProgress() {
        subTask1.setStatus(Status.IN_PROGRESS);
        subTask2.setStatus(Status.IN_PROGRESS);
        subTask3.setStatus(Status.IN_PROGRESS);
        epic.updateEpic();
        Assertions.assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }


    @Test
    @DisplayName("начальное время эпика должно быть временем самой раней задачи")
    public void shouldStarTimeIsStartTimeOfSubTask() {
        epic.updateEpic();
        Assertions.assertEquals(dateTimeSubTask1, epic.getStartTime());
    }

    @Test
    @DisplayName("продолжительность эпика должно быть суммой продолжительности всех сабтасков")
    public void shouldDurationOfEpicIsSumOfAllSubtask() {
        epic.updateEpic();
        Assertions.assertEquals(subTask1Duration + subTask2Duration + subTask3Duration,
                epic.getDuration().toMinutes());
    }

    @Test
    @DisplayName("время завершения эпика должно быть равно времени завершения самой поздней подзадачи")
    public void shouldEndTimeIsEndTimeOfSubTask() {
        epic.updateEpic();
        Assertions.assertEquals(subTask3.getEndTime(), epic.getEndTime());
    }


}