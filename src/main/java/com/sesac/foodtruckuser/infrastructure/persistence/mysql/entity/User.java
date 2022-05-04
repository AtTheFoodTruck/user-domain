package com.sesac.foodtruckuser.infrastructure.persistence.mysql.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String email;
    private String username;
    private String password;
    private String phoneNum;
    private boolean activated;
    private String bNo;

    private Long orderId;

    private Long storeId;

    private Long reviewId;

    private Long likeId;

    private Long cartId;

    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;


    public void changeUser(String username) {
        this.username = username;
    }

    public void encodingPassword(String password) {
        this.password = password;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

}
