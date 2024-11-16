package service;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.history.InMemoryHistoryManager;

@DisplayName("Менеджер")
class ManagersTest {

    @Test
    @DisplayName("должен передавать менеджера задач")
    public void shouldGetInMemoryTaskManager(){
        Assertions.assertNotNull(Managers.getDefaultManager(), "InMemoryTaskManager не должен быть null");
        Assertions.assertInstanceOf(InMemoryTaskManager.class , Managers.getDefaultManager(),
                "InMemoryTaskManager имеет другой класс");
    }

    @Test
    @DisplayName("должен передавать менеджера историй")
    public void shouldGetInMemoryHistoryManager(){
        Assertions.assertNotNull(Managers.getDefaultHistory(), "InMemoryHistory не должен быть null");
        Assertions.assertInstanceOf(InMemoryHistoryManager.class , Managers.getDefaultHistory(),
                "InMemoryHistory имеет другой класс");

    }

}