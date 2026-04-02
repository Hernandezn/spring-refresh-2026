package dev.hernandezn.spring2026.repo;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import dev.hernandezn.spring2026.entity.ServerRunHistory;
import jakarta.transaction.Transactional;

@Repository
public interface ServerRunHistoryRepo extends CrudRepository<ServerRunHistory, Long> {
	
	@Transactional
	@Modifying
	@Query(
		"UPDATE ServerRunHistory a"
		+ " SET a.shutdownStatusCode = :statusCode,"
		+ " a.shutdownTime = :shutdownTime"
		+ " WHERE a.id = :id"
	)
	int updateShutdownStatusById(Long id, Short statusCode, LocalDateTime shutdownTime);
}
