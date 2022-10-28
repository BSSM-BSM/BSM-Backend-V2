package bssm.bsm.domain.school.meal.service;

import bssm.bsm.domain.school.meal.presentation.dto.RawMealItemDto;
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
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class MealProvider {

    private final OkHttpClient httpClient;
    @Value("${env.api.meal.url}")
    private String MEAL_API_URL;

    public List<RawMealItemDto> getRawMonthMealList(YearMonth date) throws IOException {
        String dateParam = "MLSV_YMD=" + date.getYear() + String.format("%02d", date.getMonthValue());
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
        return new Gson().fromJson(jsonElements, RawMealList);
    }

}
