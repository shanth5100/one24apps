package com.one24apps.invoice.service.impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.one24apps.invoice.Address;
import com.one24apps.invoice.Client;
import com.one24apps.invoice.Invoice;
import com.one24apps.invoice.InvoiceItem;
import com.one24apps.invoice.service.InvoiceService;

//import io.woo.htmltopdf.HtmlToPdf;
//import io.woo.htmltopdf.HtmlToPdfObject;

public class InvoiceServiceImpl implements InvoiceService{
	

	public void genarateAndDownloadPDF(Invoice invoice) {
		if (invoice != null) {
			if (invoice.getClient() != null && invoice.getClient().getClientName() != null && invoice.getInvoiceItemList() != null && !invoice.getInvoiceItemList().isEmpty()) {
				
				String htmlTemplate = getHtmlTemplate(invoice);
				String currentMonth = getCurrentMonth();
				
				String destination = System.getProperty("user.home") + 
						"/Downloads/" + invoice.getClient().getClientName() + currentMonth + "AN.pdf";
//				String destination = System.getProperty("user.home") + "/Downloads/" + "one1.pdf";
				Document document = null;
				try {
					Image image = null;
					document = new Document();
					PdfWriter.getInstance(document, new FileOutputStream(destination));
					document.open();
					Font font = new Font(Font.HELVETICA, 12, Font.BOLD);
					Font text = new Font(Font.HELVETICA, 10);
					Font boldText = new Font(Font.HELVETICA, 10, Font.BOLD);
					Paragraph para = new Paragraph(" ", font);
				    document.add(para);
//					Table table = new Table(5);
				    try {
						image = Image.getInstance(getClass().getClassLoader().getResource("Geekspace.jpg"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				    
				    PdfPTable organisationInfo = new PdfPTable(2);
				    organisationInfo.getDefaultCell().setBorderWidth(0f);
				    organisationInfo.setWidthPercentage(100f);
				    organisationInfo.setWidths(new int[] {350, 100});
				    
				    PdfPCell cell1 = new PdfPCell();
				    cell1.setBorderWidth(0f);
				    cell1.addElement(new Paragraph(new Phrase("Geekspace Business Centre", font)));
				    cell1.addElement(new Paragraph());
				    cell1.addElement(new Paragraph(new Phrase("Plot No: 1204", text)));
				    cell1.addElement(new Paragraph("Manjeera Trinity Corporate"));
				    cell1.addElement(new Paragraph("Kukatpally"));
				    cell1.addElement(new Paragraph("Hyderabad"));
				    cell1.addElement(new Paragraph("Telangana, 500072"));

				    PdfPCell cell2 = new PdfPCell();
				    cell2.setBorderWidth(0f);
				    cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
				    cell2.setVerticalAlignment(Element.ALIGN_RIGHT);
				    cell2.addElement(new Paragraph(new Phrase("INVOICE", font)));
				    cell2.addElement(new Paragraph(" "));
				    cell2.addElement(image);
				    
				    
				    organisationInfo.addCell(cell1);
				    organisationInfo.addCell(cell2);
				    document.add(organisationInfo);
				    document.add(para); 
				    
//				    Billing address Table
				    PdfPTable billingAddress = new PdfPTable(3);
				    billingAddress.getDefaultCell().setBorderWidth(0f);
				    billingAddress.setWidthPercentage(100f);
				    
//				    Table headers
				    billingAddress.addCell(new Phrase("BILL TO", font));
				    billingAddress.addCell(new Phrase("SHIP TO", font));
				    billingAddress.addCell("");
//				    Table Data
				    Client client = invoice.getClient();
				    // Row 1
				    billingAddress.addCell(client.getAddressDetails().getHouseNum());
				    billingAddress.addCell(client.getClientName());
				    billingAddress.addCell("INVOICE No : " + invoice.getInvoiceNo());
				    // Row 2
				    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/YYYY");
					String date = dateFormat.format(invoice.getDate());
				    billingAddress.addCell(client.getAddressDetails().getArea());
				    billingAddress.addCell(client.getEmail());
				    billingAddress.addCell("DATE : " + date);
				    // Row 3
				    billingAddress.addCell(client.getAddressDetails().getCity());
				    billingAddress.addCell(client.getWebsite());
				    billingAddress.addCell("GSTIN : " + client.getGstNo());
				    // Row 4
				    billingAddress.addCell(client.getAddressDetails().getState() + " - " + client.getAddressDetails().getPostCode());
				    billingAddress.addCell(client.getAddressDetails().getHouseNum());
				    billingAddress.addCell("");
				    // Row 5
				    billingAddress.addCell(client.getAddressDetails().getCuntry());
				    billingAddress.addCell(client.getAddressDetails().getArea());
				    billingAddress.addCell("");
				 // Row 5
				    billingAddress.addCell("");
				    billingAddress.addCell(client.getAddressDetails().getCity());
				    billingAddress.addCell("");
				 // Row 5
				    billingAddress.addCell("");
				    billingAddress.addCell(client.getAddressDetails().getState() + " - " + client.getAddressDetails().getPostCode());
				    billingAddress.addCell("");
				 // Row 5
				    billingAddress.addCell("");
				    billingAddress.addCell(client.getAddressDetails().getCuntry());
				    billingAddress.addCell("");
				    
				    document.add(billingAddress);
				    document.add(para);
				    
//				    Invoice Item Table
					PdfPTable table = new PdfPTable(5);
					table.getDefaultCell().setBorderWidth(0f); // makes no cell borders
					table.setWidthPercentage(100f);
					table.setWidths(new int[] {300, 100,100,100,100});
				    // Setting table headers
//				    Cell cell = new Cell();
////				    cell.setHeader(true);
//				    cell.setVerticalAlignment(VerticalAlignment.CENTER);
//				    cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
//				    cell.setColspan(5);
//				    cell.setBackgroundColor(Color.LIGHT_GRAY);
//				    table.addCell(cell);
				    
					
				    table.addCell(new Phrase("Description", font));
				      table.addCell(new Phrase("Quantity", font));          
				      table.addCell(new Phrase("GST%", font));          
				      table.addCell(new Phrase("Price", font));          
				      table.addCell(new Phrase("Total", font));
//				      table.endHeaders();
				      
				      for (InvoiceItem invoiceItem : invoice.getInvoiceItemList()) {
				    	String gst = Float.toString(invoiceItem.getGst());
						table.addCell(new Phrase(invoiceItem.getDetails(), text));
						table.addCell(new Phrase(Integer.toString(invoiceItem.getQuantity()), text));
						table.addCell(new Phrase(gst, text));
						table.addCell(new Phrase(Double.toString(invoiceItem.getPrice()), text));
						table.addCell(new Phrase(Double.toString(invoiceItem.getPrice() * invoiceItem.getQuantity()), text));					
					}
				      document.add(table);
				      
				      Double subTotal = getSubTotal(invoice.getInvoiceItemList());
				      Double gstAmount = getTGstAmount(invoice.getInvoiceItemList());
				      double total_amount = subTotal + gstAmount;
				      
				      PdfPTable dueTable = new PdfPTable(5);
				      dueTable.getDefaultCell().setBorderWidth(0f); // makes no cell borders
				      dueTable.setWidthPercentage(100f);
				      dueTable.setWidths(new int[] {300, 100,100,100,100}); // sets col widths
				      
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
				      
				      document.add(dueTable);
				      document.close();
				} catch (DocumentException | FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			} 
		} 

	}
	
	@Override
	public void genaratePDF(Invoice invoice) {
		if (invoice != null) {
			if (invoice.getClient() != null && invoice.getClient().getClientName() != null && invoice.getInvoiceItemList() != null && !invoice.getInvoiceItemList().isEmpty()) {
				
				String htmlTemplate = getHtmlTemplate(invoice);
				String currentMonth = getCurrentMonth();
				
				String destination = System.getProperty("user.home") + 
						"/Downloads/" + invoice.getClient().getClientName() + currentMonth + "12.pdf";
				
				Document document = null;
				try {
					Image image = null;
					document = new Document();
					PdfWriter.getInstance(document, new FileOutputStream(destination));
					document.open();
					Font font = new Font(Font.HELVETICA, 12, Font.BOLD);
					Paragraph para = new Paragraph("Hello World PDF created using OpenPDF", font);
				    document.add(para);
				    try {
						image = Image.getInstance(getClass().getClassLoader().getResource("Geekspace.jpg"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				    PdfPTable organisationInfo = new PdfPTable(2);
				    organisationInfo.setWidthPercentage(100f);
				    organisationInfo.getDefaultCell().setBorderWidth(0f);
				    organisationInfo.addCell(new Phrase("Geekspace Business Centre", font));
				    PdfPCell cell = new PdfPCell();
				    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				    cell.setFixedHeight(100);
				    cell.addElement(image);
//				    cell.addElement(new Phrase("INVOICE", font));
				    cell.setBorder(0);
				    organisationInfo.addCell(cell);
				    document.add(organisationInfo);
					PdfPTable table = new PdfPTable(5);
					table.getDefaultCell().setBorderWidth(0f); // makes no cell borders
									
				    table.addCell(new Phrase("Description", font));
				      table.addCell(new Phrase("Quantity", font));          
				      table.addCell(new Phrase("GST%", font));          
				      table.addCell(new Phrase("Price", font));          
				      table.addCell(new Phrase("Total", font));
//				      table.endHeaders();
				      
				      List<InvoiceItem> list = invoice.getInvoiceItemList();
				      for (InvoiceItem invoiceItem : invoice.getInvoiceItemList()) {
						table.addCell(invoiceItem.getDetails());
						table.addCell(Integer.toString(invoiceItem.getQuantity()));
						table.addCell(Double.toString(invoiceItem.getGst()));
						table.addCell(Double.toString(invoiceItem.getPrice()));
						table.addCell(Double.toString(invoiceItem.getPrice()));					
					}
				      document.add(table);
				      document.close();
				} catch (DocumentException | FileNotFoundException e) {
					e.printStackTrace();
				}
				
			} 
		} 
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

	private String getHtmlTemplate(Invoice invoice) {
		String bootstrapLink = "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css\">";
		
		String organisation = getOrganization();
		String clientInfo = getClientInfo(invoice);
		String styles = getStyles();
		String invoiceTable = getInvoiceTable(invoice.getInvoiceItemList());

		String htmlTemplate = "<!DOCTYPE html><html><head><meta charset=\"UTF-8\">"+
				bootstrapLink + styles +
				"</head><body style=\"margin: 2px;\">" + 
				organisation + clientInfo + // fromTO + 
				invoiceTable + // getFooter +
				"</body></html>";
		return htmlTemplate;
//		String getFooter = getFooter();
//		String fromTO = getFromTo(invoice);
//		String htmlText = getHtmlText();
//		return htmlText;
		
	}

	private String getOrganization() {
		URL url = getClass().getClassLoader().getResource("Geekspace.jpg");
		return "	<div style=\"padding-right:10px; padding-left:10px; \">\n" + 
				"		<div class=\"row\">\n" + 
				"			<div class=\"column\">\n" + 
				"		      <p style=\"text-align: left; font-family: serif; font-size: 20px;\"> <b> Geekspace Business Centre </b> </p>\n" + 
				"		      	Plot No: 1204 <br>\n" + 
				"		       	Manjeera Trinity Corporate <br>\n" + 
				"		       	Kukatpally  <br>\n" + 
				"				Hyderabad <br>\n" + 
				"				Telangana, 500072 <br ><br >\n" + 
				" 				<p><b>GSTIN: </b>36AAHCG1369G1ZK <br> <b>CIN: </b>U74999TG2018PTC121594</p>" +
				"		    </div>\n" + 
				"			<div class=\"column\">\n" + 
				"				<p style=\"text-align: right; font-family: serif; font-size: 20px;\"> <b>INVOICE</b> </p><br>\n" + 
				"				<img style=\"margin-left: 250px;\" src='" + url + "' alt=\"Geekspace\" width=\"200\" height=\"133\"/>" +
				"			</div>\n" + 
				"		</div>\n" + 
				"	</div> <br>\n";
	}
	
	private String getClientInfo(Invoice invoice) {
		String addressDetails = getAddressDetails(invoice.getClient().getAddressDetails());
		String shipTo = getShipTO(invoice.getClient());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/YYYY");
		String date = dateFormat.format(invoice.getDate());
		return "	<div style=\"padding-right:10px; padding-left:10px; \">\n" + 
				"		<div class=\"row\">\n" + 
				"			<div class=\"spec-column-low\">\n" + 
				"				<div class=\"row\"> <span><p style=\"padding-left:18px\"><b>BILL TO</b></p></span></div>\n" + addressDetails +
				"		    </div>\n" + shipTo +
				"		<div class=\"spec-column-low\">\n" + 
				"				<span style=\"text-align: right;\">" +
				"		      	<b >INVOICE NO:</b></span> &nbsp;" +  invoice.getInvoiceNo() + "<br>\n" +
				"		       	<span style=\"text-align: right;\">\n" + 
				"				<b>DATE:</b></span> &nbsp;" + date + " <br>\n" +
				"		       	<span style=\"text-align: right;\">\n" + 
				"				<b>GSTIN:</b></span> &nbsp;" + invoice.getClient().getGstNo() + " <br>\n" +
				"		    </div>\n" + 
				"		</div>\n" + 
				"	</div> <br>\n";
	}
	
	

	private String getAddressDetails(Address address) {
		return "" + address.getHouseNum() + ", " + address.getArea() + "<br>\n" + 
				address.getCity() + "<br>\n" + 
				address.getState() +  ", " + address.getPostCode() + " <br>\n" + 
				address.getCuntry() + "<br>\n";
	}

	private String getShipTO(Client client) {
		return " <div class=\"spec-column-low\">\n" + 
				"	<div class=\"row\" > <span><p style=\"padding-left:18px\"><b>SHIP TO</b></p></span></div>\n" + 
					client.getClientName() + " <br>\n" + 
					client.getEmail() + " <br>\n" + 
					client.getWebsite() + " <br>\n" + 
					getAddressDetails(client.getAddressDetails()) +
				" </div>\n";
}

	private String getInvoiceTable(List<InvoiceItem> list) {
		
		String tableRows = getTableRows(list);
		String tableFooter = getTableFooter(list);
		
		String invoiceItems = "<div style=\"padding-left: -10px;\"><table style=\"width: 100%;\">" + //   padding-top: -80;
				" 			<thead><tr style=\"background-color: #b3fcfb;\">" + 
				"			      <th style=\"width: 50%; padding-left: 10px;\">Description</th>" + 
				"			      <th>Quantity</th>" + 
				"			      <th style=\"width: 8%;\">Gst(%)</th>" + 
				"			      <th>Price</th>" + 
				"			      <th>Total</th>" + 
				"			    </tr>" + 
				" 			<thead><tbody>" + tableRows + "</tbody>" + 
				tableFooter +
				"</table> </div>";
		return invoiceItems;
	}

	private String getTableRows(List<InvoiceItem> list) {
		String getRows = "";
		for (InvoiceItem invoiceItem : list) {
			Double amount = invoiceItem.getQuantity() * invoiceItem.getPrice();
			getRows += "<tr><td style=\"padding-left: 10px;\">" + invoiceItem.getDetails() + "</td>\n" + 
					"<td>" + invoiceItem.getQuantity() + "</td>\n" + 
					"<td>" + invoiceItem.getGst() + "</td>\n" + 
					"<td>" + invoiceItem.getPrice() + "</td>\n" +
					"<td>" + amount + "</td></tr>\n";
		}
		getRows += "<tr><td style=\"padding-left: 10px;\"> &nbsp; </td>\n" + 
				"<td></td>\n" + 
				"<td></td>\n" + 
				"<td></td></tr>\n";
		return getRows;
	}

	private String getTableFooter(List<InvoiceItem> list) {
		Double subTotal = getSubTotal(list);
		Double gstAmount = getTGstAmount(list);
		double total_amount = subTotal + gstAmount;
		String getTableFooter = "<tfoot>\n" + 
				"				<tr>\n" + 
				"			      <td></td>\n" + 
				"			      <td></td>\n" + 
				"			      <td></td>\n" + 
				"			      <td>SUBTOTAL</td>\n" + 
				"			      <td>"+ subTotal +"</td>\n" + 
				"			    </tr>\n" + 
				"				<tr>\n" + 
				"			      <td></td>\n" + 
				"			      <td></td>\n" + 
				"			      <td></td>\n" + 
				"			      <td>GST</td>\n" + 
				"			      <td>"+ gstAmount +"</td>\n" + 
				"			    </tr>\n" + 
				"				<tr>\n" + 
				"			      <td></td>\n" + 
				"			      <td></td>\n" + 
				"			      <td></td>\n" + 
				"			      <td>TOTAL</td>\n" + 
				"			      <td>"+ total_amount +"</td>\n" + 
				"			    </tr>\n" + 
				"				<tr>\n" + 
				"			      <td></td>\n" + 
				"			      <td></td>\n" + 
				"			      <td></td>\n" + 
				"			      <td><b>BALANCE DUE</b></td>\n" + 
				"			      <td><b>"+ total_amount +"</b></td>\n" + 
				"			    </tr>\n" + 
				"			</tfoot>";
		return getTableFooter;
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

	private String getStyles() {
		return "<style>\n" + 
				"* {\n" + 
				"  box-sizing: border-box;\n" + 
				"}\n" + 
				".column {\n" + 
				"  float: left;\n" + 
				"  width: 50%;\n" + 
				"  padding: 5px;\n" + 
				"}\n" + 
				".spec-column {\n" + 
				"  float: left;\n" + 
				"  width: 70%;\n" + 
				"  padding: 5px;\n" + 
				"}\n" + 
				".spec-column-low {\n" + 
				"  float: left;\n" + 
				"  width: 30%;\n" + 
				"  padding: 5px;\n" + 
				"}\n" + 
				"\n" + 
				".spec-row {\n" + 
				"  padding: -15px;" +
				"}\n" + 
				".row:after {\n" + 
				"  content: \"\";\n" + 
				"  display: table;\n" + 
				"  clear: both;\n" + 
				"}\n" + 
				".column-row:after {\n" + 
				"  content: \"\";\n" + 
				"  display: table;\n" + 
				"  clear: both;\n" + 
				"}\n" + 
				"footer {\n" + 
				"  text-align: center;\n" + 
				"  padding: 3px;\n" + 
				"  background-color: DarkSalmon;\n" + 
				"  color: white;\n" + 
				"}" +
				" #table {\n" + 
				"  background-color: #dff0f2;\n" + 
				"}" +
				"</style>";
	}
}
