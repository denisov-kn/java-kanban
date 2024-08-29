package model;

import java.util.Objects;

/* Базовый класс для задач. Содержит в себе базовые поля.  Внутри есть также id, который передается извне. */

public class Task {

    private final String summary;
    private Status status;
    private final String description;
    private Integer id;

    public Task(String summary, String description, Status status) {
        this.summary = summary;
        this.description = description;
        this.status = status;

    }

    public void setId(Integer id) {
        this.id = id;
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


    /* Метод нужен для измнения статуса, но доступен только внутри модели.
    Для внешних классов изменеие статуса недоступно */
    protected void setStatus(Status status) {
        this.status = status;
    }


    @Override
    public int hashCode() {
        return Objects.hash(description, summary);
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
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        Task otherTask = (Task) obj;
        return Objects.equals(summary, otherTask.summary) &&
                Objects.equals(description, otherTask.description) &&
                Objects.equals(status, otherTask.status) &&
                Objects.equals(id, otherTask.id);

    }
}

