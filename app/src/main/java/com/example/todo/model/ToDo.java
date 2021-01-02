package com.example.todo.model;

import java.util.Date;

public class ToDo {
    private Integer id;
    private String name;
    private String description;
    private Date date;
    private String type;
    private Integer progress;
    private Boolean isdone;
    private Integer parentId;

    public ToDo() {
        this.name = "";
        this.description = "";
        this.type = "";
        this.date = new Date();
        this.progress = 0;
        this.isdone = false;
        this.parentId = null;
    }

    public ToDo(Integer id, String name,
                String description, Date date,
                String type, Integer progress, boolean isdone,
                Integer parentId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.type = type;
        this.progress = progress;
        this.isdone = isdone;
        this.parentId = parentId;
    }

    public Integer getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public Integer getProgress() {
        return progress;
    }

    public Integer getParentId() {
        return parentId;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Boolean getIsdone() {
        return isdone;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public void setIsdone(boolean isdone) {
        this.isdone = isdone;
    }

    public void setType(String type) {
        this.type = type;
    }
}
