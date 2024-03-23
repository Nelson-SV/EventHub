package be;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class User {
    private SimpleStringProperty firstName;
    private SimpleStringProperty lastName;
    private SimpleStringProperty role;
    private SimpleIntegerProperty userId;

    public User(String firstName, String lastName, String role) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.role = new SimpleStringProperty(role);
        this.userId = new SimpleIntegerProperty();
    }

    @Override
    public String toString() {
        return firstName.get() + " "+lastName.get();

    }

    public String getFirstName() {
        return firstName.get();
    }

    public SimpleStringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public SimpleStringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }


    public int getUserId() {
        return userId.get();
    }

    public SimpleIntegerProperty userIdProperty() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId.set(userId);
    }
}
