package com.ddooby.gachiillgi.domain.entity;

import com.ddooby.gachiillgi.base.entity.BaseUpdateEntity;
import com.ddooby.gachiillgi.base.enums.UserStatusEnum;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "users")
public class User extends BaseUpdateEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "email", length = 50, unique = true)
    private String email;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "nickname", nullable = false, length = 50)
    private String nickname;

    @Column(name = "activated")
    @Enumerated(EnumType.STRING)
    private UserStatusEnum activated;

    @Column(name = "sex", length = 10)
    private String sex;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "profile_image", length = 100)
    private String profileImage;

    @Column(name = "is_oauth_user")
    private boolean isOAuthUser;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<UserAuthority> userAuthoritySet = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followingList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "followee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followerList = new ArrayList<>();

    public void updateAuthority(UserAuthority userAuthority) {
        //TODO 삭제 기능
        userAuthoritySet.add(userAuthority);
        userAuthority.setUser(this);
    }

    public boolean isActivatedUser() {
        return this.getActivated() == UserStatusEnum.ACTIVATED;
    }

    public boolean isNotOAuthUser() {
        return !this.isOAuthUser();
    }
}
