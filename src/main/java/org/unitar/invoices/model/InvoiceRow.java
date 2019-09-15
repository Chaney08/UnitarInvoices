package org.unitar.invoices.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoiceRow ")
public class InvoiceRow {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long rowId;

    private String description;
    @NotBlank(message = "Quantity is mandatory")
    private String quantity;
    private String price;
    private String total;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    @ManyToOne
    @JoinColumn(name="invoiceId")
    private Invoice invoice;

    public InvoiceRow(){
        this.description = "test";
        this.quantity = "2";
        this.price = "2";
    }

    public Long getRowId() {
        return rowId;
    }

    public void setRowId(Long rowId) {
        this.rowId = rowId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(LocalDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public int getTotal() {
        int quantity = Integer.parseInt(this.quantity);
        int price = Integer.parseInt(this.price);
        int total = quantity * price;
        return total;
    }
}
