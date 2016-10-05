package se.vgregion.glasogonbidrag.flow.state;

import se.vgregion.glasogonbidrag.flow.AddGrantFlowState;
import se.vgregion.glasogonbidrag.flow.CreateInvoiceAddGrantPidFlow;

public class AddGrantDeliveryDateState extends CreateInvoiceAddGrantPidFlow {
    @Override
    public boolean getShowIdentificationOutput() {
    return true;
}

    @Override
    public boolean getShowDeliveryDateInput() {
        return true;
    }

    @Override
    public CreateInvoiceAddGrantPidFlow nextState() {
        //return new AddGrantGrantTypeState();
        return AddGrantFlowState.SELECT_GRANT_TYPE.getState();
    }
}
