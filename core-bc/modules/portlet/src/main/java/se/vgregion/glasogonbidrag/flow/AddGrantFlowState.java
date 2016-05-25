package se.vgregion.glasogonbidrag.flow;

import se.vgregion.glasogonbidrag.flow.state.AddGrantAmountAfterAgeState;
import se.vgregion.glasogonbidrag.flow.state.AddGrantDeliveryDateState;
import se.vgregion.glasogonbidrag.flow.state.AddGrantGrantTypeState;
import se.vgregion.glasogonbidrag.flow.state.AddGrantPersonalNumberState;
import se.vgregion.glasogonbidrag.flow.state.AddGrantPrescriptionDateState;

public enum AddGrantFlowState {
    ENTER_PERSONAL_NUMBER(new AddGrantPersonalNumberState()),
    ENTER_DELIVERY_DATE(new AddGrantDeliveryDateState()),
    SELECT_GRANT_TYPE(new AddGrantGrantTypeState()),
    ENTER_PRESCRIPTION_DATE(new AddGrantPrescriptionDateState()),
    ENTER_AMOUNT_AFTER_AGE(new AddGrantAmountAfterAgeState());

    private CreateInvoiceAddGrantPidFlow state;

    private AddGrantFlowState(CreateInvoiceAddGrantPidFlow state) {
        this.state = state;
    }

    public CreateInvoiceAddGrantPidFlow getState() {
        return state;
    }
}
