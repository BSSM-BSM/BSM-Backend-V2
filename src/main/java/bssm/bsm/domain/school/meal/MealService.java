package bssm.bsm.domain.school.meal;

import bssm.bsm.domain.school.meal.dto.RawMealItemDto;
import bssm.bsm.domain.school.meal.dto.response.MealResponseDto;
import bssm.bsm.domain.school.meal.entities.Meal;
import bssm.bsm.domain.school.meal.repositories.MealRepository;
import bssm.bsm.global.exceptions.NotFoundException;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MealService {

    private final MealRepository mealRepository;
    private final OkHttpClient httpClient;
    @Value("${env.api.meal.url}")
    private String GET_MEAL_URL;

    public MealResponseDto getMeal(LocalDate date) {
         Meal meal = mealRepository.findById(date).orElseThrow(
                () -> {throw new NotFoundException();}
         );
         return MealResponseDto.builder()
                 .morning(meal.getMorning())
                 .lunch(meal.getLunch())
                 .dinner(meal.getDinner())
                 .build();
    }

    @Scheduled(cron = "0 0 0 25 * ?")
    private void getMonthMeal() throws IOException {
        LocalDate today = LocalDate.now();
        String dateParam = "MLSV_YMD=" + today.getYear() + String.format("%02d", today.getMonthValue());
        Request mealRequest = new Request.Builder()
                .url(GET_MEAL_URL + dateParam)
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
}
