package hu.cubix.logistics.bbarnabas.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.cubix.logistics.bbarnabas.dto.DelayDto;
import hu.cubix.logistics.bbarnabas.model.Milestone;
import hu.cubix.logistics.bbarnabas.model.Section;
import hu.cubix.logistics.bbarnabas.model.TransportPlan;
import hu.cubix.logistics.bbarnabas.repository.MilestoneRepository;
import hu.cubix.logistics.bbarnabas.repository.TransportPlanRepository;

@Service
public class TransportPlanService { 
	
	@Autowired
	TransportPlanRepository transportPlanRepository;
	
	@Autowired
	MilestoneRepository milestoneRepository;
	
	@Autowired
	ExpectedIncomeService expectedIncomeService;
	
	@Transactional
	public TransportPlan save(TransportPlan transportPlan) {
		return transportPlanRepository.save(transportPlan);
	}
	
	public List<TransportPlan> findAll(){
		return transportPlanRepository.findAll();
	}
	
	public Optional<TransportPlan>findById(Long id){
		return transportPlanRepository.findById(id);
	}
	
	@Transactional
	public TransportPlan update (TransportPlan transportPlan) {
		if(!transportPlanRepository.existsById(transportPlan.getId()))
			return null;
		return transportPlanRepository.save(transportPlan);
		
	}
	
	@Transactional
	public void delete(Long id) {
		transportPlanRepository.deleteById(id);
	}
	
	@Transactional 
	@PreAuthorize("hasAuthority('TransportManager')")  
	public void registerDelay(Long transportPlanId, DelayDto delayDto) { 
		
		Optional<TransportPlan> transportPlanOptional = transportPlanRepository.findById(transportPlanId);
		Optional<Milestone> milestoneOptional = milestoneRepository.findById(delayDto.getMilestoneId());
		
        if (transportPlanOptional.isPresent()) {
        	
            TransportPlan transportPlan = transportPlanOptional.get();

            Milestone milestoneToUpdate = null;
            
            for (Section section : transportPlan.getSections()) {
            	
                if (section.getStartMilestone().getId() == delayDto.getMilestoneId()) {
                	
                    milestoneToUpdate = section.getEndMilestone();
                    break;
                }
                if (section.getEndMilestone().getId() == delayDto.getMilestoneId()) {
                	
                    int nextSectionIndex = transportPlan.getSections().indexOf(section) + 1;
                    
                    if (nextSectionIndex < transportPlan.getSections().size()) {
                    	
                        milestoneToUpdate = transportPlan.getSections().get(nextSectionIndex).getStartMilestone();
                        break;
                    }
                }
                
            }
            
            if(milestoneToUpdate == null && milestoneOptional.isPresent()) {
            	throw new MilestoneNotPartOfSectionsException("Milestone nem része az adott transportplan section-jeinek.");
            } else if (!milestoneOptional.isPresent()){
            	throw new NoSuchElementException();
            }
            
            if (milestoneToUpdate != null) {
            	
                int delay = delayDto.getDelayInMinutes();
                
                LocalDateTime plannedTime = milestoneToUpdate.getPlannedTime();
                LocalDateTime newPlannedTime = plannedTime.plusMinutes(delay);
                
                milestoneToUpdate.setPlannedTime(newPlannedTime);
                
                double expectedIncome = transportPlan.getExpectedIncome();
                
                double newExpectedIncome = expectedIncomeService.expectedIncomeByDelay(delay, expectedIncome);
                
                transportPlan.setExpectedIncome(newExpectedIncome);

            }
            
        } else {
        	throw new NoSuchElementException();
        	
        }
        
	}
	
}
