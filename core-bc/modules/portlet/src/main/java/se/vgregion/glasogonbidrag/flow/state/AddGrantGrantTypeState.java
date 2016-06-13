package se.vgregion.glasogonbidrag.flow.state;

import se.vgregion.glasogonbidrag.flow.AddGrantFlowState;
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
        // This state don't have a natural next state.
        return null;
    }

    @Override
    public CreateInvoiceAddGrantPidFlow nextState(AddGrantAction action) {
        if (action == AddGrantAction.AGE_0_TO_15 || action == AddGrantAction.AGE_0_TO_19) {
//            return new AddGrantPrescriptionDateState();
            return AddGrantFlowState.ENTER_PRESCRIPTION_DATE.getState();
        } else if(action == AddGrantAction.OTHER) {
//            return new AddGrantOtherTypeState();
            return AddGrantFlowState.ENTER_GRANT_STATE_OTHER_TYPE.getState();
        } else {
            return null;
        }
    }
}
