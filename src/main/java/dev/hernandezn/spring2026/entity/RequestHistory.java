package dev.hernandezn.spring2026.entity;

import java.time.LocalDateTime;

import dev.hernandezn.spring2026.enums.HttpRequestMethod;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class RequestHistory {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@SequenceGenerator(
		allocationSize=1
	)
	private Long id;
	
	@Column(name="server_run_id", insertable=true, updatable=true, nullable=false)
	private Long uptimeHistoryId;
	
	@Column(nullable=false)
	private LocalDateTime requestTime;
	
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private HttpRequestMethod httpMethod;
	
	@Column(nullable=false)
	private String pathUri;
	
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="server_run_id", insertable=false, updatable=false)
	private UptimeHistory uptimeHistory;
}
