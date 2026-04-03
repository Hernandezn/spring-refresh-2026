package dev.hernandezn.spring2026.entity;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
public class UptimeHistory {
	@Id
	@GeneratedValue(
		strategy=GenerationType.AUTO
	)
	@SequenceGenerator(
		allocationSize=1
	)
	private Long id;
	
	@Column(nullable=false)
	private LocalDateTime startupTime;
	
	@Column(name="shutdown_status_code", insertable=true, updatable=true, nullable=false)
	private Short shutdownStatusCode;
	
	@Column(nullable=true)
	private LocalDateTime shutdownTime;
	
	
	
	@ManyToOne(fetch=FetchType.EAGER) // grabs the linked entity when this entity is retrieved from the DB
	@JoinColumn(name="shutdown_status_code", insertable=false, updatable=false)
	private ShutdownStatus shutdownStatus;
}
