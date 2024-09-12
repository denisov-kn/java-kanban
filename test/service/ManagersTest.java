package service;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ManagersTest {

    @Test
    public void shouldGetInMemoryTaskManager(){
        Assertions.assertNotNull(Managers.getDefaultManager());
        Assertions.assertInstanceOf(InMemoryTaskManager.class , Managers.getDefaultManager());
    }

    @Test
    public void shouldGetInMemoryHistoryManager(){
        Assertions.assertNotNull(Managers.getDefaultHistory());
        Assertions.assertInstanceOf(InMemoryHistoryManager.class , Managers.getDefaultHistory());

    }

}