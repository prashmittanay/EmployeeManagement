package org.learn.employeemanagement;

import java.io.Serializable;

public class Employee implements Serializable {
    private String id;
    private String name;
    private String department;
    private String picture;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getPicture() {
        return picture;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", picture='" + picture + '\'' +
                '}';
    }

    public Employee() {
        super();
    }

    public Employee(String id, String name, String department) {
        this.id = id;
        this.name = name;
        this.department = department;
    }

    public Employee(String id, String name, String department, String picture) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.picture = picture;
    }
}
