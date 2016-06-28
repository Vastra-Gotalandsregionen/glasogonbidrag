package se.vgregion.glasogonbidrag.library;

import se.vgregion.glasogonbidrag.model.ImportError;
import se.vgregion.glasogonbidrag.model.ValidationError;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Grant;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Identification;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Migration;

import java.util.List;

/**
 * Created by martlin on 2016/06/27.
 */
public interface StorableRepository {
    List<ImportError> getImportErrors();
    List<ValidationError> getValidationErrors();

    Migration getMigration();

    List<Identification> getIdentifications();
    List<Beneficiary> getBeneficiaries();
    List<Invoice> getInvoices();
    List<Grant> getGrants();
}
