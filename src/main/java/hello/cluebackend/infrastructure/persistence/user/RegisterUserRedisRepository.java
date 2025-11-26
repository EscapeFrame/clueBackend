package hello.cluebackend.infrastructure.persistence.user;

import hello.cluebackend.application.user.dto.response.UserRedisDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisterUserRedisRepository extends CrudRepository<UserRedisDto, String> {
}
