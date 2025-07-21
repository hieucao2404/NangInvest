/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "UserLessons")
@IdClass(UserLessons.UserLessonsId.class)
public class UserLessons {
    @Id
    @Column(name = "UserID")
    private int userId;

    @Id
    @Column(name = "LessonID")
    private int lessonId;

    @Column(name = "Completed")
    private boolean completed;

    @Column(name = "CompletionDate")
    private Timestamp completionDate;

    // Constructors
    public UserLessons() {}
    public UserLessons(int userId, int lessonId) {
        this.userId = userId;
        this.lessonId = lessonId;
        this.completed = false;
    }

    // Getters and setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getLessonId() { return lessonId; }
    public void setLessonId(int lessonId) { this.lessonId = lessonId; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    public Timestamp getCompletionDate() { return completionDate; }
    public void setCompletionDate(Timestamp completionDate) { this.completionDate = completionDate; }

    @Embeddable
    public static class UserLessonsId implements java.io.Serializable {
        @Column(name = "UserID")
        private int userId;

        @Column(name = "LessonID")
        private int lessonId;

        // Getters, setters, equals, hashCode
        public int getUserId() { return userId; }
        public void setUserId(int userId) { this.userId = userId; }
        public int getLessonId() { return lessonId; }
        public void setLessonId(int lessonId) { this.lessonId = lessonId; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UserLessonsId that = (UserLessonsId) o;
            return userId == that.userId && lessonId == that.lessonId;
        }

        @Override
        public int hashCode() {
            return 31 * userId + lessonId;
        }
    }
}
