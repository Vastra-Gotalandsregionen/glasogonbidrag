package se.vgregion.glasogonbidrag.model;

public class ImportGrant {
    private String prescriptionDate;
    private String deliveryDate;
    private String amount;
    private String invoiceNumber;
    private String verificationNumber;

    public ImportGrant(String prescriptionDate,
                       String deliveryDate,
                       String amount,
                       String invoiceNumber,
                       String verificationNumber) {
        this.prescriptionDate = prescriptionDate;
        this.deliveryDate = deliveryDate;
        this.amount = amount;
        this.invoiceNumber = invoiceNumber;
        this.verificationNumber = verificationNumber;
    }

    public String getPrescriptionDate() {
        return prescriptionDate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public String getAmount() {
        return amount;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public String getVerificationNumber() {
        return verificationNumber;
    }

    @Override
    public String toString() {
        return verificationNumber + ":" + amount;
    }
}
