package com.creditsuisse.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.creditsuisse.app.domain.Event;

@Repository
public interface IEventRepository extends JpaRepository<Event, Long> {

}
