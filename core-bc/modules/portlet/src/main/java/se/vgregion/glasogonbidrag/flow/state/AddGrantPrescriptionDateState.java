package se.vgregion.glasogonbidrag.flow.state;

import se.vgregion.glasogonbidrag.flow.AddGrantFlowState;
import se.vgregion.glasogonbidrag.flow.CreateInvoiceAddGrantPidFlow;

public class AddGrantPrescriptionDateState extends CreateInvoiceAddGrantPidFlow {

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
    public boolean getShowContactLensesOutput() {
        return true;
    }

    @Override
    public boolean getShowPrescriptionDateInput() {
        return true;
    }

    @Override
    public CreateInvoiceAddGrantPidFlow nextState() {
        //return new AddGrantAmountAfterAgeState();
        return AddGrantFlowState.ENTER_AMOUNT_AFTER_AGE.getState();
    }
}
