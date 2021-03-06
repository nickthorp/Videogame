package com.hoard.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.hoard.videogame.entity.Videogame;
import com.hoard.views.View;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"dateCreated", "dateModified", "deleted"},
        allowGetters = true)
@Table(name="hoard_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(View.Summary.class)
    private Integer id;
    @NotBlank
    @Email
    @Column(unique = true)
    @JsonView(View.Summary.class)
    private String email;
    @NotBlank
    @JsonView(View.Summary.class)
    private String userName;
    @JsonView(View.Summary.class)
    private String firstName;
    @JsonView(View.Summary.class)
    private String lastName;
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "user")
    @JsonView(View.SummaryWithList.class)
    private Set<Videogame> videogames = new HashSet<>();
    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    @JsonView(View.Summary.class)
    private Date dateCreated;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    @JsonView(View.Summary.class)
    private Date dateModified;
    //private Boolean deleted;

    public User(){}

    public User(String email, String userName, String firstName, String lastName ){
        this.email     = email;
        this.userName  = userName;
        this.firstName = firstName;
        this.lastName  = lastName;
    }

    public User(Integer id, String email, String userName, String firstName, String lastName ){
        this.id        = id;
        this.email     = email;
        this.userName  = userName;
        this.firstName = firstName;
        this.lastName  = lastName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public Date getDateCreated() {
        return dateCreated;
    }

    public Date getDateModified() {
        return dateModified;
    }

    public Set<Videogame> getVideogames() {
        return videogames;
    }

    public void setVideogames(Set<Videogame> videogames) {
        this.videogames = videogames;
    }

    //TODO Override hashcode

    @Override
    public boolean equals(Object o) {
        User user = (User) o;
        return ( (this.id == user.getId()) &&
                this.email.equals(user.getEmail()) &&
                this.firstName.equals(user.getFirstName()) &&
                this.lastName.equals(user.getLastName()) &&
                this.userName.equals(user.getUserName())
        );
    }
}
