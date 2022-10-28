package bssm.bsm.domain.school.meal.service;

import bssm.bsm.domain.school.meal.domain.Meal;
import bssm.bsm.domain.school.meal.domain.MealType;
import bssm.bsm.domain.school.meal.facade.MealFacade;
import bssm.bsm.domain.school.meal.domain.MealRepository;
import bssm.bsm.domain.webpush.domain.WebPush;
import bssm.bsm.domain.webpush.domain.repository.WebPushRepository;
import bssm.bsm.domain.webpush.presentation.dto.request.WebPushSendDto;
import bssm.bsm.global.utils.WebPushUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Component
@RequiredArgsConstructor
public class MealScheduler {

    private final MealRepository mealRepository;
    private final WebPushRepository webPushRepository;
    private final WebPushUtil webPushUtil;
    private final MealFacade mealFacade;
    private final MealProvider mealProvider;
    @Value("${env.meal.url}")
    private String MEAL_ACCESS_URL;

    @PostConstruct
    public void init() throws IOException {
        getMonthMeal();
    }

    @Scheduled(cron = "* * * 25 * ?")
    private void getMonthMeal() throws IOException {
        LocalDate nextMonth = LocalDate.now().plusMonths(1);
        List<Meal> mealList = mealProvider.getRawMonthMealList(nextMonth).stream()
                .map(meal -> meal.toEntity(mealFacade.filterMealStr(meal.getDDISH_NM())))
                .toList();

        mealRepository.saveAll(mealList);
    }

    @Scheduled(cron = "0 30 6 * * 1-5")
    private void morningNotification() throws JsonProcessingException {
        WebPushSendDto dto = WebPushSendDto.builder()
                .title("오늘의 아침")
                .body(mealFacade.getTodayMealStr(MealType.MORNING))
                .link(MEAL_ACCESS_URL)
                .build();
        sendMealNotification(dto);
    }

    @Scheduled(cron = "0 30 11 * * 1-5")
    private void lunchNotification() throws JsonProcessingException {
        WebPushSendDto dto = WebPushSendDto.builder()
                .title("오늘의 점심")
                .body(mealFacade.getTodayMealStr(MealType.LUNCH))
                .link(MEAL_ACCESS_URL)
                .build();
        sendMealNotification(dto);
    }

    @Scheduled(cron = "0 0 17 * * 1-5")
    private void dinnerNotification() throws JsonProcessingException {
        WebPushSendDto dto = WebPushSendDto.builder()
                .title("오늘의 저녁")
                .body(mealFacade.getTodayMealStr(MealType.DINNER))
                .link(MEAL_ACCESS_URL)
                .build();
        sendMealNotification(dto);
    }

    private void sendMealNotification(WebPushSendDto dto) throws JsonProcessingException {
        List<WebPush> pushList = webPushRepository.findAll();
        webPushUtil.sendNotificationToAll(pushList, dto);
    }

}
