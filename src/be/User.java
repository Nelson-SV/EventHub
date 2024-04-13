package be;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.Objects;

public class User {
    private SimpleStringProperty firstName;
    private SimpleStringProperty lastName;
    private SimpleStringProperty role;
    private SimpleIntegerProperty userId;
    private ObservableList<String> userEvents;
    private SimpleStringProperty password;
    private SimpleStringProperty userImageUrl;

    public User(String firstName, String lastName, String role) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.role = new SimpleStringProperty(role);
        this.userId = new SimpleIntegerProperty();
    }

    public User(String firstName, String lastName, String role, String password, String userImageUrl) {
        this(firstName, lastName, role);
        this.password = new SimpleStringProperty(password);
        this.userImageUrl = new SimpleStringProperty(userImageUrl);
    }

    public User(String firstName, String lastName, String role, String password) {
        this(firstName, lastName, role);
        this.password = new SimpleStringProperty(password);
    }

/**copy constructor, in order to make  a copy , to not affect the original
 * @param user original  user object */
    public User(User user) {
        this.firstName = new SimpleStringProperty();
        this.firstName.setValue(user.getFirstName());
        this.lastName = new SimpleStringProperty();
        this.lastName.setValue(user.getLastName());
        this.password = new SimpleStringProperty();
        this.password.setValue(user.getPassword());
        this.role =new SimpleStringProperty();
        this.role.setValue(user.getRole());
        this.userImageUrl= new SimpleStringProperty();
        this.userImageUrl.setValue(user.getUserImageUrl());
        this.userEvents = FXCollections.observableArrayList();
        this.userEvents.setAll(user.getUserEvents());
        this.userId = new SimpleIntegerProperty();
        this.userId.setValue(user.getUserId());
    }

    public User(String firstName, String lastName, String role, String password, String userImageUrl, List<String> events) {
        this(firstName, lastName, role, password, userImageUrl);
        this.userEvents = FXCollections.observableArrayList();
        this.userEvents.setAll(events);
    }


    public ObservableList<String> getUserEvents() {
        return userEvents;
    }

    public void setUserEvents(ObservableList<String> userEvents) {
        this.userEvents = userEvents;
    }

    public String getPassword() {
        return password.get();
    }

    public SimpleStringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }


    public String getRole() {
        return role.get();
    }

    public SimpleStringProperty roleProperty() {
        return role;
    }

    public void setRole(String role) {
        this.role.set(role);
    }

    public String getUserImageUrl() {
        return userImageUrl.get();
    }

    public SimpleStringProperty userImageUrlProperty() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl.set(userImageUrl);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(firstName.getValue(), user.firstName.getValue()) &&
                Objects.equals(lastName.getValue(), user.lastName.getValue()) &&
                Objects.equals(role.getValue(), user.role.getValue()) &&
                Objects.equals(userId.getValue(), user.userId.getValue()) &&
                (password == null || user.password == null || Objects.equals(password.getValue(), user.password.getValue()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, role, userId, userEvents, password);
    }

    @Override
    public String toString() {
        return firstName.getValue() + " " +
               lastName.getValue();
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
