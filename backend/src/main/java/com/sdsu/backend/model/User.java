package com.sdsu.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Objects;

@Entity
@Table(name = "user")
public class User {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Constraints for data integrity
    @Getter @Setter
    @Column(unique = true, nullable = false)
    private String email;

    @Getter @Setter
    @Column(nullable = false)
    private String password; // Hashed Password

    @Column(nullable = false)
    private String name;
    @Setter
    private boolean activeStatus = false;
    private boolean darkMode = false;

    // VVV ||CALENDAR 1-1 || VVV

    // @OneToOne(mappedBy = "calendar")
    // private Calendar calendar;

    // @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    // private UserSettings userSettings;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Calendar calendar;

    public User() {
        //Default constructor required by JPA
    }

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    // getters (some getters aren't here due to lombok's @Getter)
    public String getUsername() {
        return name;
    }
    public boolean getActiveStatus() {return activeStatus;}
    public boolean getDarkMode() {return darkMode;}

    // setters (some setters aren't here due to lombok's @Setter)
    public void setUsername(String name) {
        this.name = name;
    }
    public void setActiveStatusTrue() {this.activeStatus = true;}
    public void setActiveStatusFalse() {this.activeStatus = false;}
    public void setDarkModeTrue() {this.darkMode =true;}
    public void setDarkModeFalse() {this.darkMode = false;}

    // maintain bidirectional synchronization
    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
        if (calendar != null) {
            calendar.setUser(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        // Use getClass() to check against Hibernate proxies
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        // Check equality based on ID (or a business key if ID is transient)
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        // Use ID for hashCode, ensuring consistency with equals
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", activeStatus=" + activeStatus +
                ", darkMode=" + darkMode +
                '}';
    }
}