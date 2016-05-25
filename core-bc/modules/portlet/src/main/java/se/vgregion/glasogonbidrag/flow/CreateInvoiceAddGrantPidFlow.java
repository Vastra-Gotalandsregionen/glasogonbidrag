package se.vgregion.glasogonbidrag.flow;

public class CreateInvoiceAddGrantPidFlow {

    private boolean showPersonalNumberInput;
    private boolean showPersonalNumberOutput;
    private boolean showDeliveryDateInput;
    private boolean showDeliveryDateOutput;
    private boolean showGrantTypeInput;
    private boolean showGrantTypeOutput;
    private boolean showGrantTypeAgeSection;
    private boolean showGrantTypeOtherSection;
    private boolean showPrescriptionDateInput;
    private boolean showPrescriptionDateOutput;
    private boolean showAmountInput;
    private boolean showAmountOutput;

    public CreateInvoiceAddGrantPidFlow() {
        showPersonalNumberInput = true;
        showPersonalNumberOutput = false;
        showDeliveryDateInput = false;
        showDeliveryDateOutput = false;
        showGrantTypeInput = false;
        showGrantTypeOutput = false;
        showGrantTypeAgeSection = false;
        showGrantTypeOtherSection = false;
        showPrescriptionDateInput = false;
        showPrescriptionDateOutput = false;
        showAmountInput = false;
        showAmountOutput = false;
    }

    public boolean getShowPersonalNumberInput() {
        return showPersonalNumberInput;
    }

    public void setShowPersonalNumberInput(boolean showPersonalNumberInput) {
        this.showPersonalNumberInput = showPersonalNumberInput;
    }

    public boolean getShowPersonalNumberOutput() {
        return showPersonalNumberOutput;
    }

    public void setShowPersonalNumberOutput(boolean showPersonalNumberOutput) {
        this.showPersonalNumberOutput = showPersonalNumberOutput;
    }

    public boolean getShowDeliveryDateInput() {
        return showDeliveryDateInput;
    }

    public void setShowDeliveryDateInput(boolean showDeliveryDateInput) {
        this.showDeliveryDateInput = showDeliveryDateInput;
    }

    public boolean getShowDeliveryDateOutput() {
        return showDeliveryDateOutput;
    }

    public void setShowDeliveryDateOutput(boolean showDeliveryDateOutput) {
        this.showDeliveryDateOutput = showDeliveryDateOutput;
    }

    public boolean getShowGrantTypeInput() {
        return showGrantTypeInput;
    }

    public void setShowGrantTypeInput(boolean showGrantTypeInput) {
        this.showGrantTypeInput = showGrantTypeInput;
    }

    public boolean getShowGrantTypeOutput() {
        return showGrantTypeOutput;
    }

    public void setShowGrantTypeOutput(boolean showGrantTypeOutput) {
        this.showGrantTypeOutput = showGrantTypeOutput;
    }

    public boolean getShowGrantTypeAgeSection() {
        return showGrantTypeAgeSection;
    }

    public void setShowGrantTypeAgeSection(boolean showGrantTypeAgeSection) {
        this.showGrantTypeAgeSection = showGrantTypeAgeSection;
    }

    public boolean getShowGrantTypeOtherSection() {
        return showGrantTypeOtherSection;
    }

    public void setShowGrantTypeOtherSection(boolean showGrantTypeOtherSection) {
        this.showGrantTypeOtherSection = showGrantTypeOtherSection;
    }

    public boolean getShowPrescriptionDateInput() {
        return showPrescriptionDateInput;
    }

    public void setShowPrescriptionDateInput(boolean showPrescriptionDateInput) {
        this.showPrescriptionDateInput = showPrescriptionDateInput;
    }

    public boolean getShowPrescriptionDateOutput() {
        return showPrescriptionDateOutput;
    }

    public void setShowPrescriptionDateOutput(boolean showPrescriptionDateOutput) {
        this.showPrescriptionDateOutput = showPrescriptionDateOutput;
    }

    public boolean getShowAmountInput() {
        return showAmountInput;
    }

    public void setShowAmountInput(boolean showAmountInput) {
        this.showAmountInput = showAmountInput;
    }

    public boolean getShowAmountOutput() {
        return showAmountOutput;
    }

    public void setShowAmountOutput(boolean showAmountOutput) {
        this.showAmountOutput = showAmountOutput;
    }
}
