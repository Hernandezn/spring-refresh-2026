package dev.hernandezn.spring2026.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.hernandezn.spring2026.entity.RequestHistory;

public interface RequestHistoryRepo extends JpaRepository<RequestHistory, Long> {

}
