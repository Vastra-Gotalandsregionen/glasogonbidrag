package se.vgregion.glasogonbidrag.flow;

import se.vgregion.glasogonbidrag.flow.state.*;

public enum AddGrantFlowState {
    ENTER_PERSONAL_NUMBER(new AddGrantPersonalNumberState()),
    ENTER_DELIVERY_DATE(new AddGrantDeliveryDateState()),
    SELECT_GRANT_TYPE(new AddGrantGrantTypeState()),
    ENTER_PRESCRIPTION_DATE(new AddGrantPrescriptionDateState()),
    ENTER_GRANT_STATE_OTHER_TYPE(new AddGrantOtherTypeState()),
    ENTER_GRANT_STATE_OTHER_DATE(new AddGrantOtherDateState()),
    ENTER_AMOUNT_AFTER_AGE(new AddGrantAmountAfterAgeState()),
    ENTER_AMOUNT_AFTER_OTHER(new AddGrantAmountAfterOtherState());

    private CreateInvoiceAddGrantPidFlow state;

    private AddGrantFlowState(CreateInvoiceAddGrantPidFlow state) {
        this.state = state;
    }

    public CreateInvoiceAddGrantPidFlow getState() {
        return state;
    }
}
