package com.meepalika.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.meepalika.entity.Feedback;

@Repository
public interface FeedbackDAO extends JpaRepository<Feedback, Long> {

	Feedback findById(int id);

	List<Feedback> findAllByUserIdOrderByIdDesc(int id);

}
