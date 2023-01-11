package com.zaranik.cursework.authservice.entities;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Table(name = "app_users")
@NoArgsConstructor
public class User extends BaseEntity {

    private String username;

    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private boolean isActivated;

    private String refreshToken;

    @ManyToOne(optional = false)
    private Role role;


    public User(String username, String email, String password,
                String firstName, String lastName, boolean isActivated) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActivated = isActivated;
    }


    public static UserBuilder builder(){
        return new UserBuilder();
    }

    public static class UserBuilder {
        private String username;
        private String email;
        private String password;
        private String firstName;
        private String lastName;
        private boolean isActivated;

        public UserBuilder setUsername(String username) {
            this.username = username;
            return this;
        }

        public UserBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserBuilder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserBuilder setActivated(boolean activated) {
            isActivated = activated;
            return this;
        }

        public User build(){
            return new User(username, email, password,
                    firstName, lastName, isActivated);
        }

    }

}
