package fsb.jee.ecommerceproject.repositories;

import fsb.jee.ecommerceproject.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
