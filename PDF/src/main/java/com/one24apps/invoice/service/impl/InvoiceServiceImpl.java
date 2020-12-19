package com.one24apps.invoice.service.impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.itextpdf.html2pdf.HtmlConverter;
import com.one24apps.invoice.Address;
import com.one24apps.invoice.Client;
import com.one24apps.invoice.Invoice;
import com.one24apps.invoice.InvoiceItem;
import com.one24apps.invoice.service.InvoiceService;

public class InvoiceServiceImpl implements InvoiceService{
	
	public void genarateAndDownloadPDF(Invoice invoice) {
		if (invoice != null) {
			if (invoice.getClient() != null && invoice.getClient().getClientName() != null && invoice.getInvoiceItemList() != null && !invoice.getInvoiceItemList().isEmpty()) {
				String htmlTemplate = getHtmlTemplate(invoice);
				
				String currentMonth = getCurrentMonth();
				try {
//					HtmlConverter.convertToPdf(htmlTemplate, new FileOutputStream( System.getProperty("user.home") +"/" + client.getClientName()+ "-" + currentMonth +".pdf"));
					HtmlConverter.convertToPdf(
							htmlTemplate, new FileOutputStream( System.getProperty("user.home") + 
									"/" + invoice.getClient().getClientName() + currentMonth + ".pdf"));
					
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} 
			} 
		} else {
			
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
		return "	<div>\n" + 
				"		<div class=\"row\">\n" + 
				"			<div class=\"column\">\n" + 
				"		      <p style=\"text-align: left; font-family: serif; font-size: 20px;\"> <b> Geekspace Business Centre </b> </p>\n" + 
				"		      	Plot No: 1204 <br>\n" + 
				"		       	Manjeera Trinity Corporate <br>\n" + 
				"		       	Kukatpally  <br>\n" + 
				"				Hyderabad <br>\n" + 
				"				Telangana, 500072\n" + 
				"		    </div>\n" + 
				"			<div class=\"column\">\n" + 
				"				<p style=\"text-align: right; font-family: serif; font-size: 20px;\"> <b>INVOICE</b> </p><br>\n" + 
				"				<img style=\"margin-left: 150px;\" src='" + url + "' alt=\"Geekspace\" width=\"200\" height=\"133\"/>" +
				"			</div>\n" + 
				"		</div>\n" + 
				"	</div> <br>\n";
	}
	
	private String getClientInfo(Invoice invoice) {
//		Address address = invoice.getClient().getAddressDetails();
		String addressDetails = getAddressDetails(invoice.getClient().getAddressDetails());
		String shipTo = getShipTO(invoice.getClient());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/YYYY");
		String date = dateFormat.format(invoice.getDate());
		return "	<div>\n" + 
				"		<div class=\"row\">\n" + 
				"			<div class=\"spec-column-low\">\n" + 
//				"		      	<p style=\"text-align: left; font-family: serif; font-size: 14px;\"> <b> Geekspace Business Center </b> </p>\n" + 
				"				<div class=\"row\"> <span><p style=\"padding-left:18px\"><b>BILL TO</b></p></span></div>\n" + addressDetails +
				"		    </div>\n" + shipTo +
				"		<div class=\"spec-column-low\">\n" + 
				"				<span style=\"text-align: right;\">" +
				"		      	<b >INVOICE NO:</b></span> &nbsp;" +  invoice.getInvoiceNo() + "<br>\n" +
				"		       	<span style=\"text-align: right;\">\n" + 
				"				<b>DATE:</b></span> &nbsp;" + date + " <br>\n" +
				"		       	<span style=\"text-align: right;\">\n" + 
				"				<b>GSTIN:</b></span> &nbsp;" + invoice.getGstNo() + " <br>\n" +
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
				"			      <th style=\"width: 60%; padding-left: 10px;\">Description</th>" + 
				"			      <th>Quantity</th>" + 
				"			      <th>Gst(%)</th>" + 
				"			      <th>Price</th>" + 
				"			    </tr>" + 
				" 			<thead><tbody>" + tableRows + "</tbody>" + 
				tableFooter +
				"</table> </div>";
		return invoiceItems;
	}

	private String getTableRows(List<InvoiceItem> list) {
		String getRows = "";
		for (InvoiceItem invoiceItem : list) {
			getRows += "<tr><td style=\"padding-left: 10px;\">" + invoiceItem.getDetails() + "</td>\n" + 
					"<td>" + invoiceItem.getQuantity() + "</td>\n" + 
					"<td>" + invoiceItem.getGst() + "</td>\n" + 
					"<td>" + invoiceItem.getPrice() + "</td></tr>\n";
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
				"			      <td>SUBTOTAL</td>\n" + 
				"			      <td>"+ subTotal +"</td>\n" + 
				"			    </tr>\n" + 
				"				<tr>\n" + 
				"			      <td></td>\n" + 
				"			      <td></td>\n" + 
				"			      <td>GST</td>\n" + 
				"			      <td>"+ gstAmount +"</td>\n" + 
				"			    </tr>\n" + 
				"				<tr>\n" + 
				"			      <td></td>\n" + 
				"			      <td></td>\n" + 
				"			      <td>TOTAL</td>\n" + 
				"			      <td>"+ total_amount +"</td>\n" + 
				"			    </tr>\n" + 
				"				<tr>\n" + 
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
			subTotal += invoiceItem.getPrice();
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


//	private String getFooter() {
//		String footer = "<footer>\n" +
//				" <br><p> <span style=\"text-align: right;\">*** Make all checks payable ***</span></p>" + 
//				"   <p><span style=\"text-align: left;\">Author: 124APPS Limited</p></div>\n" + 
//				"</footer>";
//		return footer;
//	}
	
//	private String getFromTo(Invoice invoice) {
//		Client client = invoice.getClient();
//		return "<div class=\"row\">" +  
//				"			<div class=\"column\">" + 
//				"				<p style=\"font-size: 12\"> <b>From</b> </p>" + 
//				"				Geekspace Business Center <br>" + 
//				"				Manjeera Trinity Corporate <br>" + 
//				"				Hyderabad <br>" + 
//				"				Telangana, 500018" +
//				"			</div>" + 
//				"			<div class=\"column\">" + 
//				"				<p style=\"font-size: 12\"> <b>To</b> </p>" + 
//				client.getClientName() +" <br>" + 
//				invoice.getGstNo() + "<br>" + 
//				client.getEmail() + "<br>" + 
//				client.getWebsite() +
//				"</div></div>";
//	}
	
//	private String getHtmlText () {
//		String styles = getStyles();
//		return "<!DOCTYPE html>\n" + 
//		"<html>\n" + 
//		"<head>\n" + 
//		"<meta charset=\"UTF-8\">\n" + 
//		"<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css\">\n" + styles +
//		"</head>\n" + 
//		"<body>\n" + 
//		"<div class=\"container\">\n" + 
//		"	<br>\n" + 
//		"	<div>\n" + 
//		"		<div class=\"row\">\n" + 
//		"			<div class=\"column\">\n" + 
//		"		      <p style=\"text-align: left; font-family: serif; font-size: 20px;\"> <b> Geekspace Business Center </b> </p>\n" + 
//		"		      	Plot No: 1204 <br>\n" + 
//		"		       	Manjeera Trinity Corporate <br>\n" + 
//		"		       	Kukatpally  <br>\n" + 
//		"				Hyderabad <br>\n" + 
//		"				Telangana, 500018\n" + 
//		"		    </div>\n" + 
//		"			<div class=\"column\">\n" + 
//		"				<p style=\"text-align: right; font-family: serif; font-size: 20px;\"> <b>INVOICE</b> </p><br>\n" + 
//		"				<div class=\"row\" >\n" + 
//		"					<div class=\"spec-column-low\"></div>\n" + 
//		"					<div class=\"spec-column\">\n" + 
//		"						<div class=\"row\" style=\"text-align:center; background-color: aqua;\">\n" + 
//		"							<div class=\"column\">\n" + 
//		"								<span>Invoive Date</span>\n" + 
//		"							</div>\n" + 
//		"							<div class=\"column\">\n" + 
//		"								<span>Invoive #</span>\n" + 
//		"							</div>\n" + 
//		"						</div>\n" + 
//		"					</div>\n" + 
//		"				</div>\n" + 
//		"				<div class=\"row\" >\n" + 
//		"					<div class=\"column\"></div>\n" + 
//		"					<div class=\"column\">\n" + 
//		"						<div class=\"row\" style=\"text-align:center;\">\n" + 
//		"							<div class=\"column\">\n" + 
//		"								<span>Invoice Date Dynamicly</span>\n" + 
//		"							</div>\n" + 
//		"							<div class=\"column\">\n" + 
//		"								<span>Invoice Number Dynamicly</span>\n" + 
//		"							</div>\n" + 
//		"						</div>\n" + 
//		"					</div>\n" + 
//		"				</div>\n" + 
//		"			</div>\n" + 
//		"		</div>\n" + 
//		"	</div> <br>\n" + 
//		"	\n" + 
//		"	<div>\n" + 
//		"		<div class=\"row\">\n" + 
//		"			<div class=\"col-sm-6\">\n" + 
//		"				<div class=\"row\" style=\"background-color: aqua;\"> <span>BILL TO</span></div>\n" + 
//		"		      <!-- <p style=\"text-align: left; font-family: serif; font-size: 20px;\"> <b> Geekspace Business Center </b> </p> -->\n" + 
//		"		      	Plot No: 1204 <br>\n" + 
//		"		       	Manjeera Trinity Corporate <br>\n" + 
//		"		       	Kukatpally  <br>\n" + 
//		"				Hyderabad <br>\n" + 
//		"				Telangana, 500018\n" + 
//		"		    </div>\n" + 
//		"			<div class=\"col-sm-6\">\n" + 
//		"				<div class=\"row\"> \n" + 
//		"					<div class=\"row\" >\n" + 
//		"						<div class=\"col-sm-6\"></div>\n" + 
//		"						<div class=\"col-sm-6\">\n" + 
//		"							<div class=\"row\" style=\"text-align:center; background-color: aqua;\">\n" + 
//		"								<div class=\"col-sm-6\">\n" + 
//		"									<span>Client ID</span>\n" + 
//		"								</div>\n" + 
//		"								<div class=\"col-sm-6\">\n" + 
//		"									<span>TERMS</span>\n" + 
//		"								</div>\n" + 
//		"							</div>\n" + 
//		"						</div>\n" + 
//		"					</div>\n" + 
//		"				</div>\n" + 
//		"				<div class=\"row\" >\n" + 
//		"					<div class=\"col-sm-6\"></div>\n" + 
//		"					<div class=\"col-sm-6\">\n" + 
//		"						<div class=\"row\" style=\"text-align:center;\">\n" + 
//		"							<div class=\"col-sm-6\">\n" + 
//		"								<span>Invoive Date</span>\n" + 
//		"							</div>\n" + 
//		"							<div class=\"col-sm-6\">\n" + 
//		"								<span>Invoive #</span>\n" + 
//		"							</div>\n" + 
//		"						</div>\n" + 
//		"					</div>\n" + 
//		"				</div>\n" + 
//		"				\n" + 
//		"			</div>\n" + 
//		"		</div>\n" + 
//		"	</div> <br>\n" + 
//		"	\n" + 
//		"		<table style=\"width: 100%;\">\n" + 
//		"			<thead style=\"background-color: highlight; color: green; padding-top: -20; text-align: left;\"> \n" + 
//		"			\n" + 
//		"				<tr>\n" + 
//		"			      <th style=\"width: 60%;\">Description</th>\n" + 
//		"			      <th>Quantity</th>\n" + 
//		"			      <th>Gst(%)</th>\n" + 
//		"			      <th>Price</th>\n" + 
//		"			    </tr>\n" + 
//		"			</thead>\n" + 
//		"			<tbody>\n" + 
//		"				<tr >\n" + 
//		"			      <td>ksn</td>\n" + 
//		"			      <td>1</td>\n" + 
//		"			      <td>18</td>\n" + 
//		"			      <td>112380</td>\n" + 
//		"			    </tr>\n" + 
//		"			    <tr>\n" + 
//		"			      <td>ksd</td>\n" + 
//		"			      <td>1</td>\n" + 
//		"			      <td>18</td>\n" + 
//		"			      <td>112380</td>\n" + 
//		"			    </tr>\n" + 
//		"			    <tr>\n" + 
//		"			      <td>ks</td>\n" + 
//		"			      <td>1</td>\n" + 
//		"			      <td>18</td>\n" + 
//		"			      <td>112380</td>\n" + 
//		"			    </tr>\n" + 
//		"			    <tr>\n" + 
//		"			      <td>k</td>\n" + 
//		"			      <td>1</td>\n" + 
//		"			      <td>18</td>\n" + 
//		"			      <td>112380</td>\n" + 
//		"			    </tr>\n" + 
//		"			</tbody>\n" + 
//		"			<tfoot>\n" + 
//		"				<tr>\n" + 
//		"			      <td></td>\n" + 
//		"			      <td></td>\n" + 
//		"			      <td>TotalTotalTotal</td>\n" + 
//		"			      <td>18</td>\n" + 
//		"			    </tr>\n" + 
//		"			</tfoot>	\n" + 
//		"		</table>\n" + 
//		"</div>\n" + 
//		"</body>\n" + 
//		"</html>";
//	}

}
