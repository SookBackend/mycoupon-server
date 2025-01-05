package com.example.mycoupon.domain.member;

import com.example.mycoupon.domain.oauth.entity.ProviderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByName(String name);
    Optional<Member> findByRefreshToken(String accessToken);
    Optional<Member> findByProviderIdAndProviderType(String providerId, ProviderType providerType);
}
