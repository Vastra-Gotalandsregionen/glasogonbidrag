package se.vgregion.glasogonbidrag.flow.state;

import se.vgregion.glasogonbidrag.flow.AddGrantFlowState;
import se.vgregion.glasogonbidrag.flow.CreateInvoiceAddGrantPidFlow;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class AddGrantPersonalNumberState extends CreateInvoiceAddGrantPidFlow {
    @Override
    public boolean getShowPersonalNumberInput() {
        return true;
    }

    @Override
    public CreateInvoiceAddGrantPidFlow nextState() {
        //return new AddGrantDeliveryDateState();
        return AddGrantFlowState.ENTER_DELIVERY_DATE.getState();
    }
}
