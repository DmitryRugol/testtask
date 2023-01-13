package by.dmitryrugol.testtask.repository;

import by.dmitryrugol.testtask.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select u.* from users u where exists (select 1 from email_data ed where ed.user_id = u.id and email = :searchStr) " +
            "or exists (select 1 from phone_data pd where pd.user_id = u.id and phone = :searchStr)", nativeQuery = true)
    Optional<User> findByEmailOrPhone(@Param("searchStr") String searchStr);
}
