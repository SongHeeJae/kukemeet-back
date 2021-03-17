package com.kuke.videomeeting.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User extends CommonEntityDate{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String uid;

    private String password;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String nickname;

    private String provider;

    private String refreshToken;

    private String code; // 인증번호

    private long failureCount = 0;

    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    private List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.REMOVE)
    List<Message> receivedMessages;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.REMOVE)
    List<Message> sentMessages;

    @OneToMany(mappedBy = "from", cascade = CascadeType.REMOVE)
    List<Friend> myFriends;

    @OneToMany(mappedBy = "to", cascade = CascadeType.REMOVE)
    List<Friend> addedMe;

    public static User createUser(String uid, String password, String username, String nickname, String provider, List<Role> roles) {
        User user = new User();
        user.uid = uid;
        user.password = password;
        user.username = username;
        user.nickname = nickname;
        user.provider = provider;
        user.roles = roles;
        return user;
    }

    public void changeRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void changeUsername(String username) {
        this.username = username;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeCode(String code) {
        this.code = code;
    }

    public void increaseFailureCount() {
        this.failureCount++;
    }

    public void resetFailureCount() {
        this.failureCount = 0;
    }


}
