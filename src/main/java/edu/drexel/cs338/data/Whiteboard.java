package edu.drexel.cs338.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angel on 8/21/2016.
 */
public class Whiteboard {
    private String name;
    private String creator;
    private String password;
    private List<String> users = new ArrayList<>();

    public Whiteboard() {
    }

    public Whiteboard(String name, String creator, String password) {
        this.name = name;
        this.creator = creator;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public void addUser(String user) {
        if(!users.contains(user)) {
            users.add(user);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Whiteboard that = (Whiteboard) o;

        if (!name.equals(that.name)) return false;
        if (!creator.equals(that.creator)) return false;
        return password != null ? password.equals(that.password) : that.password == null;

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + creator.hashCode();
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}
