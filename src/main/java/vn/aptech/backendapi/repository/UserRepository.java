package vn.aptech.backendapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.aptech.backendapi.entities.User;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
//    @Query("SELECT u FROM User u WHERE u.email = :username OR u.phone = :username")
//    User findByEmailOrPhone(String username);
    Optional<User> findByEmailOrPhone(String email, String phone);

}
