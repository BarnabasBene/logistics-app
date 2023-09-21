package hu.cubix.logistics.bbarnabas.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

public class AddressDto {
	
    private Long id;
    
    @NotEmpty
    @NotNull
    private String countryCode;
    @NotEmpty
    @NotNull
    private String city;
    @NotEmpty
    @NotNull
    private String street;
    @NotEmpty
    @NotNull
    private String zipCode;
    @NotEmpty
    @NotNull
    private String houseNumber;
    private double latitude;
    private double longitude;
    
	public AddressDto() {
		
	}
	
	public AddressDto(String countryCode, String city, String street, String zipCode, String houseNumber,
			double latitude, double longitude) {
		
		//this.id = id;
		this.countryCode = countryCode;
		this.city = city;
		this.street = street;
		this.zipCode = zipCode;
		this.houseNumber = houseNumber;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getHouseNumber() {
		return houseNumber;
	}
	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
    
    
}

