package hu.cubix.logistics.bbarnabas.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hu.cubix.logistics.bbarnabas.model.TransportPlan;


public interface TransportPlanRepository extends JpaRepository<TransportPlan, Long>{
		
	@Query("Select distinct tp from TransportPlan tp left join fetch tp.sections")	
	Optional<TransportPlan> findByIdWithSections(Long id);
	
}
