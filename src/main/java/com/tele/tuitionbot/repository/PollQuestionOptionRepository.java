package com.tele.tuitionbot.repository;

import com.tele.tuitionbot.entity.PollQuestionOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollQuestionOptionRepository extends JpaRepository<PollQuestionOption, Long> {
}
