package hu.cubix.logistics.bbarnabas.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


import hu.cubix.logistics.bbarnabas.dto.AddressDto;
import hu.cubix.logistics.bbarnabas.mapper.AddressMapper;
import hu.cubix.logistics.bbarnabas.model.Address;
import hu.cubix.logistics.bbarnabas.service.AddressService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/addresses") 
public class AddressController {
	
	@Autowired
	AddressService addressService;
	
	@Autowired
	AddressMapper addressMapper;
	
	@PostMapping
	public AddressDto create(@RequestBody @Valid AddressDto addressDto) {
		if (addressDto.getId() != null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST); 
		} else {
			return addressMapper.addressToDto(addressService.save(addressMapper.dtoToAddress(addressDto)));
		}
		
	}
	
	@GetMapping  
	public List<AddressDto> getAllAddress(){  
		
		return addressMapper.addressesToDtos(addressService.findAll());
	}
	
	@GetMapping("/{id}")
	public AddressDto findById(@PathVariable Long id) {
		Address address = findByIdOrThrow(id);
		return addressMapper.addressToDto(address);	
	}

	private Address findByIdOrThrow(Long id) {
		return addressService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}
	
	
	@PutMapping("/{id}")
	public ResponseEntity<AddressDto> update(@PathVariable long id, @Valid @RequestBody AddressDto addressDto) {
		
		if (addressDto.getId() != null && !addressDto.getId().equals(id)){
			return ResponseEntity.badRequest().body(null);   
		} else {
			addressDto.setId(id);
			Address updatedAddress = addressService.update(addressMapper.dtoToAddress(addressDto));
			
			if (updatedAddress == null) {
				return ResponseEntity.notFound().build();  
			} else {
				return ResponseEntity.ok(addressMapper.addressToDto(updatedAddress));
			}
		}
		
	}
	
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		addressService.delete(id);
	}
	
	@PostMapping("/search")
	public List<AddressDto> findByExample(@RequestBody AddressDto example, @SortDefault("id") Pageable pageable, HttpServletResponse response) {
				
		if(example.getCity() == null && example.getStreet() == null && example.getZipCode() == null && example.getCountryCode() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST); 
		} else {
			
			Page<Address> page = addressService.findAddressesByExample(example, pageable);
			
			response.setHeader("X-Total-Count", String.valueOf(page.getTotalElements()));
			
			return addressMapper.addressesToDtos(page.getContent());
		}
		
	}
}
