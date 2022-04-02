package com.sesac.foodtruckuser.infrastructure.persistence.mysql.entity;

//import com.sesac.domain.common.BaseEntity;
//import com.sesac.domain.item.entity.Cart;
//import com.sesac.domain.item.entity.Store;
//import com.sesac.domain.order.entity.Order;
//import com.sesac.domain.order.entity.Review;
import com.sesac.foodtruckuser.infrastructure.persistence.mysql.repository.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;

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
    private String username; // nickname
    private String password;
    private String phoneNum;
    private boolean activated;
    private String bNo; // 사업자등록번호

//    @Enumerated(EnumType.STRING)
//    private Role role;

    // Order
//    @OneToMany(mappedBy = "user")
//    private List<Order> orders = new ArrayList<>();
//
//    // Store
//    @OneToOne(fetch = LAZY, mappedBy = "user")
//    private Store store;
//
//    // Review
//    @OneToMany(mappedBy = "user")
//    private List<Review> reviews = new ArrayList<>();
//
//    // Like
//    @OneToMany(mappedBy = "user")
//    private List<Like> likes = new ArrayList<>();
//
//    // Cart
//    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL) // User 삭제 시 cart 삭제?
//    @JoinColumn(name = "cart_id")
//    private Cart cart;

    // Authority
    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;


    // 회원정보 수정 - nickname
    public void changeUser(String username) {
        this.username = username;
    }

    // 회원정보 수정 - password
    public void encodingPassword(String password) {
        this.password = password;
    }
}
