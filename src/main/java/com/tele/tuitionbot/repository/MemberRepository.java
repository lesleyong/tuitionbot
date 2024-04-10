package com.tele.tuitionbot.repository;

import com.tele.tuitionbot.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByTelegramId(Long telegramId);
}
