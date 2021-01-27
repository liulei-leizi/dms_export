package com.tazh.tazhinterface.pojo;
import java.util.Objects;
public class Goods {
    private int id;
    private String name;
    private int status;

    public Goods() {
    }

    public Goods(int id, String name, int status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Goods goods = (Goods) o;
        return id == goods.id &&
                status == goods.status &&
                Objects.equals(name, goods.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, status);
    }
}
