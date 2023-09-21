package hu.cubix.logistics.bbarnabas.controller;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import hu.cubix.logistics.bbarnabas.dto.DelayDto;
import hu.cubix.logistics.bbarnabas.service.MilestoneNotPartOfSectionsException;
import hu.cubix.logistics.bbarnabas.service.TransportPlanService;

@RestController
@RequestMapping("/api/transportPlans") 
public class TransportPlanController {
	
	@Autowired
    private TransportPlanService transportPlanService;

    @PostMapping("/{transportPlanId}/delay")
    public void registerDelay(@PathVariable Long transportPlanId, @RequestBody DelayDto delayDto) {
    	
    	try {
    		transportPlanService.registerDelay(transportPlanId, delayDto);
    	} catch (MilestoneNotPartOfSectionsException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);  
		} catch (NoSuchElementException e) {
    		throw new ResponseStatusException(HttpStatus.NOT_FOUND);  
		}
    }    
}
