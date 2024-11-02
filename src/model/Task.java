package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

/* Базовый класс для задач. Содержит в себе базовые поля.  Внутри есть также id, который передается извне. */

public class Task {

    private String summary;
    private Status status;
    private String description;
    private Integer id;
    private Duration duration;
    private LocalDateTime startTime;

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Task(String summary, String description, Status status, Long duration, LocalDateTime startTime) {
        this.summary = summary;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = Duration.ofMinutes(duration);

    }

    public Task(String summary, String description, Status status) {
        this.summary = summary;
        this.description = description;
        this.status = status;
        this.duration = Duration.ZERO;
        this.startTime = LocalDateTime.MIN;

    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
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

    public void setDuration(Long duration) {
        this.duration = Duration.ofMinutes(duration);
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
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



