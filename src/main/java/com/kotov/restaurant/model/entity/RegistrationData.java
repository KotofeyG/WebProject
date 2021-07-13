package com.kotov.restaurant.model.entity;

public class RegistrationData {
    private String login;
    private String email;
    private String mobileNumber;

    public RegistrationData() {
    }

    public RegistrationData(String login, String email, String mobileNumber) {
        this.login = login;
        this.email = email;
        this.mobileNumber = mobileNumber;
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

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || getClass() != otherObject.getClass()) {
            return false;
        }

        RegistrationData other = (RegistrationData) otherObject;

        if (login != null ? !login.equals(other.login) : other.login != null) {
            return false;
        }
        if (email != null ? !email.equals(other.email) : other.email != null) {
            return false;
        }
        return mobileNumber != null ? mobileNumber.equals(other.mobileNumber) : other.mobileNumber == null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (login != null ? login.hashCode() : 0);
        result = prime * result + (email != null ? email.hashCode() : 0);
        result = prime * result + (mobileNumber != null ? mobileNumber.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(getClass().getSimpleName());
        result.append("[ login = ").append(login);
        result.append(",email = ").append(email);
        result.append(",mobileNumber = ").append(mobileNumber).append(" ]");
        return result.toString();
    }
}