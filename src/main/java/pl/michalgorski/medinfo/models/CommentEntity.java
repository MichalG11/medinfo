package pl.michalgorski.medinfo.models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "comment")
@Data
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String context;
    private String rating;

    @Column(name = "creation_time")
    private LocalDateTime commentDate;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private DoctorEntity doctor;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;


}
