package dev.hernandezn.spring2026.entity;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Immutable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShutdownStatus {
	@Id
	Short id;
	
	@Column(length=15)
	String status;
};
