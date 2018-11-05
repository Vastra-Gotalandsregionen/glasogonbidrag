package se.vgregion.glasogonbidrag.flow.state;

import se.vgregion.glasogonbidrag.flow.AddGrantFlowState;
import se.vgregion.glasogonbidrag.flow.CreateInvoiceAddGrantPidFlow;

public class AddGrantContactLensesState extends CreateInvoiceAddGrantPidFlow {

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
    public boolean getShowContactLensesInput() {
        return true;
    }

    @Override
    public CreateInvoiceAddGrantPidFlow nextState() {
        return AddGrantFlowState.ENTER_PRESCRIPTION_DATE.getState();
    }
}
