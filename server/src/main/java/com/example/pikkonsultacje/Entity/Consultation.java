package com.example.pikkonsultacje.Entity;

import com.example.pikkonsultacje.Enum.Status;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Document(collection = "Consultations")
public class Consultation {

    @Id
    private String id;

    private User tutor;

    private User student;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime date;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime consultationStartTime;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime consultationEndTime;

    private String room;

    private Status status = Status.FREE;

    @Override
    public String toString() {
        return "Consultation{" +
                "id='" + id + '\'' +
                ", tutor='" + tutor + '\'' +
                ", student='" + student + '\'' +
                ", date=" + date +
                ", consultationStartTime=" + consultationStartTime +
                ", consultationEndTime=" + consultationEndTime +
                ", room=" + room +
                '}';
    }

    public boolean reserve(User user) {
        if (student == null ) {
            status = Status.RESERVED;
            student = user;
            return true;
        } else {
            return false;
        }
    }

    public void free() {
        student = null;
        status = Status.FREE;
    }


}