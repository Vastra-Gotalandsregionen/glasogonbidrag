package se.vgregion.glasogonbidrag.jsf.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.vgregion.service.glasogonbidrag.domain.api.service.StatisticExportService;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Martin Lind
 */
@Component
public class StatisticReportResourceHandler extends ResourceHandlerWrapper {

    public static final String LIBRARY_NAME = "se.vgregion.glasogonbidrag";

    private ResourceHandler resourceHandler;

    public StatisticReportResourceHandler(ResourceHandler resourceHandler) {
        this.resourceHandler = resourceHandler;
    }

    @Override
    public Resource createResource(String resourceName, String libraryName) {
        if (LIBRARY_NAME.equals(libraryName)) {
            return new StatisticReportResource(resourceName);
        }
        return super.createResource(resourceName, libraryName);
    }

    @Override
    public boolean libraryExists(String libraryName) {
        if (LIBRARY_NAME.equals(libraryName)) {
            return true;
        } else {
            return super.libraryExists(libraryName);
        }
    }

    @Override
    public ResourceHandler getWrapped() {
        return resourceHandler;
    }
}
