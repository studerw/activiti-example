package com.studerw.activiti.model;

import com.google.common.base.Objects;
import org.activiti.engine.identity.User;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * User: studerw
 * Date: 5/18/14
 */
public class UserForm implements Serializable {
    @NotNull
    @Size(min = 5, max = 50)
    private String userName;
    @JsonIgnore
    @NotNull
    @Size(min = 8, max = 30)
    private String password;
    @NotNull
    @Email
    private String email;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String group;

    public UserForm() {}

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public UserForm(String userName, String password, String email, String firstName, String lastName, String group) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.group = group;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserForm)) return false;

        UserForm userForm = (UserForm) o;

        if (userName != null ? !userName.equals(userForm.userName) : userForm.userName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return userName != null ? userName.hashCode() : 0;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("userName", userName)
                .add("email", email)
                .add("firstName", firstName)
                .add("lastName", lastName)
                .add("group", group)
                .toString();
    }

    public static UserForm fromUser(User user){
        UserForm userForm = new UserForm();
        userForm.setUserName(user.getId());
        userForm.setPassword(user.getPassword());
        userForm.setFirstName(user.getFirstName());
        userForm.setLastName(user.getLastName());
        userForm.setEmail(user.getEmail());

        return userForm;

    }
}
