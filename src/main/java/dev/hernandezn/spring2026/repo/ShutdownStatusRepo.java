package dev.hernandezn.spring2026.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import dev.hernandezn.spring2026.entity.ShutdownStatus;

@Repository
public interface ShutdownStatusRepo extends CrudRepository<ShutdownStatus, Short> {

}
