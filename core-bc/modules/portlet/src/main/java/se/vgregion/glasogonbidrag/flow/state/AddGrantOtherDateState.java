package se.vgregion.glasogonbidrag.flow.state;

import se.vgregion.glasogonbidrag.flow.AddGrantFlowState;
import se.vgregion.glasogonbidrag.flow.CreateInvoiceAddGrantPidFlow;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class AddGrantOtherDateState extends CreateInvoiceAddGrantPidFlow {

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
    public boolean getShowGrantTypeOtherSection() {
        return true;
    }

    @Override
    public boolean getShowPrescriptionTypeOutput() {
        return true;
    }

    @Override
    public boolean getShowPrescriptionDateInput() {
        return true;
    }

    @Override
    public CreateInvoiceAddGrantPidFlow nextState() {
        return AddGrantFlowState.ENTER_AMOUNT_AFTER_OTHER.getState();
    }
}
