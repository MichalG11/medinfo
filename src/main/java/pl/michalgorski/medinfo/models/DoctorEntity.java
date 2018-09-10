package pl.michalgorski.medinfo.models;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "doctor")
@Data
public class DoctorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String city;
    private String specialization;
    private String name;
    private String surname;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "quantity_comments")
    private int quantityOfComments;

    @Column(name = "average_rating")
    private double averageRating;

    @OneToMany(mappedBy = "doctor", fetch = FetchType.EAGER, orphanRemoval = true)
    private List<CommentEntity> comments;

}
