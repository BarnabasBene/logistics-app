package hu.cubix.logistics.bbarnabas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Section {
	
	@Id
    @GeneratedValue
    private Long id;
    
    private int sectionNumber;

    @ManyToOne
    private TransportPlan transportPlan;

    @ManyToOne
    private Milestone startMilestone;

    @ManyToOne
    private Milestone endMilestone;
    
    
	public Section() {
		
	}

	public Section(Long id, int sectionNumber, TransportPlan transportPlan, Milestone startMilestone,
			Milestone endMilestone) {
		
		this.id = id;
		this.sectionNumber = sectionNumber;
		this.transportPlan = transportPlan;
		this.startMilestone = startMilestone;
		this.endMilestone = endMilestone;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getSectionNumber() {
		return sectionNumber;
	}

	public void setSectionNumber(int sectionNumber) {
		this.sectionNumber = sectionNumber;
	}

	public TransportPlan getTransportPlan() {
		return transportPlan;
	}

	public void setTransportPlan(TransportPlan transportPlan) {
		this.transportPlan = transportPlan;
	}

	public Milestone getStartMilestone() {
		return startMilestone;
	}

	public void setStartMilestone(Milestone startMilestone) {
		this.startMilestone = startMilestone;
	}

	public Milestone getEndMilestone() {
		return endMilestone;
	}

	public void setEndMilestone(Milestone endMilestone) {
		this.endMilestone = endMilestone;
	}
    
    
}
