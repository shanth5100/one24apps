package com.one24apps.invoice.service;

import com.one24apps.invoice.Invoice;

public interface InvoiceService {

	void genarateAndDownloadPDF(Invoice invoice);
	void genaratePDF(Invoice invoice);
}
