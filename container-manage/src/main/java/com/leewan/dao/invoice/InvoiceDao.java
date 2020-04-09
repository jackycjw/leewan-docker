package com.leewan.dao.invoice;

import java.util.List;
import java.util.Map;


public interface InvoiceDao {

	public List<Map<String, Object>> queryInvoiceList(Map map);
	
	
	public void saveInvoice(Map map);
	
	public void deleteInvoice(int invoiceId);
}
