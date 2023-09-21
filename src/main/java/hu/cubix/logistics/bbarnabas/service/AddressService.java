package hu.cubix.logistics.bbarnabas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import hu.cubix.logistics.bbarnabas.dto.AddressDto;
import hu.cubix.logistics.bbarnabas.model.Address;
import hu.cubix.logistics.bbarnabas.repository.AddressRepository;

@Service
public class AddressService {
	
	@Autowired
	AddressRepository addressRepository;
	
	@Transactional 
	@PreAuthorize("hasAuthority('AddressManager')")  
	public Address save(Address address) {
		return addressRepository.save(address);
	}
	
	public List<Address> findAll(){
		return addressRepository.findAll();
	}
	
	public Optional<Address>findById(Long id){
		return addressRepository.findById(id);
	}
	
	@Transactional
	@PreAuthorize("hasAuthority('AddressManager')")  
	public Address update (Address address) {
		if(!addressRepository.existsById(address.getId()))
			return null;
		return addressRepository.save(address);		
	}
	
	@Transactional
	public void delete(Long id) {
		addressRepository.deleteById(id);
	}
	
	public Page<Address> findAddressesByExample(AddressDto example, Pageable pageable){
		
		String countryCode = example.getCountryCode();
		String zipCode = example.getZipCode();
		String city = example.getCity();
		String street = example.getStreet();
		
		Specification<Address> spec = Specification.where(null);
		
		if (StringUtils.hasText(countryCode))
			spec = spec.and(AddressSpecifications.hasCountryCode(countryCode));
		
		if (StringUtils.hasText(zipCode))
			spec = spec.and(AddressSpecifications.hasZipCode(zipCode));
		
		if (StringUtils.hasText(city))
			spec = spec.and(AddressSpecifications.hasCity(city));
		
		if (StringUtils.hasText(street))
			spec = spec.and(AddressSpecifications.hasStreet(street));
			
		return addressRepository.findAll(spec, pageable);
					
	}
	
}
