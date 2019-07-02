package se.vgregion.glasogonbidrag.flow.state;

import se.vgregion.glasogonbidrag.flow.CreateInvoiceAddGrantPidFlow;

public class AddGrantAllDataAfterAgeState extends CreateInvoiceAddGrantPidFlow {

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
    public boolean getShowGrantTypeAgeSection() {
        return true;
    }

    @Override
    public boolean getShowContactLensesOutput() {
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
