package se.vgregion.portal.glasogonbidrag.domain.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 *
 */
@Entity
@Table(name = "vgr_glasogonbidrag_grant")
public class Grant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "delivery_date")
    @Temporal(TemporalType.DATE)
    private Date deliveryDate;

    @Column(name = "prescription_date")
    @Temporal(TemporalType.DATE)
    private Date prescriptionDate;

    private int vat;

    private int amount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "receiver")
    private Beneficiary beneficiary;

    public Grant() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Date getPrescriptionDate() {
        return prescriptionDate;
    }

    public void setPrescriptionDate(Date prescriptionDate) {
        this.prescriptionDate = prescriptionDate;
    }

    public int getVat() {
        return vat;
    }

    public void setVat(int vat) {
        this.vat = vat;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Beneficiary getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(Beneficiary beneficiary) {
        this.beneficiary = beneficiary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Grant grant = (Grant) o;

        if (vat != grant.vat) return false;
        if (amount != grant.amount) return false;
        if (!id.equals(grant.id)) return false;
        if (deliveryDate != null ? !deliveryDate.equals(grant.deliveryDate) : grant.deliveryDate != null)
            return false;
        if (prescriptionDate != null ? !prescriptionDate.equals(grant.prescriptionDate) : grant.prescriptionDate != null)
            return false;
        return beneficiary != null ? beneficiary.equals(grant.beneficiary) : grant.beneficiary == null;

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (deliveryDate != null ? deliveryDate.hashCode() : 0);
        result = 31 * result + (prescriptionDate != null ? prescriptionDate.hashCode() : 0);
        result = 31 * result + vat;
        result = 31 * result + amount;
        result = 31 * result + (beneficiary != null ? beneficiary.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Grant{" +
                "id=" + id +
                ", deliveryDate=" + deliveryDate +
                ", prescriptionDate=" + prescriptionDate +
                ", vat=" + vat +
                ", amount=" + amount +
                '}';
    }
}
