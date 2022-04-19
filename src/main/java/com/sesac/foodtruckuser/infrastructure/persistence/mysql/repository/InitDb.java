package com.sesac.foodtruckuser.infrastructure.persistence.mysql.repository;

import com.sesac.foodtruckuser.infrastructure.persistence.mysql.entity.Authority;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        public void dbInit1() {
            Authority role_user = Authority.builder()
                    .authorityName("ROLE_USER")
                    .build();

            Authority role_manager = Authority.builder()
                    .authorityName("ROLE_MANAGER")
                    .build();

            em.persist(role_user);
            em.persist(role_manager);
        }

    }
}
