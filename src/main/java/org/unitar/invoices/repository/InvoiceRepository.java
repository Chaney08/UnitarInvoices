package org.unitar.invoices.repository;

import org.unitar.invoices.model.Invoice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends CrudRepository<Invoice, Integer> {
    Invoice findByInvoiceName(String name);
    void deleteInvoiceByInvoiceId(long id);
    Invoice findInvoiceByInvoiceId(long id);
}
