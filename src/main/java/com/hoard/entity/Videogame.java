package com.hoard.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.hoard.views.View;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity // This tells Hibernate to make a table out of this class
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"dateCreated", "dateModified"},
        allowGetters = true)
public class Videogame {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @JsonView(View.Summary.class)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonView(View.SummaryWithUser.class)
    private User user;
    @NotBlank
    @JsonView(View.Summary.class)
    private String title;
    @JsonView(View.Summary.class)
    private String developer;
    @JsonView(View.Summary.class)
    private String platform;
    @JsonView(View.Summary.class)
    private Boolean isPlayed = false;
    @JsonView(View.Summary.class)
    private Boolean isPlaying = false;
    @JsonView(View.Summary.class)
    private Boolean isComplete = false;

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

    public Videogame() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    //@JsonIgnore
    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Boolean getIsPlayed() {
        return isPlayed;
    }

    public void setIsPlayed(Boolean isPlayed) {
        this.isPlayed = isPlayed;
    }

    public Boolean getIsPlaying() {
        return isPlaying;
    }

    public void setIsPlaying(Boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public Boolean getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(Boolean isComplete) {
        this.isComplete = isComplete;
    }

    public Date getDateCreated() { return dateCreated; }

    public Date getDateModified() { return dateModified; }

}
