package dev.hernandezn.spring2026.repo;

import org.springframework.data.repository.CrudRepository;

import dev.hernandezn.spring2026.entity.RequestHistory;

public interface RequestHistoryRepo extends CrudRepository<RequestHistory, Long> {

}
