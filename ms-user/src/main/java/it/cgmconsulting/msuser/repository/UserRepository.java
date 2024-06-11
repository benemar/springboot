package it.cgmconsulting.msuser.repository;

import it.cgmconsulting.msuser.entity.User;
import it.cgmconsulting.msuser.payload.response.UserBackupResponse;
import it.cgmconsulting.msuser.payload.response.UserResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsernameOrEmail(String a, String b);
    boolean existsByIdAndAuthorityAuthorityName(long id, String authorityName);

    @Query(value="SELECT new it.cgmconsulting.msuser.payload.response.UserResponse(" +
            "u.id," +
            "u.username" +
            ") FROM User u " +
            "WHERE u.authority.authorityName = :authorityName")
    List<UserResponse> getByRole(@Param("authorityName") String authorityName);

    @Query(value="SELECT new it.cgmconsulting.msuser.payload.response.UserResponse(" +
            "u.id," +
            "u.username" +
            ") FROM User u " +
            "WHERE u.id = :userId")
    UserResponse getUser(@Param("userId") long userId);

    @Query(value="SELECT new it.cgmconsulting.msuser.payload.response.UserBackupResponse(" +
            "u.id, " +
            "u.username, " +
            "u.password, " +
            "u.email, " +
            "u.enabled, " +
            "u.authority.id, " +
            "u.createdAt, " +
            "u.updatedAt) " +
            "FROM User u")
    List<UserBackupResponse> getBackupUsers();
}
