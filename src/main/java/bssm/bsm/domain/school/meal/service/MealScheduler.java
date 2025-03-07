package bssm.bsm.domain.school.meal.service;

import bssm.bsm.domain.school.meal.domain.Meal;
import bssm.bsm.domain.school.meal.domain.MealType;
import bssm.bsm.domain.school.meal.facade.MealFacade;
import bssm.bsm.domain.school.meal.domain.MealRepository;
import bssm.bsm.domain.webpush.domain.WebPush;
import bssm.bsm.domain.webpush.domain.repository.WebPushRepository;
import bssm.bsm.domain.webpush.presentation.dto.request.WebPushMsgDto;
import bssm.bsm.domain.webpush.service.SendWebPushService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.YearMonth;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MealScheduler {

    private final MealRepository mealRepository;
    private final WebPushRepository webPushRepository;
    private final SendWebPushService webPushUtil;
    private final MealFacade mealFacade;
    private final MealProvider mealProvider;

    @Value("${meal.url}")
    private String MEAL_ACCESS_URL;

    @Scheduled(cron = "0 0 0 25 * ?")
    private void getMonthMeal() throws IOException {
        YearMonth nextMonth = YearMonth.now().plusMonths(1);
        List<Meal> mealList = mealProvider.getRawMonthMealList(nextMonth).stream()
                .map(meal -> meal.toEntity(mealFacade.filterMealStr(meal.getDDISH_NM())))
                .toList();

        mealRepository.saveAll(mealList);
    }

    @Scheduled(cron = "0 30 6 * * 1-5")
    private void morningNotification() throws JsonProcessingException {
        WebPushMsgDto dto = WebPushMsgDto.create(
                "오늘의 아침",
                mealFacade.getTodayMealStr(MealType.MORNING),
                MEAL_ACCESS_URL);
        sendMealNotification(dto);
    }

    @Scheduled(cron = "0 30 11 * * 1-5")
    private void lunchNotification() throws JsonProcessingException {
        WebPushMsgDto dto = WebPushMsgDto.create(
                "오늘의 점심",
                mealFacade.getTodayMealStr(MealType.LUNCH),
                MEAL_ACCESS_URL);
        sendMealNotification(dto);
    }

    @Scheduled(cron = "0 0 17 * * 1-5")
    private void dinnerNotification() throws JsonProcessingException {
        WebPushMsgDto dto = WebPushMsgDto.create(
                "오늘의 저녁",
                mealFacade.getTodayMealStr(MealType.DINNER),
                MEAL_ACCESS_URL);
        sendMealNotification(dto);
    }

    private void sendMealNotification(WebPushMsgDto dto) throws JsonProcessingException {
        List<WebPush> pushList = webPushRepository.findAll();
        webPushUtil.sendNotificationToAll(pushList, dto);
    }

}
