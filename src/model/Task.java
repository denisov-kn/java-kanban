package model;

/* Базовый класс для задач. Содержит в себе базовые поля.  Внутри есть также id, который передается извне. */

import java.util.Objects;

public class Task {

    private String summary;
    private Status status;
    private String description;
    private Integer id;

    public Task(String summary, String description, Status status) {
        this.summary = summary;
        this.description = description;
        this.status = status;

    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }


    public String getSummary() {
        return summary;
    }


    public Status getStatus() {
        return status;
    }


    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" +
                "summary='" + summary + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", id=" + id + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task task)) return false;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return id;
    }
}



