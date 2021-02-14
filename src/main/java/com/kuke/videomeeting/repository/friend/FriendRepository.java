package com.kuke.videomeeting.repository.friend;

import com.kuke.videomeeting.domain.Friend;
import com.kuke.videomeeting.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    Optional<Friend> findByFromAndTo(User from, User to);

    @Query("select f from Friend f join fetch f.to " +
            "where f.from.id = :fromId order by f.to.username asc")
    Slice<Friend> findMyFriendsByUserIdOrderByUsername(@Param("fromId") Long fromId);
}
