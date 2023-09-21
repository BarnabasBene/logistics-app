package hu.cubix.logistics.bbarnabas.web;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

import hu.cubix.logistics.bbarnabas.dto.DelayDto;
import hu.cubix.logistics.bbarnabas.dto.LoginDto;
import hu.cubix.logistics.bbarnabas.model.TransportPlan;
import hu.cubix.logistics.bbarnabas.model.Milestone;
import hu.cubix.logistics.bbarnabas.model.Section;
import hu.cubix.logistics.bbarnabas.repository.AddressRepository;
import hu.cubix.logistics.bbarnabas.repository.MilestoneRepository;
import hu.cubix.logistics.bbarnabas.repository.SectionRepository;
import hu.cubix.logistics.bbarnabas.repository.TransportPlanRepository;
import hu.cubix.logistics.bbarnabas.service.InitDbService;


@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "360000")
public class TransportPlanControllerIT {
	
	private static final String TEST_PASS = "pass";

	private static final String TEST_USERNAME = "transportManager";

	private static final String BASE_URI = "/api/transportPlans";
	
	@Autowired
	WebTestClient webTestClient;
	
	@Autowired
	TransportPlanRepository transportPlanRepository;
	
	@Autowired
	AddressRepository addressRepository;
	
	@Autowired
	MilestoneRepository milestoneRepository;
	
	@Autowired
	SectionRepository sectionRepository;
	
	@Autowired
	InitDbService initDbService;
	
	private String jwt;
	
	@BeforeEach
	void init() {
		
		sectionRepository.deleteAll();
		milestoneRepository.deleteAll();
		transportPlanRepository.deleteAll();
		addressRepository.deleteAll();
		
		initDbService.createInitialTransportPlans();	
			
		LoginDto body = new LoginDto();
		body.setUsername(TEST_USERNAME);
		body.setPassword(TEST_PASS);
		jwt = webTestClient.post()
			.uri("/api/login")
			.bodyValue(body)
			.exchange()
			.expectBody(String.class)
			.returnResult()
			.getResponseBody();
	}
	
	@Test
	void testThatRegisterDelayWasSuccessful() throws Exception {
		
		Long transportPlanId = 2L;  
		long milestoneId = 5;  
	    int delayInMinutes = 35;
	    DelayDto delayDto = new DelayDto(milestoneId, delayInMinutes);
	    
	    registerDelay(transportPlanId, delayDto).expectStatus().isOk(); 		
	}
	
	@Test
	void testThatTPlanOrMStoneIsNotExist() throws Exception {
	
		Long transportPlanId = 99L;
		long milestoneId = 5; 
	    int delayInMinutes = 35;
	    DelayDto delayDto = new DelayDto(milestoneId, delayInMinutes);
	    
	    Long transportPlanId2 = 2L; 
		long milestoneId2 = 99;
	    int delayInMinutes2 = 35;
	    DelayDto delayDto2 = new DelayDto(milestoneId2, delayInMinutes2);
	    
	    // TP is not exist
	    registerDelay(transportPlanId, delayDto).expectStatus().isNotFound(); 
	    	   
	    Optional<TransportPlan> transportPlanOptional = transportPlanRepository.findById(transportPlanId);
		
		assertThat(transportPlanOptional).isEmpty();
	
		// MS is not exist
	    registerDelay(transportPlanId2, delayDto2).expectStatus().isNotFound(); 
	    
	    Optional<Milestone> milestoneOptional = milestoneRepository.findById(milestoneId2);
		
		assertThat(milestoneOptional).isEmpty();				
	}
	
	@Test
	void testThatAddDelayToSectionEndMilestone() throws Exception {
		
		Long transportPlanId = 2L;  
		long milestoneId = 5;  
	    int delayInMinutes = 11;
	    DelayDto delayDto = new DelayDto(milestoneId, delayInMinutes);
	    
	    Milestone milestoneBeforeRegisterDelay = getMilestoneToUpdate(transportPlanId, milestoneId); // (actual section EndMilestone)

	    registerDelay(transportPlanId, delayDto).expectStatus().isOk();
	    
	    Milestone milestoneAfterRegisterDelay = getMilestoneToUpdate(transportPlanId, milestoneId); // (actual section EndMilestone)

	    assertThat(milestoneAfterRegisterDelay.getPlannedTime()).isEqualTo(milestoneBeforeRegisterDelay.getPlannedTime().plusMinutes(delayInMinutes));
	}
	
	@Test
	void testThatAddDelayToNextSectionStartMilestone() throws Exception {
		
		Long transportPlanId = 2L;  
		long milestoneId = 6;  
	    int delayInMinutes = 11;
	    DelayDto delayDto = new DelayDto(milestoneId, delayInMinutes);
	    
	    Milestone milestoneBeforeRegisterDelay = getMilestoneToUpdate(transportPlanId, milestoneId); // (next section StartMilestone)

	    registerDelay(transportPlanId, delayDto).expectStatus().isOk();
	    
	    Milestone milestoneAfterRegisterDelay = getMilestoneToUpdate(transportPlanId, milestoneId); // (next section StartMilestone)

	    assertThat(milestoneAfterRegisterDelay.getPlannedTime()).isEqualTo(milestoneBeforeRegisterDelay.getPlannedTime().plusMinutes(delayInMinutes));
	}	
	
	@Test
	void testThatExpectedIncomeDecreaseByDelayLessThan30M() throws Exception {
				
		Long transportPlanId = 2L; 
		long milestoneId = 5;  
	    int delayInMinutes = 25;
	    double decreasedExpectedIncome = 97.5;  // 2.5 % income loss
	    
	    DelayDto delayDto = new DelayDto(milestoneId, delayInMinutes);
	    
	    registerDelay(transportPlanId, delayDto).expectStatus().isOk();
	    
	    Optional<TransportPlan> transportPlanOptional = transportPlanRepository.findById(transportPlanId);
		
		assertThat(transportPlanOptional).isNotEmpty();
		TransportPlan transportPlan = transportPlanOptional.get();
		assertThat(transportPlan.getExpectedIncome()).isEqualTo(decreasedExpectedIncome);		
	}
	
	@Test
	void testThatExpectedIncomeDecreaseByDelayLessThan60M() throws Exception {	
		
		Long transportPlanId = 2L; 
		long milestoneId = 5;   
	    int delayInMinutes = 35;
	    double decreasedExpectedIncome = 95;  // 5 % income loss
	    
	    DelayDto delayDto = new DelayDto(milestoneId, delayInMinutes);
	    
	    registerDelay(transportPlanId, delayDto).expectStatus().isOk();
	    
	    Optional<TransportPlan> transportPlanOptional = transportPlanRepository.findById(transportPlanId);
		
		assertThat(transportPlanOptional).isNotEmpty();
		TransportPlan transportPlan = transportPlanOptional.get();
		assertThat(transportPlan.getExpectedIncome()).isEqualTo(decreasedExpectedIncome);		
	}
	
	@Test
	void testThatExpectedIncomeDecreaseByDelayLessThan120M() throws Exception {
				
		Long transportPlanId = 2L; 
		long milestoneId = 5;   
	    int delayInMinutes = 80;
	    double decreasedExpectedIncome = 90;  // 10 % income loss
	    
	    DelayDto delayDto = new DelayDto(milestoneId, delayInMinutes);
	    
	    registerDelay(transportPlanId, delayDto).expectStatus().isOk();
	    
	    Optional<TransportPlan> transportPlanOptional = transportPlanRepository.findById(transportPlanId);
		
		assertThat(transportPlanOptional).isNotEmpty();
		TransportPlan transportPlan = transportPlanOptional.get();
		assertThat(transportPlan.getExpectedIncome()).isEqualTo(decreasedExpectedIncome);		
	}
	
	@Test
	void testThatExpectedIncomeDecreaseByDelayMoreThan120M() throws Exception {	
		
		Long transportPlanId = 2L; 
		long milestoneId = 5;   
	    int delayInMinutes = 130;
	    double decreasedExpectedIncome = 85;  // 15 % income loss
	    
	    DelayDto delayDto = new DelayDto(milestoneId, delayInMinutes);
	    
	    registerDelay(transportPlanId, delayDto).expectStatus().isOk();
	    
	    Optional<TransportPlan> transportPlanOptional = transportPlanRepository.findById(transportPlanId);
		
		assertThat(transportPlanOptional).isNotEmpty();
		TransportPlan transportPlan = transportPlanOptional.get();
		assertThat(transportPlan.getExpectedIncome()).isEqualTo(decreasedExpectedIncome);		
	}	
	
	
    public ResponseSpec registerDelay(@PathVariable Long transportPlanId, @RequestBody DelayDto delayDto) {
		
		String path = BASE_URI + "/" + transportPlanId + "/delay";
		
		return webTestClient
				.post()
				.uri(path)
				.headers(headers -> headers.setBearerAuth(jwt))		
				.bodyValue(delayDto)
				.exchange();   
    }
    
    
    public Milestone getMilestoneToUpdate (Long transportPlanId, long milestoneId) {
    	
    	Optional<TransportPlan> transportPlanOptional = transportPlanRepository.findByIdWithSections(transportPlanId);
    	
        if (transportPlanOptional.isPresent()) {
        	
            TransportPlan transportPlan = transportPlanOptional.get();

            Milestone milestoneToUpdate = null;
            
            for (Section section : transportPlan.getSections()) {
            	
                if (section.getStartMilestone().getId() == milestoneId) {
                	
                    milestoneToUpdate = section.getEndMilestone();
                    break;
                }
                if (section.getEndMilestone().getId() == milestoneId) {
                	
                    int nextSectionIndex = transportPlan.getSections().indexOf(section) + 1;
                    
                    if (nextSectionIndex < transportPlan.getSections().size()) {
                    	
                        milestoneToUpdate = transportPlan.getSections().get(nextSectionIndex).getStartMilestone();
                        break;
                    }
                }
                
            }
           return milestoneToUpdate; 
            
        } 
        return null; 
    }
    
}
