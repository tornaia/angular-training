package com.farata.course.mwd.auction.entity;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
    private int id;
    private String name;
    private String email;
    private boolean hasOverbidNotifications;

    public User(int id, String name, String email, boolean hasOverbidNotifications) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.hasOverbidNotifications = hasOverbidNotifications;
    }

    @Override public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", hasOverbidNotifications=").append(hasOverbidNotifications);
        sb.append('}');
        return sb.toString();
    }

    // TODO implement to getJsonObject method

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isHasOverbidNotifications() {
        return hasOverbidNotifications;
    }

    public void setHasOverbidNotifications(boolean hasOverbidNotifications) {
        this.hasOverbidNotifications = hasOverbidNotifications;
    }


    public int hashCode() {
        return id;
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (!(o instanceof User)) {
            return false;
        }

        User other = (User) o;

        return Objects.equals(this.id, other.id);
    }
}
