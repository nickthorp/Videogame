package com.hoard.videogame.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.hoard.user.entity.User;
import com.hoard.views.View;
import javax.validation.constraints.NotBlank;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity // This tells Hibernate to make a table out of this class
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"dateCreated", "dateModified", "deleted"},
        allowGetters = true)
public class Videogame {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
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
    private Boolean deleted;

    public Videogame() {}

    public Videogame(Integer id, User user, String title, String developer, String platform){
        this.id = id;
        this.user = user;
        this.title = title;
        this.platform = platform;
        this.developer = developer;
        this.isPlaying = false;
        this.isPlayed = false;
        this.isComplete = false;
    }

    public Videogame(User user, String title, String developer, String platform,
                     Boolean isComplete, Boolean isPlayed, Boolean isPlaying) {
        this.user = user;
        this.title = title;
        this.platform = platform;
        this.developer = developer;
        this.isPlaying = isPlaying;
        this.isPlayed = isPlayed;
        this.isComplete = isComplete;
    }

    public Videogame(Integer id, User user, String title, String developer, String platform,
                     Boolean isComplete, Boolean isPlayed, Boolean isPlaying) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.platform = platform;
        this.developer = developer;
        this.isPlaying = isPlaying;
        this.isPlayed = isPlayed;
        this.isComplete = isComplete;
    }

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

    //TODO Override hashcode

    @Override
    public boolean equals(Object o) {
        Videogame videogame = (Videogame) o;
        return ( //this.user.equals(videogame.getUser()) &&
                this.title.equals(videogame.getTitle()) &&
                this.developer.equals(videogame.getDeveloper()) &&
                this.platform.equals(videogame.getPlatform()) &&
                this.isComplete == videogame.getIsComplete() &&
                this.isPlayed == videogame.getIsPlayed() &&
                this.isPlaying == videogame.getIsPlaying()
        );
    }
}
