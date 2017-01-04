package se.vgregion.glasogonbidrag.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import se.vgregion.glasogonbidrag.viewobject.StatisticsVO;

import java.util.*;

@Component
@Scope(value = "prototype")
public class StatisticsMockUtil {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(StatisticsMockUtil.class);


    public String test() {

        String test = "test";

        return test;
    }

    public List<StatisticsVO> getStatistics(String statisticsGrouping,
                                            String statisticsFilterGender,
                                            int statisticsFilterBirthYearStart,
                                            int statisticsFilterBirthYearStop) {

        ArrayList<StatisticsVO> statisticsVOs = new ArrayList<StatisticsVO>();

        if(statisticsGrouping.equals("municipality")) {
            for(String municipality : StatisticsMockConstants.municipalities) {
                StatisticsVO statisticsVO = new StatisticsVO();
                statisticsVO.setLabel(municipality);
                statisticsVO.setGrantsSum(5000);
                statisticsVO.setNumberOfGrants(13);
                statisticsVOs.add(statisticsVO);
            }
        } else if(statisticsGrouping.equals("birthday")) {
            Random ageRandom = new Random();

            for(int i = statisticsFilterBirthYearStart; i<= statisticsFilterBirthYearStop; i++) {
                boolean includeYear = getRandomBoolean(0.3f);

                if(includeYear) {
                    StatisticsVO statisticsVO = new StatisticsVO();
                    statisticsVO.setLabel(String.valueOf(i));
                    statisticsVO.setGrantsSum(5000);
                    statisticsVO.setNumberOfGrants(13);
                    statisticsVOs.add(statisticsVO);
                }
            }
        } else if(statisticsGrouping.equals("gender")) {
            StatisticsVO statisticsVO_1 = new StatisticsVO();
            statisticsVO_1.setLabel("Man");
            statisticsVO_1.setGrantsSum(5000);
            statisticsVO_1.setNumberOfGrants(13);
            statisticsVOs.add(statisticsVO_1);

            StatisticsVO statisticsVO_2 = new StatisticsVO();
            statisticsVO_2.setLabel("Kvinna");
            statisticsVO_2.setGrantsSum(5000);
            statisticsVO_2.setNumberOfGrants(13);
            statisticsVOs.add(statisticsVO_2);

            StatisticsVO statisticsVO_3 = new StatisticsVO();
            statisticsVO_3.setLabel("Uppgift saknas");
            statisticsVO_3.setGrantsSum(5000);
            statisticsVO_3.setNumberOfGrants(13);
            statisticsVOs.add(statisticsVO_3);
        } else if(statisticsGrouping.equals("grantType")) {
            StatisticsVO statisticsVO_1 = new StatisticsVO();
            statisticsVO_1.setLabel("Barn");
            statisticsVO_1.setGrantsSum(5000);
            statisticsVO_1.setNumberOfGrants(13);
            statisticsVOs.add(statisticsVO_1);

            StatisticsVO statisticsVO_2 = new StatisticsVO();
            statisticsVO_2.setLabel("Special - Afaki");
            statisticsVO_2.setGrantsSum(5000);
            statisticsVO_2.setNumberOfGrants(13);
            statisticsVOs.add(statisticsVO_2);

            StatisticsVO statisticsVO_3 = new StatisticsVO();
            statisticsVO_3.setLabel("Special - Keratokonus");
            statisticsVO_3.setGrantsSum(5000);
            statisticsVO_3.setNumberOfGrants(13);
            statisticsVOs.add(statisticsVO_3);

            StatisticsVO statisticsVO_4 = new StatisticsVO();
            statisticsVO_4.setLabel("Special - Synsvag");
            statisticsVO_4.setGrantsSum(5000);
            statisticsVO_4.setNumberOfGrants(13);
            statisticsVOs.add(statisticsVO_4);
        }

        return statisticsVOs;
    }

    private boolean getRandomBoolean(float p){
        Random random = new Random();
        return random.nextFloat() < p;
    }

}
