package model;

/* Каждая сабтаска имеет ссылку только на один эпик. */

import java.time.LocalDateTime;

public class SubTask extends Task {
        private Integer parentId;

    public SubTask(String summary, String description, Status status, Integer parentId, Long duration, LocalDateTime startTime) {
        super(summary, description, status, duration, startTime);
        this.parentId = parentId;
    }

    public SubTask(String summary, String description, Status status, Integer parentId) {
        super(summary, description, status);
        this.parentId = parentId;
    }



    public Integer getParentId() {
        return parentId;
    }
}
