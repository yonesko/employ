package gleb.data;

import gleb.model.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DigestTaskRepo extends CrudRepository<Task, Long> {
}
