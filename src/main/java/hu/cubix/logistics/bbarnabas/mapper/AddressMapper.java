package hu.cubix.logistics.bbarnabas.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import hu.cubix.logistics.bbarnabas.dto.AddressDto;
import hu.cubix.logistics.bbarnabas.model.Address;

@Mapper(componentModel= "spring")
public interface AddressMapper {
	
	List<AddressDto> addressesToDtos(List<Address>addresses);
	
	Address dtoToAddress(AddressDto addressDto);

    AddressDto addressToDto(Address address);
}
