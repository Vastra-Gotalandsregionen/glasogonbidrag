package se.vgregion.glasogonbidrag.flow.state;

import se.vgregion.glasogonbidrag.flow.CreateInvoiceAddGrantPidFlow;

public class AddGrantAllDataAfterOtherState extends CreateInvoiceAddGrantPidFlow {

    @Override
    public boolean getShowIdentificationOutput() {
        return true;
    }

    @Override
    public boolean getShowDeliveryDateOutput() {
        return true;
    }

    @Override
    public boolean getShowGrantTypeOutput() {
        return true;
    }

    @Override
    public boolean getShowGrantTypeOtherSection() {
        return true;
    }

    @Override
    public boolean getShowPrescriptionTypeOutput() {
        return true;
    }

    @Override
    public boolean getShowPrescriptionDateOutput() {
        return true;
    }

    @Override
    public boolean getShowAmountOutput() {
        return true;
    }

    @Override
    public CreateInvoiceAddGrantPidFlow nextState() {
        // Final state, next state will be null state.
        return null;
    }
}
