package model;

/* Каждая сабтаска имеет ссылку только на один эпик. */


public class SubTask extends Task {
        private Integer parentId;

    public SubTask(String summary, String description, Status status, Integer parentId) {
        super(summary, description, status);
        this.parentId = parentId;

    }


    public Integer getParentId() {
        return parentId;
    }
}
