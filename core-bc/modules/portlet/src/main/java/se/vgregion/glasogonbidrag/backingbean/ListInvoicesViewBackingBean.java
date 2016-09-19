package se.vgregion.glasogonbidrag.backingbean;

import com.liferay.portal.theme.ThemeDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.glasogonbidrag.util.LiferayUtil;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.service.glasogonbidrag.domain.api.data.InvoiceRepository;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Component(value = "listInvoicesViewBackingBean")
@Scope(value = "view")
public class ListInvoicesViewBackingBean {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(ListInvoicesViewBackingBean.class);

    @Autowired
    private FacesUtil facesUtil;

    @Autowired
    private LiferayUtil liferayUtil;

    @Autowired
    private InvoiceRepository invoiceRepository;

    private List<Invoice> invoices;

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public String getUserNameById(long userId) {
        return liferayUtil.getUserNameById(userId);
    }

    @PostConstruct
    protected void init() {
        //invoices = invoiceRepository.findAllWithParts();
        fetchInvoices();
    }

    private void fetchInvoices() {
        invoices = invoiceRepository.findAllWithParts();
    }

}
