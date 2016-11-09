package se.vgregion.glasogonbidrag.backingbean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import se.vgregion.glasogonbidrag.mock.StatisticsMockUtil;
import se.vgregion.glasogonbidrag.util.DateUtil;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.glasogonbidrag.util.LiferayUtil;
import se.vgregion.glasogonbidrag.viewobject.StatisticsVO;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Erik Andersson - Monator Technologies AB
 */
@Component(value = "statisticsExportViewBackingBean")
@Scope(value = "view", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class StatisticsExportViewBackingBean {


    private static final Logger LOGGER =
            LoggerFactory.getLogger(StatisticsExportViewBackingBean.class);

    @Autowired
    private FacesUtil facesUtil;


    @PostConstruct
    protected void init() {
        LOGGER.info("init");

    }


}
