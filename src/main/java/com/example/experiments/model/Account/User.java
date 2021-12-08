package com.example.experiments.model.Account;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.time.LocalDate;
import java.util.Objects;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "User_table")
@Table(
        name = "user_table"
//        uniqueConstraints = {
//                @UniqueConstraint(
//                        name = "user_email_unique,",
//                        columnNames = "email"
//                )
//        }
)
public class User extends Person implements Account {

    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1 // amount to increment when allocating sequence numbers
    )
    @GeneratedValue(
            strategy = SEQUENCE, // use DB sequence for ID generation
            generator = "user_sequence"
    )
    @Column(
            name = "id",
            updatable = false // column not included in SQL UPDATE statements
    )
    private Long id;

    @Column(
            name = "username",
            columnDefinition = "TEXT", // TEXT datatype in POSTGRESQL
            unique = true
    )
    @NotNull // preferred over nullable = false as validation takes place before Hibernate sends SQL queries to the DB
    @NotEmpty(message = "Username cannot be empty")
    @Size(min=2, max=30)
    private String username;

    @Column(
            name = "password",
            columnDefinition = "TEXT"
    )
    @NotNull
    @NotEmpty(message = "Password cannot be empty")
    @Size(max = 30)
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$", message = "Minimum eight chars, at least one lowercase, one uppercase and one number")
    private String password;

    @Column(
            name = "email",
            columnDefinition = "TEXT"
    )
    @NotNull
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Must be a valid email address")
    private String email;

    public User() {
        super();
    }

    public User(String username,
                String password,
                String email,
                String firstName,
                String lastName) {
        super(firstName, lastName);
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User(String username,
                       String password,
                       String email,
                       String firstName,
                       String lastName,
                       LocalDate dob) {
        super(firstName, lastName, dob);
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String toString() {
        return "\nId: " + this.id +
                "\nUsername: " + this.username +
                "\nPassword: " + this.password +
                "\nEmail: " + this.email +
                "\nFirstName: " + super.getFirstName() +
                "\nLastName: " + super.getLastName() +
                "\nAge: " + super.getAge();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, email);
    }

    public Long getId() { return this.id; }
    public String getUsername() { return this.username; }
    public String getPassword() { return this.password; }
    public String getEmail() { return this.email; }
    public void setId(Long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email) { this.email = email; }
}

