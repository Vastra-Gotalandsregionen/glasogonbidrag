package se.vgregion.glasogonbidrag.flow.state;

import se.vgregion.glasogonbidrag.flow.CreateInvoiceAddGrantPidFlow;
import se.vgregion.glasogonbidrag.flow.action.AddGrantAction;

public class AddGrantGrantTypeState extends CreateInvoiceAddGrantPidFlow {

    @Override
    public boolean getShowPersonalNumberOutput() {
        return true;
    }

    @Override
    public boolean getShowDeliveryDateOutput() {
        return true;
    }

    @Override
    public boolean getShowGrantTypeInput() {
        return true;
    }

    @Override
    public CreateInvoiceAddGrantPidFlow nextState() {
        return null;
    }

    @Override
    public CreateInvoiceAddGrantPidFlow nextState(AddGrantAction action) {
        if (action == AddGrantAction.AGE_0_TO_15 || action == AddGrantAction.AGE_0_TO_19) {
            return new AddGrantPrescriptionDateState();
        } else {
            return null;
        }
    }
}