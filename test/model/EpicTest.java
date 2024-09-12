package model;

import org.junit.jupiter.api.*;

@DisplayName("Эпик")
class EpicTest {

    Epic epic;
    SubTask subTask1;
    SubTask subTask2;
    SubTask subTask3;

    @BeforeEach
    public void BeforeEach(){
        epic = new Epic("Эпик 1", "Описание 2");
        epic.setId(1);
        subTask1 = new SubTask("Сабтаск 1", "Описание 1", Status.NEW, epic.getId());
        subTask2 = new SubTask("Сабтаск 2", "Описание 2", Status.NEW, epic.getId());
        subTask3 = new SubTask("Сабтаск 3", "Описание 3", Status.NEW, epic.getId());
        epic.addSubTaskToEpic(subTask1);
        epic.addSubTaskToEpic(subTask2);
        epic.addSubTaskToEpic(subTask3);
    }


    @Test
    @DisplayName("должен иметь статус NEW если все задачи имеют статус NEW")
    public void shouldEpicNewStatusIfAllSubTaskAreNew() {
        epic.updateStatus();
        Assertions.assertEquals(Status.NEW, epic.getStatus());
    }


    @Test
    @DisplayName("должен иметь статус IN PROGRESS если хоть одна задача не имеет статус  DONE")
    public void shouldEpicInProgressStatusIfAllSubTaskAreNotDone() {

        subTask1.setStatus(Status.IN_PROGRESS);
        subTask3.setStatus(Status.DONE);
        epic.updateStatus();
        Assertions.assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    @DisplayName("должен иметь статус DONE если все задачи имеют статус DONE")
    public void shouldEpicDoneStatusIfAllSubTaskAreDone() {
        subTask1.setStatus(Status.DONE);
        subTask2.setStatus(Status.DONE);
        subTask3.setStatus(Status.DONE);
        epic.updateStatus();
        Assertions.assertEquals(Status.DONE, epic.getStatus());
    }

}