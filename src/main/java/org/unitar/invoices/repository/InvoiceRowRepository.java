package org.unitar.invoices.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.unitar.invoices.model.Invoice;
import org.unitar.invoices.model.InvoiceRow;

import java.util.List;

@Repository
public interface InvoiceRowRepository extends CrudRepository<InvoiceRow, Integer> {
    void deleteInvoiceRowByRowId(long id);
    InvoiceRow findInvoiceRowByRowId(long id);
    List<InvoiceRow> findByInvoice(Invoice invoice);
}
