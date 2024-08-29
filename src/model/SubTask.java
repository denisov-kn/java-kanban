package model;

public class SubTask extends Task {
        private Integer parentId;

    public SubTask(String summary, String description, Status status) {
        super(summary, description, status);
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
}
