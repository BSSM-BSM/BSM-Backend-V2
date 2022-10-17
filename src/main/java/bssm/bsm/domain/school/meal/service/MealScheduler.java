package bssm.bsm.domain.school.meal.service;

import bssm.bsm.domain.school.meal.presentation.dto.RawMealItemDto;
import bssm.bsm.domain.school.meal.domain.Meal;
import bssm.bsm.domain.school.meal.domain.MealType;
import bssm.bsm.domain.school.meal.facade.MealFacade;
import bssm.bsm.domain.school.meal.domain.MealRepository;
import bssm.bsm.domain.webpush.domain.WebPush;
import bssm.bsm.domain.webpush.domain.repository.WebPushRepository;
import bssm.bsm.domain.webpush.presentation.dto.request.WebPushSendDto;
import bssm.bsm.global.utils.WebPushUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@RequiredArgsConstructor
public class MealScheduler {

    private final MealRepository mealRepository;
    private final WebPushRepository webPushRepository;
    private final WebPushUtil webPushUtil;
    private final OkHttpClient httpClient;
    private final MealFacade mealFacade;
    @Value("${env.api.meal.url}")
    private String MEAL_API_URL;
    @Value("${env.meal.url}")
    private String MEAL_ACCESS_URL;

    @Scheduled(cron = "0 0 0 25 * ?")
    private void getMonthMeal() throws IOException {
        LocalDate today = LocalDate.now();
        String dateParam = "MLSV_YMD=" + today.getYear() + String.format("%02d", today.getMonthValue());
        Request mealRequest = new Request.Builder()
                .url(MEAL_API_URL + dateParam)
                .get()
                .build();
        Response mealResponse = httpClient.newCall(mealRequest).execute();

        JsonElement element = JsonParser.parseString(Objects.requireNonNull(mealResponse.body()).string());
        JsonArray jsonElements = element
                .getAsJsonObject().get("mealServiceDietInfo")
                .getAsJsonArray().get(1)
                .getAsJsonObject().get("row")
                .getAsJsonArray();

        Type RawMealList = new TypeToken<List<RawMealItemDto>>(){}.getType();
        List<RawMealItemDto> list = new Gson().fromJson(jsonElements, RawMealList);

        Map<String, Meal> mealMap = new HashMap<>();

        list.forEach(rawMealItem -> {
            String mealDate = rawMealItem.getMLSV_YMD();
            Meal meal = mealMap.get(mealDate);

            if (meal == null) {
                // Init value
                meal = new Meal(LocalDate.parse(rawMealItem.getMLSV_YMD(), DateTimeFormatter.ofPattern("yyyyMMdd")));
            }
            switch (rawMealItem.getMMEAL_SC_NM()) {
                case "조식" -> meal.setMorning(convertMealStr(rawMealItem.getDDISH_NM()));
                case "중식" -> meal.setLunch(convertMealStr(rawMealItem.getDDISH_NM()));
                case "석식" -> meal.setDinner(convertMealStr(rawMealItem.getDDISH_NM()));
            }
            mealMap.put(mealDate, meal);
        });

        mealRepository.saveAll(new ArrayList<>(mealMap.values()));
    }

    public String convertMealStr(String str) {
        return str.replaceAll("<br/>|\\([0-9.]*?\\)|\\(산고\\)", "").trim();
    }

    @Scheduled(cron = "0 30 6 * * 1-5")
    private void morningNotification() throws JsonProcessingException {
        WebPushSendDto dto = WebPushSendDto.builder()
                .title("오늘의 아침")
                .body(mealFacade.getMealStr(MealType.MORNING))
                .link(MEAL_ACCESS_URL)
                .build();
        sendMealNotification(dto);
    }

    @Scheduled(cron = "0 30 11 * * 1-5")
    private void lunchNotification() throws JsonProcessingException {
        WebPushSendDto dto = WebPushSendDto.builder()
                .title("오늘의 점심")
                .body(mealFacade.getMealStr(MealType.LUNCH))
                .link(MEAL_ACCESS_URL)
                .build();
        sendMealNotification(dto);
    }

    @Scheduled(cron = "0 0 17 * * 1-5")
    private void dinnerNotification() throws JsonProcessingException {
        WebPushSendDto dto = WebPushSendDto.builder()
                .title("오늘의 저녁")
                .body(mealFacade.getMealStr(MealType.DINNER))
                .link(MEAL_ACCESS_URL)
                .build();
        sendMealNotification(dto);
    }

    private void sendMealNotification(WebPushSendDto dto) throws JsonProcessingException {
        List<WebPush> pushList = webPushRepository.findAll();
        webPushUtil.sendNotificationToAll(pushList, dto);
    }

}
