package com.one24apps.invoice.service.impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.html.WebColors;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.one24apps.invoice.Client;
import com.one24apps.invoice.Invoice;
import com.one24apps.invoice.InvoiceItem;
import com.one24apps.invoice.service.InvoiceService;


public class InvoiceServiceImpl implements InvoiceService{
	
	private Font font = new Font(Font.HELVETICA, 12, Font.BOLD);
	private Font text = new Font(Font.HELVETICA, 10);
	private Font boldText = new Font(Font.HELVETICA, 10, Font.BOLD);
	
	public void genarateAndDownloadPDF(Invoice invoice) {
		if (invoice != null) {
			if (invoice.getClient() != null && invoice.getClient().getClientName() != null && invoice.getInvoiceItemList() != null && !invoice.getInvoiceItemList().isEmpty()) {
				
//				String htmlTemplate = getHtmlTemplate(invoice);
				String currentMonth = getCurrentMonth();
				
				String destination = System.getProperty("user.home") + 
						"/Downloads/" + invoice.getClient().getClientName() + currentMonth + ".pdf";
//				String destination = System.getProperty("user.home") + "/Downloads/" + "one.pdf";
				Document document = null;
				try {
					Image image = null;
					document = new Document();
					PdfWriter.getInstance(document, new FileOutputStream(destination));
					document.open();
					Paragraph para = new Paragraph(" ");
				    document.add(para);
//					Table table = new Table(5);
				    try {
						image = Image.getInstance(getClass().getClassLoader().getResource("Geekspace.jpg"));
					} catch (IOException e) {
						e.printStackTrace();
					}
				    

				    PdfPTable organisationInfo = getOrganisation(image);
				    
				    document.add(organisationInfo);
				    document.add(para); 
				    
				    
				    PdfPTable billingAddress = getBillingAddress(invoice);
				    document.add(billingAddress);
				    document.add(para);
				       
//				    Invoice Item Table
				    PdfPTable table = getInvoiceTbl(invoice);
				    document.add(table);
				    document.add(para);
				      
				    Double subTotal = getSubTotal(invoice.getInvoiceItemList());
				    Double gstAmount = getTGstAmount(invoice.getInvoiceItemList());
				    Double total_amount = subTotal + gstAmount;
				      
				    PdfPTable dueTable = getDueTable(subTotal, gstAmount, total_amount);
				    document.add(dueTable);
				      
				    document.close();
				} catch (DocumentException | FileNotFoundException e) {
					e.printStackTrace();
				}
			
			} 
		} 

	}
	
	
	
	private PdfPTable getDueTable(Double subTotal, Double gstAmount, double total_amount) {
		PdfPTable dueTable = new PdfPTable(5);
	      dueTable.getDefaultCell().setBorderWidth(0f); // makes no cell borders
	      dueTable.setWidthPercentage(100f);
	      dueTable.setWidths(new int[] {300, 100,100,130,100}); // sets col widths
	      
	      DecimalFormat decimalFormat = new DecimalFormat("0.000");
	      
	      dueTable.addCell("");
	      dueTable.addCell("");
	      dueTable.addCell("");
	      dueTable.addCell(new Phrase("SUBTOTAL", boldText));
	      dueTable.addCell(new Phrase(decimalFormat.format(subTotal) + "", text));
	      
	      dueTable.addCell("");
	      dueTable.addCell("");
	      dueTable.addCell("");
	      dueTable.addCell(new Phrase("GST", boldText));
	      dueTable.addCell(new Phrase(decimalFormat.format(gstAmount)+ "", text));
	      
	      dueTable.addCell("");
	      dueTable.addCell("");
	      dueTable.addCell("");
	      dueTable.addCell(new Phrase("TOTAL", boldText));
	      dueTable.addCell(new Phrase(decimalFormat.format(total_amount) + "", text));
	      
	      dueTable.addCell("");
	      dueTable.addCell("");
	      dueTable.addCell("");
	      dueTable.addCell(new Phrase("BALANCE DUE", boldText));
	      dueTable.addCell(new Phrase(decimalFormat.format(total_amount) + "", text));
	      
		return dueTable;
	}



	private PdfPTable getInvoiceTbl(Invoice invoice) {
	    DecimalFormat decimalFormat = new DecimalFormat("0.000");

		PdfPTable table = new PdfPTable(5);
		setTableProperties(table);
		table.setWidths(new int[] {300, 100,100,130,100});				    
		
	    table.addCell(getColoredCell("Description", font, true));
	    table.addCell(getColoredCell("Quantity", font, true));          
	    table.addCell(getColoredCell("GST%", font, true));          
	    table.addCell(getColoredCell("Price", font, true));          
	    table.addCell(getColoredCell("Total", font, true));
	      
	    for (InvoiceItem invoiceItem : invoice.getInvoiceItemList()) {
	    	String gst = Float.toString(invoiceItem.getGst());
			table.addCell(new Phrase(invoiceItem.getDetails(), text));
			table.addCell(new Phrase(Integer.toString(invoiceItem.getQuantity()), text));
			table.addCell(new Phrase(gst, text));
			table.addCell(new Phrase(decimalFormat.format(invoiceItem.getPrice()), text));
			table.addCell(new Phrase(decimalFormat.format(invoiceItem.getPrice() * invoiceItem.getQuantity()), text));					
		}
		return table;
	}

	private PdfPCell getCell() {
		PdfPCell cell = new PdfPCell();
		cell.setBorderWidth(0);
		return cell;
	}

	private PdfPCell getColoredCell(String data, Font font1) {
		return getColoredCell(data, font1, false);
	}



	private PdfPCell getColoredCell(String value, Font font, boolean color) {
		PdfPCell cell = getCell();
		if (color) {
			cell.setBackgroundColor(WebColors.getRGBColor("#d9f8fa"));
		}
		cell.addElement(new Phrase(value, font));
		return cell;
	}

	

	private PdfPTable getBillingAddress(Invoice invoice) {
		PdfPTable billingAddress = new PdfPTable(3);
		setTableProperties(billingAddress);
	    billingAddress.setWidths(new float[] {35f, 35f, 30f});
		
//	    Table headers
	    billingAddress.addCell(new Phrase("BILL TO", font));
	    billingAddress.addCell(new Phrase("SHIP TO", font));
	    billingAddress.addCell("");
//	    Table Data
	    Client client = invoice.getClient();
	    // Row 1
	    billingAddress.addCell(new Phrase(client.getAddressDetails().getHouseNum(), text));
	    billingAddress.addCell(new Phrase(client.getClientName(), text));
	    PdfPCell invoiceNo = getCell();
	    invoiceNo.setPaddingBottom(-10);
	    
	    invoiceNo.addElement(getPhrase("INVOICE No : ", boldText, invoice.getInvoiceNo().toString(), text));
	    billingAddress.addCell(invoiceNo);
	    // Row 2
	    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/YYYY");
		String date = dateFormat.format(invoice.getDate());
		
	    billingAddress.addCell(new Phrase(client.getAddressDetails().getArea(), text));
	    billingAddress.addCell(new Phrase(client.getEmail(), text));
	    PdfPCell invoiceDate = getCell();
	    invoiceDate.setPaddingBottom(-10);
	    
	    invoiceDate.addElement(getPhrase("DATE : ", boldText, date, text));
	    billingAddress.addCell(invoiceDate);
	    
	    
	    // Row 3
	    billingAddress.addCell(new Phrase(client.getAddressDetails().getCity(), text));
	    billingAddress.addCell(new Phrase(client.getWebsite(), text));
	    PdfPCell gstIn = getCell();
	    gstIn.setPaddingBottom(-10);
	    
	    gstIn.addElement(getPhrase("GSTIN : ", boldText, client.getGstNo(), text));
	    billingAddress.addCell(gstIn);
	    
	    // Row 4
	    billingAddress.addCell(new Phrase(client.getAddressDetails().getState() + " - " + client.getAddressDetails().getPostCode(), text));
	    billingAddress.addCell(new Phrase(client.getAddressDetails().getHouseNum(), text));
	    billingAddress.addCell("");
	    // Row 5
	    billingAddress.addCell(new Phrase(client.getAddressDetails().getCuntry(), text));
	    billingAddress.addCell(new Phrase(client.getAddressDetails().getArea(), text));
	    billingAddress.addCell("");
	 // Row 6
	    billingAddress.addCell("");
	    billingAddress.addCell(new Phrase(client.getAddressDetails().getCity(), text));
	    billingAddress.addCell("");
	 // Row 7
	    billingAddress.addCell("");
	    billingAddress.addCell(new Phrase(client.getAddressDetails().getState() + " - " + client.getAddressDetails().getPostCode(), text));
	    billingAddress.addCell("");
	 // Row 8
	    billingAddress.addCell("");
	    billingAddress.addCell(new Phrase(client.getAddressDetails().getCuntry(), text));
	    billingAddress.addCell("");
		return billingAddress;
	}



	private Phrase getPhrase(String string, Font boldText, String string1, Font text) {
		Phrase phrase = new Phrase();
		phrase.add(new Chunk(string, boldText));
		phrase.add(new Chunk(string1, text));
		return phrase;
	}

	private PdfPTable getOrganisation(Image image) {
		PdfPTable organisationInfo = new PdfPTable(2);
		setTableProperties(organisationInfo);
	    organisationInfo.setWidths(new int[] {350, 100});
	    
	    PdfPCell cell1 = getCell();
	    cell1.addElement(new Paragraph(new Phrase("Geekspace Business Centre", font)));
	    cell1.addElement(new Paragraph(" "));
	    cell1.addElement(new Paragraph(new Phrase("Plot No: 1204", text)));
	    cell1.addElement(new Paragraph(new Phrase("Manjeera Trinity Corporate", text)));
	    cell1.addElement(new Paragraph(new Phrase("Kukatpally", text)));
	    cell1.addElement(new Paragraph(new Phrase("Hyderabad", text)));
	    cell1.addElement(new Paragraph(new Phrase("Telangana, 500072", text)));

	    PdfPCell cell2 = getCell();
	    cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
	    cell2.setVerticalAlignment(Element.ALIGN_RIGHT);
	    cell2.addElement(new Paragraph(new Phrase("INVOICE", font)));
	    cell2.addElement(new Paragraph(" "));
	    cell2.addElement(image);
	    
	    
	    organisationInfo.addCell(cell1);
	    organisationInfo.addCell(cell2);
		return organisationInfo;
	}



	private void setTableProperties(PdfPTable pdfPTable) {
		pdfPTable.getDefaultCell().setBorderWidth(0f);
		pdfPTable.setWidthPercentage(100f);
	}



	private String getCurrentMonth() {
		String currentMonth = "";
		@SuppressWarnings("deprecation")
		int month = new Date().getMonth();
		switch (month) {
		case 1:
			currentMonth = "FEB";
			break;
		case 2:
			currentMonth = "MAR";
			break;
		case 3:
			currentMonth = "APR";
			break;
		case 4:
			currentMonth = "MAY";
			break;
		case 5:
			currentMonth = "JUN";
			break;
		case 6:
			currentMonth = "JUL";
			break;
		case 7:
			currentMonth = "AUG";
			break;
		case 8:
			currentMonth = "SEP";
			break;
		case 9:
			currentMonth = "OCT";
			break;
		case 10:
			currentMonth = "NOV";
			break;
		case 11:
			currentMonth = "DEC";
			break;
		default: currentMonth = "JAN";
			break;
		}
		return currentMonth;
	}

	private Double getSubTotal(List<InvoiceItem> list) {
		Double subTotal = 0.0;
		for (InvoiceItem invoiceItem : list) {
			subTotal += (invoiceItem.getPrice() * invoiceItem.getQuantity());
		}
		return subTotal;
	}
	
	private Double getTGstAmount(List<InvoiceItem> list) {
		Double gstAmount = 0.0;
		for (InvoiceItem invoiceItem : list) {
			if (invoiceItem.getGst()>0) {
				double gst_amount = (invoiceItem.getPrice() * invoiceItem.getQuantity() * invoiceItem.getGst())/100;
				gstAmount += gst_amount;
			}
		}
		return gstAmount;
	}
}
