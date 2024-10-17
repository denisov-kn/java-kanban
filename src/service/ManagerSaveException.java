package service;

import java.io.IOException;

public class ManagerSaveException extends IOException {

    public ManagerSaveException() {
    }

    public ManagerSaveException(String s, IOException e) {
     super(s,e);
    }
}
