package com.one24apps.invoice;

import java.util.Date;
import java.util.List;

public class Invoice {
	
	private Long invoiceNo;
	private Date date;
	private Client client;
	private List<InvoiceItem> invoiceItemList;
	
	public Long getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(Long invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}
	public List<InvoiceItem> getInvoiceItemList() {
		return invoiceItemList;
	}
	public void setInvoiceItemList(List<InvoiceItem> invoiceItemList) {
		this.invoiceItemList = invoiceItemList;
	}
	
	
}
