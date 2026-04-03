package dev.hernandezn.spring2026.repo;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dev.hernandezn.spring2026.entity.UptimeHistory;
import jakarta.transaction.Transactional;

@Repository
public interface UptimeHistoryRepo extends JpaRepository<UptimeHistory, Long> {
	
	@Transactional
	@Modifying
	@Query(
		"UPDATE UptimeHistory a"
		+ " SET a.shutdownStatusCode = :statusCode,"
		+ " a.shutdownTime = :shutdownTime"
		+ " WHERE a.id = :id"
	)
	int updateShutdownStatusById(Long id, Short statusCode, LocalDateTime shutdownTime);
}
