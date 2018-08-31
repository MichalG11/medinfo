package pl.michalgorski.medinfo.models.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.michalgorski.medinfo.models.CommentEntity;

import java.util.Optional;

@Repository
public interface CommentRepository extends CrudRepository<CommentEntity, Integer> {

    Optional<CommentEntity> findById(int commentId);

}
