package com.example.mycoupon.domain.member.repository;

import com.example.mycoupon.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findBySocialId(String SocialId);
    Optional<Member> findById(Long id);
}
