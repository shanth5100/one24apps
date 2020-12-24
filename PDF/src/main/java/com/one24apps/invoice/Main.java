package com.one24apps.invoice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.one24apps.invoice.service.InvoiceService;
import com.one24apps.invoice.service.impl.InvoiceServiceImpl;

public class Main {

//	static Logger logger = LoggerFactory.getLogger(InvoiceServiceImpl.class);
	
	public static void main(String[] args) {
		List<InvoiceItem> invoiceItemsList = new ArrayList<InvoiceItem>();
		
		InvoiceItem invoiceItems = new InvoiceItem();
		invoiceItems.setDetails("Rent");
		invoiceItems.setGst(18);
		invoiceItems.setPrice(50000);
		invoiceItems.setQuantity(2);
		InvoiceItem invoiceItems1 = new InvoiceItem();
		invoiceItems1.setDetails("Rent");
		invoiceItems1.setGst(18.1f);
		invoiceItems1.setPrice(120);
		invoiceItems1.setQuantity(1);
		InvoiceItem invoiceItems2 = new InvoiceItem();
		invoiceItems2.setDetails("Rent");
//		invoiceItems2.setGst(18);
		invoiceItems2.setPrice(120);
		invoiceItems2.setQuantity(1);
		
		invoiceItemsList.add(invoiceItems);
//		invoiceItemsList.add(invoiceItems1);
		invoiceItemsList.add(invoiceItems2);
		
		Address address = new Address();
		address.setArea("KPHB");
		address.setHouseNum("Geekspace Bussines Centre");
		address.setCity("Hyderabad");
		address.setState("Telangana");
		address.setPostCode("500072");
		address.setCuntry("India");
		
		Client client = new Client();
		client.setClientName("clientName");
		client.setAddressDetails(address);
		client.setEmail("prashant@mail.124apps.com");
		client.setGstNo("36AAHCG1369G1ZK");
		client.setWebsite("www.124apps.com");
		
		Invoice invoice = new Invoice();
		invoice.setClient(client);
		invoice.setInvoiceItemList(invoiceItemsList);
		invoice.setDate(new Date());
		invoice.setInvoiceNo(123l);
		
		InvoiceService invoiceService = new InvoiceServiceImpl();
		if (invoice != null && invoice.getInvoiceItemList()!=null && !invoice.getInvoiceItemList().isEmpty() && invoice.getClient() != null && invoice.getClient().getAddressDetails() != null) {
			invoiceService.genarateAndDownloadPDF(invoice);
		} 
		
//		invoiceService.genarateAndDownloadPDF(client);
	}

}
