package com.tele.tuitionbot.repository;

import com.tele.tuitionbot.entity.PollQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollQuestionRepository extends JpaRepository<PollQuestion, Long> {
}
