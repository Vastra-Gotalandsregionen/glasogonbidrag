package se.vgregion.glasogonbidrag.library;

import se.vgregion.glasogonbidrag.model.ImportDocument;
import se.vgregion.glasogonbidrag.model.ImportError;
import se.vgregion.glasogonbidrag.model.ImportGrant;
import se.vgregion.glasogonbidrag.model.ValidationError;
import se.vgregion.glasogonbidrag.parser.ParseOutputData;
import se.vgregion.glasogonbidrag.util.TypesUtil;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Beneficiary;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Grant;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Identification;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Migration;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author Martin Lind - Monator Technologies AB
 */
public class StorableRepositoryImpl implements StorableRepository {

    private List<ImportError> importErrors;
    private List<ValidationError> validationErrors;

    private BeneficiaryLibrary beneficiaryLibrary;
    private InvoiceLibrary invoiceLibrary;
    private GrantLibrary grantLibrary;

    public StorableRepositoryImpl() {
        importErrors = new ArrayList<>();
        validationErrors = new ArrayList<>();

        beneficiaryLibrary = new BeneficiaryLibrary();
        invoiceLibrary = new InvoiceLibrary();
        grantLibrary = new GrantLibrary();
    }

    @Override
    public List<ImportError> getImportErrors() {
        return importErrors;
    }

    @Override
    public List<ValidationError> getValidationErrors() {
        return validationErrors;
    }

    @Override
    public Migration getMigration() {
        return invoiceLibrary.getMigration();
    }

    @Override
    public List<Identification> getIdentifications() {
        return beneficiaryLibrary.getIdentifications();
    }

    @Override
    public List<Beneficiary> getBeneficiaries() {
        return beneficiaryLibrary.getBeneficiaries();
    }

    @Override
    public List<Invoice> getInvoices() {
        return invoiceLibrary.getInvoices();
    }

    @Override
    public List<Grant> getGrants() {
        return grantLibrary.getGrants();
    }

    public void catalog(Map<String, ParseOutputData> data) {
        for (String key : data.keySet()) {
            catalog(data.get(key));
        }
    }

    private void catalog(ParseOutputData data) {
        importErrors.addAll(data.getImportErrors());
        validationErrors.addAll(data.getValidationErrors());

        for (ImportDocument document : data.getDocuments()) {
            catalog(document);
        }
    }

    private void catalog(ImportDocument document) {
        String id = document.getId();
        String[] fullName = document.getName().split(" ", 2);
        String firstName = "";
        String lastName = "";
        if (fullName.length > 1) {
            firstName = fullName[0];
            lastName = fullName[1];
        }

        Identification i = TypesUtil.identification(id);
        Beneficiary b = beneficiaryLibrary.find(i);
        if (b == null) {
            b = TypesUtil.beneficiary(i, firstName, lastName);
            beneficiaryLibrary.add(b);
        }

        for (ImportGrant grant : document.getGrants()) {
            catalog(b, grant);
        }
    }

    /**
     *
     * @param beneficiary
     * @param grant
     */
    private void catalog(Beneficiary beneficiary, ImportGrant grant) {
        String verificationNumber = grant.getVerificationNumber();
        String invoiceNumber = grant.getInvoiceNumber();
        Date deliveryDate = parse(grant.getDeliveryDate());
        Date prescriptionDate = parse(grant.getPrescriptionDate());
        String amount = grant.getAmount();

        Invoice i = invoiceLibrary.find(verificationNumber);
        if (i == null) {
            i = TypesUtil.invoice(verificationNumber, invoiceNumber);
            invoiceLibrary.add(i);
        }

        Grant g = TypesUtil.grant(deliveryDate, prescriptionDate, amount);

        beneficiaryLibrary.assign(beneficiary, g);
        grantLibrary.add(g);

        i.addGrant(g);
    }

    private final String[] FORMAT_STRINGS = {"yyyy-MM-dd", "yyMMdd"};

    private Date parse(String dateString) {
        for (String fmt : FORMAT_STRINGS) {
            try {
                return new SimpleDateFormat(fmt).parse(dateString);
            } catch (ParseException ignored) { }
        }

        return null;
    }
}
