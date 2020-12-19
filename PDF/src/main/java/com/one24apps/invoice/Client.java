package com.one24apps.invoice;

public class Client {
	private String clientName;
	private Address addressDetails;
	private String email;
	private String website;
	
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	
	public Address getAddressDetails() {
		return addressDetails;
	}
	public void setAddressDetails(Address addressDetails) {
		this.addressDetails = addressDetails;
	}
	
	
}
