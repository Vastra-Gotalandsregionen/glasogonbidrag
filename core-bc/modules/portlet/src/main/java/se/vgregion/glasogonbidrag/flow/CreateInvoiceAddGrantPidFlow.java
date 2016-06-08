package se.vgregion.glasogonbidrag.flow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.vgregion.glasogonbidrag.flow.action.AddGrantAction;

public abstract class CreateInvoiceAddGrantPidFlow {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(CreateInvoiceAddGrantPidFlow.class);

    // Methods for actions

    public abstract CreateInvoiceAddGrantPidFlow nextState();

    public CreateInvoiceAddGrantPidFlow nextState(AddGrantAction action) {
        if (action == AddGrantAction.NATURAL) {
            return nextState();
        } else {
            return null;
        }
    }

    // Default view settings

    public boolean getShowPersonalNumberInput() {
        return false;
    }

    public boolean getShowPersonalNumberOutput() {
        return false;
    }

    public boolean getShowDeliveryDateInput() {
        return false;
    }

    public boolean getShowDeliveryDateOutput() {
        return false;
    }

    public boolean getShowGrantTypeInput() {
        return false;
    }

    public boolean getShowGrantTypeOutput() {
        return false;
    }

    public boolean getShowGrantTypeAgeSection() {
        return false;
    }

    public boolean getShowGrantTypeOtherSection() {
        return false;
    }

    public boolean getShowPrescriptionTypeInput() {
        return false;
    }

    public boolean getShowPrescriptionTypeOutput() {
        return false;
    }

    public boolean getShowPrescriptionDateInput() {
        return false;
    }

    public boolean getShowPrescriptionDateOutput() {
        return false;
    }

    public boolean getShowAmountInput() {
        return false;
    }

    public boolean getShowAmountOutput() {
        return false;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("CreateInvoiceAddGrantPidFlow{");
        sb.append("getShowPersonalNumberInput=").append(getShowPersonalNumberInput());
        sb.append(",getShowPersonalNumberOutput=").append(getShowPersonalNumberOutput());
        sb.append(",getShowDeliveryDateInput=").append(getShowDeliveryDateInput());
        sb.append(",getShowDeliveryDateOutput=").append(getShowDeliveryDateOutput());
        sb.append(",getShowGrantTypeInput=").append(getShowGrantTypeInput());
        sb.append(",getShowGrantTypeOutput=").append(getShowGrantTypeOutput());
        sb.append(",getShowGrantTypeOtherSection=").append(getShowGrantTypeOtherSection());
        sb.append(",getShowPrescriptionTypeInput=").append(getShowPrescriptionTypeInput());
        sb.append(",getShowPrescriptionTypeOutput=").append(getShowPrescriptionTypeOutput());
        sb.append(",getShowPrescriptionDateInput=").append(getShowPrescriptionDateInput());
        sb.append(",getShowPrescriptionDateOutput=").append(getShowPrescriptionDateOutput());
        sb.append(",getShowAmountInput=").append(getShowAmountInput());
        sb.append(",getShowAmountOutput=").append(getShowAmountOutput());
        sb.append('}');
        return sb.toString();
    }
}
