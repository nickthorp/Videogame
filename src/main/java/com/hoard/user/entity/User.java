package com.hoard.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.hoard.videogame.entity.Videogame;
import com.hoard.views.View;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"dateCreated", "dateModified", "isAccountNonExpired", "isAccountNonLocked",
                        "isCredentialsNonExpired", "isEnabled"},
        allowGetters = true)
@Table(name="hoard_user")
public class User implements UserDetails {
    private static final long serialVersionUID = 2396654715019746670L;

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
    private String username;

    private String password;

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

    private Boolean isAccountNonExpired;
    private Boolean isAccountNonLocked;
    private Boolean isCredentialsNonExpired;
    private Boolean isEnabled;

    //private Collection<GrantedAuthority> authorities;

    public User(){}

    public User(String email, String username, String firstName, String lastName ){
        this.email     = email;
        this.username  = username;
        this.firstName = firstName;
        this.lastName  = lastName;
    }

    public User(Integer id, String email, String username, String firstName, String lastName ){
        this.id        = id;
        this.email     = email;
        this.username  = username;
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

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return null;
    }

    public void setPassword(){}

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

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    //TODO Override hashcode

    @Override
    public boolean equals(Object o) {
        User user = (User) o;
        return ( (this.id == user.getId()) &&
                this.email.equals(user.getEmail()) &&
                this.firstName.equals(user.getFirstName()) &&
                this.lastName.equals(user.getLastName()) &&
                this.username.equals(user.getUsername())
        );
    }
}
