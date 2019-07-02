package se.vgregion.glasogonbidrag.flow;

import se.vgregion.glasogonbidrag.flow.state.*;

public enum AddGrantFlowState {
    ENTER_IDENTIFICATION(new AddGrantIdentificationState()),
    ENTER_DELIVERY_DATE(new AddGrantDeliveryDateState()),
    SELECT_GRANT_TYPE(new AddGrantGrantTypeState()),
    ENTER_PRESCRIPTION_DATE(new AddGrantPrescriptionDateState()),
    ENTER_GRANT_STATE_OTHER_TYPE(new AddGrantOtherTypeState()),
    ENTER_GRANT_STATE_OTHER_DATE(new AddGrantOtherDateState()),
    ENTER_AMOUNT_AFTER_AGE(new AddGrantAmountAfterAgeState()),
    ENTER_AMOUNT_AFTER_OTHER(new AddGrantAmountAfterOtherState()),
    ENTER_ALL_DATA_AFTER_OTHER(new AddGrantAllDataAfterOtherState()),
    ENTER_ALL_DATA_AFTER_AGE(new AddGrantAllDataAfterAgeState());

    private CreateInvoiceAddGrantPidFlow state;

    AddGrantFlowState(CreateInvoiceAddGrantPidFlow state) {
        this.state = state;
    }

    public CreateInvoiceAddGrantPidFlow getState() {
        return state;
    }
}
