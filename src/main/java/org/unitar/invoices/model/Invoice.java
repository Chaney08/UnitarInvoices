package org.unitar.invoices.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Entity
@Table(name = "invoice")
public class Invoice {


    @Id
    @GeneratedValue(strategy= GenerationType    .IDENTITY)
    private Long invoiceId;

    @NotBlank(message = "Invoice Name is mandatory")
    private String invoiceName;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    @ManyToOne
    @JoinColumn(name="userId")
    private User user;

    @OneToMany(mappedBy="invoice")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<InvoiceRow> invoiceRows;

    private int totalAmount;

    public Invoice(){

    }


    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceName() {
        return invoiceName;
    }

    public void setInvoiceName(String invoiceName) {
        this.invoiceName = invoiceName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<InvoiceRow> getInvoiceRows() {
        return invoiceRows;
    }

    public void setInvoiceRows(List<InvoiceRow> invoiceRows) {
        this.invoiceRows = invoiceRows;
    }

    public void addInvoiceRow(InvoiceRow row) {
        if(invoiceRows == null){
            invoiceRows = new ArrayList<>();
        }
        this.invoiceRows.add(row);
    }

    public void deleteRow(int idForDeletion){
        //Need to use iterator as we will be removing if found - Which cannot be done in enhanced for loop
        for (Iterator<InvoiceRow> it = invoiceRows.iterator(); it.hasNext();) {
            InvoiceRow row = it.next();
            if (idForDeletion == row.getRowId()){
                it.remove();
            }
        }
    }
    public void deleteRowByIndex(int index){
        this.invoiceRows.remove(index);
    }

    public int getTotalAmount() {
        if(invoiceRows == null){
            return 0;
        }
        totalAmount = invoiceRows.stream().mapToInt(x-> x.getTotal()).sum();
        return totalAmount;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceId=" + invoiceId +
                ", invoiceName='" + invoiceName + '\'' +
                ", createDateTime=" + createDateTime +
                '}';
    }
}
