package com.kotov.restaurant.model.entity;

import java.time.LocalDateTime;

public class User extends AbstractEntity {
    private String login;
    private String email;
    private String firstName;
    private String patronymic;
    private String lastName;
    private String mobileNumber;
    private LocalDateTime registered;
    private Address address;
    private Status status;
    private Role role;

    public enum Status {
        OFFLINE, ONLINE, BLOCKED
    }

    public enum Role {
        ADMIN, MANAGER, CLIENT, GUEST
    }

    public User() {
    }

    public User(String login, String email, String mobileNumber, LocalDateTime registered, User.Role role) {
        this.login = login;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.registered = registered;
        this.role = role;
        this.status = Status.OFFLINE;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public LocalDateTime getRegistered() {
        return registered;
    }

    public void setRegistered(LocalDateTime registered) {
        this.registered = registered;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (!super.equals(otherObject)) {
            return false;
        }
        User other = (User) otherObject;
        if (login != null ? !login.equals(other.login) : other.login != null) {
            return false;
        }
        if (email != null ? !email.equals(other.email) : other.email != null) {
            return false;
        }
        if (firstName != null ? !firstName.equals(other.firstName) : other.firstName != null) {
            return false;
        }
        if (patronymic != null ? !patronymic.equals(other.patronymic) : other.patronymic != null) {
            return false;
        }
        if (lastName != null ? !lastName.equals(other.lastName) : other.lastName != null) {
            return false;
        }
        if (mobileNumber != null ? !mobileNumber.equals(other.mobileNumber) : other.mobileNumber != null) {
            return false;
        }
        if (registered != null ? !registered.equals(other.registered) : other.registered != null) {
            return false;
        }
        if (address != null ? !address.equals(other.address) : other.address != null) {
            return false;
        }
        if (status != other.status) {
            return false;
        }
        return role == other.role;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (login != null ? login.hashCode() : 0);
        result = prime * result + (email != null ? email.hashCode() : 0);
        result = prime * result + (firstName != null ? firstName.hashCode() : 0);
        result = prime * result + (patronymic != null ? patronymic.hashCode() : 0);
        result = prime * result + (lastName != null ? lastName.hashCode() : 0);
        result = prime * result + (mobileNumber != null ? mobileNumber.hashCode() : 0);
        result = prime * result + (registered != null ? registered.hashCode() : 0);
        result = prime * result + (address != null ? address.hashCode() : 0);
        result = prime * result + (status != null ? status.hashCode() : 0);
        result = prime * result + (role != null ? role.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(super.toString());
        result.append(" ,login = ").append(login);
        result.append(" ,email = ").append(email);
        result.append(" ,firstName = ").append(firstName);
        result.append(" ,middleName = ").append(patronymic);
        result.append(" ,lastName = ").append(lastName);
        result.append(" ,mobileNumber = ").append(mobileNumber);
        result.append(" ,registered = ").append(registered);
        result.append(" ,address = ").append(address);
        result.append(" ,status = ").append(status);
        result.append(" ,role = ").append(role).append(" ]");
        return result.toString();
    }
}