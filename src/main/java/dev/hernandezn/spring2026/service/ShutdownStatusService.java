package dev.hernandezn.spring2026.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.hernandezn.spring2026.entity.ShutdownStatus;
import dev.hernandezn.spring2026.repo.ShutdownStatusRepo;
import jakarta.transaction.Transactional;

/**
 * Sets up the ShutdownStatus lookup table.
 * 
 * Not something that exists in a live service, only for the local database being used by this development sandbox, to satisfy foreign key constraints.
 */
@Service
public class ShutdownStatusService {
	@Autowired
	private ShutdownStatusRepo shutdownStatusRepo;
	
	@Transactional
	public void initializeStatuses() {
		shutdownStatusRepo.saveAll(
			List.of(
				new ShutdownStatus((short)0, "OK"),
				new ShutdownStatus((short)1, "UNPLANNED")
			)
		);
	}
}
