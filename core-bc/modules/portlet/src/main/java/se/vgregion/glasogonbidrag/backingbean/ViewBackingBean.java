package se.vgregion.glasogonbidrag.backingbean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Supplier;
import se.vgregion.service.glasogonbidrag.data.SupplierRepository;

import javax.annotation.PostConstruct;
import java.util.List;

@Component(value = "viewBean")
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ViewBackingBean {

    @Autowired
    private SupplierRepository repository;

    private List<Supplier> suppliers;

    public List<Supplier> getSuppliers() {
        return suppliers;
    }

    @PostConstruct
    protected void init() {
        System.out.println("ViewBackingBean - init");

        suppliers = repository.findAll();
    }
}
