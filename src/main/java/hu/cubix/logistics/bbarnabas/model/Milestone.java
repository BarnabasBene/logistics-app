package hu.cubix.logistics.bbarnabas.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Milestone {
	
	@Id
    @GeneratedValue
    private Long id;
    
    private LocalDateTime plannedTime;

    @ManyToOne
    private Address address;

    
	public Milestone() {
		
	}

	public Milestone(Long id, LocalDateTime plannedTime, Address address) {
		
		this.id = id;
		this.plannedTime = plannedTime;
		this.address = address;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getPlannedTime() {
		return plannedTime;
	}

	public void setPlannedTime(LocalDateTime plannedTime) {
		this.plannedTime = plannedTime;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
    
    
}
