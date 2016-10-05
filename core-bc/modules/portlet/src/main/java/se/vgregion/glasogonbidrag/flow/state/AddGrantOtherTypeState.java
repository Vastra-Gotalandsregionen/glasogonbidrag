package se.vgregion.glasogonbidrag.flow.state;

import se.vgregion.glasogonbidrag.flow.AddGrantFlowState;
import se.vgregion.glasogonbidrag.flow.CreateInvoiceAddGrantPidFlow;

public class AddGrantOtherTypeState extends CreateInvoiceAddGrantPidFlow {

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
    public boolean getShowGrantTypeOtherSection() {
        return true;
    }

    @Override
    public boolean getShowPrescriptionTypeInput() {
        return true;
    }

    @Override
    public CreateInvoiceAddGrantPidFlow nextState() {
        //return new AddGrantAmountAfterOtherState();
        return AddGrantFlowState.ENTER_GRANT_STATE_OTHER_DATE.getState();
    }
}
