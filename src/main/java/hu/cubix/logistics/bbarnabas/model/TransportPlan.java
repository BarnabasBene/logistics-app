package hu.cubix.logistics.bbarnabas.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class TransportPlan {

	@Id
	@GeneratedValue
	private Long id;

	private double expectedIncome;

	@OneToMany(mappedBy = "transportPlan")
	private List<Section> sections;

	public TransportPlan() {

	}

	public TransportPlan(Long id, double expectedIncome, List<Section> sections) {

		this.id = id;
		this.expectedIncome = expectedIncome;
		this.sections = sections;
	}

	public void addSection(Section section) {

		if (this.sections == null)
			this.sections = new ArrayList<>();
		section.setSectionNumber(sections.size());
		section.setTransportPlan(this);
		sections.add(section);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getExpectedIncome() {
		return expectedIncome;
	}

	public void setExpectedIncome(double expectedIncome) {
		this.expectedIncome = expectedIncome;
	}

	public List<Section> getSections() {
		return sections;
	}

	public void setSections(List<Section> sections) {
		this.sections = sections;
	}

}
