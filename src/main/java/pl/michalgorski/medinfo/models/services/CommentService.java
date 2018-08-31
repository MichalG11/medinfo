package pl.michalgorski.medinfo.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.michalgorski.medinfo.models.CommentEntity;
import pl.michalgorski.medinfo.models.DoctorEntity;
import pl.michalgorski.medinfo.models.forms.CommentForm;
import pl.michalgorski.medinfo.models.repositories.CommentRepository;

import java.util.Optional;

@Service
public class CommentService {

    final SessionService sessionService;
    final CommentRepository commentRepository;
    final DoctorService doctorService;

    @Autowired
    public CommentService(SessionService sessionService, CommentRepository commentRepository,
                          DoctorService doctorService) {
        this.sessionService = sessionService;
        this.commentRepository = commentRepository;
        this.doctorService = doctorService;
    }


    public void addComment(CommentForm commentForm, int doctorId) {
        CommentEntity commentEntity = createCommentEntity(commentForm, doctorId);
        commentRepository.save(commentEntity);
        doctorService.increaseQuantityOfComments(doctorId);
    }

    private CommentEntity createCommentEntity(CommentForm commentForm, int doctorId) {
        CommentEntity commentEntity = new CommentEntity();
        DoctorEntity doctorEntity = new DoctorEntity();

        doctorEntity.setId(doctorId);

        commentEntity.setContext(commentForm.getContext());
        commentEntity.setRating(commentForm.getRating());
        commentEntity.setDoctor(doctorEntity);
        commentEntity.setUser(sessionService.getUserEntity());
        return commentEntity;
    }

    public void deleteCommentById(int doctorId, int commentId) {
        doctorService.decreaseQuantityOfComments(doctorId);
        commentRepository.deleteById(commentId);
    }

    public Optional<CommentEntity> getCommentById(int commentId) {
        return commentRepository.findById(commentId);
    }



}
