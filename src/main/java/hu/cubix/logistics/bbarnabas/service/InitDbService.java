package hu.cubix.logistics.bbarnabas.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.cubix.logistics.bbarnabas.model.Address;
import hu.cubix.logistics.bbarnabas.model.Milestone;
import hu.cubix.logistics.bbarnabas.model.Section;
import hu.cubix.logistics.bbarnabas.model.TransportPlan;
import hu.cubix.logistics.bbarnabas.repository.AddressRepository;
import hu.cubix.logistics.bbarnabas.repository.MilestoneRepository;
import hu.cubix.logistics.bbarnabas.repository.SectionRepository;
import hu.cubix.logistics.bbarnabas.repository.TransportPlanRepository;
import jakarta.transaction.Transactional;

@Service
public class InitDbService {
	
	@Autowired
	TransportPlanRepository transportPlanRepository;
	
	@Autowired
	AddressRepository addressRepository;
	
	@Autowired
	SectionRepository sectionRepository;
	
	@Autowired
	MilestoneRepository milestoneRepository;
	
	@Transactional
	public void clearDb () {
		
		sectionRepository.deleteAllInBatch();
		milestoneRepository.deleteAllInBatch();
		transportPlanRepository.deleteAllInBatch();
		addressRepository.deleteAllInBatch();
	}
	
	@Transactional
	public void createInitialTransportPlans() {
		
        TransportPlan transportPlan1 = new TransportPlan();
        transportPlan1.setExpectedIncome(100.0);
        transportPlan1.addSection(createSection1ForTransportPlan1());
        transportPlan1.addSection(createSection2ForTransportPlan1());
        
        transportPlanRepository.save(transportPlan1);
    }
	
	public Section createSection1ForTransportPlan1() {
        
        Section section1 = new Section();
        section1.setSectionNumber(1);
        section1.setStartMilestone(createMilestone1(LocalDateTime.now().plusDays(6)));
        section1.setEndMilestone(createMilestone2(LocalDateTime.now().plusDays(7)));
        
       sectionRepository.save(section1);
        return section1;
    }
	
	public Section createSection2ForTransportPlan1() {
        
        Section section2 = new Section();
        section2.setSectionNumber(2);
        section2.setStartMilestone(createMilestone3(LocalDateTime.now().plusDays(8)));
        section2.setEndMilestone(createMilestone4(LocalDateTime.now().plusDays(9)));
        
       sectionRepository.save(section2);
        return section2;
    }
	
	public Milestone createMilestone1(LocalDateTime plannedTime) {
		
        Milestone milestone = new Milestone();
        milestone.setAddress(createAddress1());
        milestone.setPlannedTime(plannedTime);
        
        milestoneRepository.save(milestone);
        return milestone;
    }
	
	public Milestone createMilestone2(LocalDateTime plannedTime) {
		
        Milestone milestone = new Milestone();
        milestone.setAddress(createAddress2());
        milestone.setPlannedTime(plannedTime);
        
        milestoneRepository.save(milestone);
        return milestone;
    }
	
	public Milestone createMilestone3(LocalDateTime plannedTime) {
		
        Milestone milestone = new Milestone();
        milestone.setAddress(createAddress3());
        milestone.setPlannedTime(plannedTime);
        
        milestoneRepository.save(milestone);
        return milestone;
    }
	
	public Milestone createMilestone4(LocalDateTime plannedTime) {
		
        Milestone milestone = new Milestone();
        milestone.setAddress(createAddress4());
        milestone.setPlannedTime(plannedTime);
        
        milestoneRepository.save(milestone);
        return milestone;
    }

    public Address createAddress1() {
    	
        Address address = new Address();
        
        address.setCity("Budapest");
        address.setCountryCode("HU");
        address.setZipCode("1163");
        address.setStreet("Sasfészek utca");
        address.setHouseNumber("17");
        address.setLatitude(47.51344);
        address.setLongitude(19.17672);
        
        addressRepository.save(address);
        
        return address;
    }
    
    public Address createAddress2() {
    	
        Address address = new Address();
        
        address.setCity("Kecskemét");
        address.setCountryCode("HU");
        address.setZipCode("6000");
        address.setStreet("Batthyány utca");
        address.setHouseNumber("5");
        address.setLatitude(46.90444);
        address.setLongitude(19.69163);
        
        addressRepository.save(address);
        
        return address;
    }
    
    public Address createAddress3() {
    	
        Address address = new Address();
        
        address.setCity("Pécs");
        address.setCountryCode("HU");
        address.setZipCode("7621");
        address.setStreet("Király utca");
        address.setHouseNumber("10");
        address.setLatitude(46.07703);
        address.setLongitude(18.23362);
        
        addressRepository.save(address);
        
        return address;
    }
    
    public Address createAddress4() {
    	
        Address address = new Address();
        
        address.setCity("Szombathely");
        address.setCountryCode("HU");
        address.setZipCode("9700");
        address.setStreet("Hargita utca");
        address.setHouseNumber("9");
        address.setLatitude(47.24043);
        address.setLongitude(16.61262);
        
        addressRepository.save(address);
        
        return address;
    }


}
