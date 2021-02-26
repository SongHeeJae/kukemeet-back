package com.kuke.videomeeting.repository.user;

import com.kuke.videomeeting.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUid(String uid);
    Optional<User> findByNickname(String nickname);
    Optional<User> findByUidAndProvider(String uid, String provider);

    @Query("select u from User u join fetch u.roles where u.id = :id")
    Optional<User> findByIdWithRoles(@Param("id") Long id);

    @Query("select u from User u where u.uid like concat('%', :uid, '%') and " +
            "u.username like concat('%', :username, '%') and " +
            "u.nickname like concat('%', :nickname, '%')")
    List<User> findAllUsersByCondition(@Param("uid") String uid, @Param("username") String username, @Param("nickname") String nickname);
}
