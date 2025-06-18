package hello.cluebackend.global.security.jwt.domain.repository;

import hello.cluebackend.global.security.jwt.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

}