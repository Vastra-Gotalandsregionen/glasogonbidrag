package se.vgregion.glasogonbidrag.flow.state;

import se.vgregion.glasogonbidrag.flow.CreateInvoiceAddGrantPidFlow;

public class AddGrantDeliveryDateState extends CreateInvoiceAddGrantPidFlow {
    @Override
    public boolean getShowPersonalNumberOutput() {
    return true;
}

    @Override
    public boolean getShowDeliveryDateInput() {
        return true;
    }

    @Override
    public CreateInvoiceAddGrantPidFlow nextState() {
        return new AddGrantGrantTypeState();
    }
}
