package zerobase.weather.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.WeatherApplication;
import zerobase.weather.domain.DateWeather;
import zerobase.weather.domain.Diary;
import zerobase.weather.exception.DiaryException;
import zerobase.weather.repository.DateWeatherRepository;
import zerobase.weather.repository.DiaryRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static zerobase.weather.type.ErrorCode.*;

@Service
@Transactional(readOnly = true)
public class DiaryService {
    @Value("${openWeatherMap.key}")
    private String apikey;

    private final DateWeatherRepository dateWeatherRepository;
    private final DiaryRepository diaryRepository;

    private static final Logger logger = LoggerFactory.getLogger(WeatherApplication.class);

    public DiaryService(DateWeatherRepository dateWeatherRepository, DiaryRepository diaryRepository) {
        this.dateWeatherRepository = dateWeatherRepository;
        this.diaryRepository = diaryRepository;
    }

    @Transactional
    @Scheduled(cron = "0 0 1 * * *") // 매일 01시 0분 0초에 실행
    public void saveWeatherDate() {
        logger.info("Start saving weather data.");
        dateWeatherRepository.save(getWeatherFromApi());
        logger.info("Weather data saved successfully.");
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void createDiary(LocalDate date, String text) {
        logger.info("Start creating diary for date: {}", date);
        validateDate(date);

        // 날씨 데이터 가져오기 (API OR DB)
        DateWeather dateWeather = getDateWeather(date);

        // 파싱된 데이터 + 일기 데이터 DB에 넣기
        Diary nowDiary = new Diary();
        nowDiary.setDateWeather(dateWeather);
        nowDiary.setText(text);
        nowDiary.setDate(date);

        diaryRepository.save(nowDiary);
        logger.info("Diary created successfully.");
    }

    @Transactional(readOnly = true)
    public List<Diary> readDiary(LocalDate date) {
        logger.info("Start getting diary for date: {}", date);
        validateDate(date);

        List<Diary> diaryList = diaryRepository.findAllByDate(date);
        logger.info("Diary retrieved successfully.");
        return diaryList;
    }

    public List<Diary> readDiaries(LocalDate startDate, LocalDate endDate) {
        logger.info("Start getting diary for date between {} and {}", startDate, endDate);
        validateDate(startDate);
        validateDate(endDate);

        if (startDate.isAfter(endDate)) {
            throw new DiaryException(START_DATE_MUST_BE_EARLIER_OR_EQUAL_TO_END_DATE);
        }

        List<Diary> diaryList = diaryRepository.findAllByDateBetween(startDate, endDate);
        logger.info("Diary retrieved successfully.");
        return diaryList;
    }

    @Transactional
    public void updateDiary(LocalDate date, String text) {
        logger.info("Start updating diary for date: {}", date);
        validateDate(date);
        Diary nowDiary = diaryRepository.getFirstByDate(date);
        nowDiary.setText(text);
        diaryRepository.save(nowDiary);
        logger.info("Diary updated successfully.");
    }

    @Transactional
    public void deleteDiary(LocalDate date) {
        logger.info("Start deleting diary for date: {}", date);
        validateDate(date);
        diaryRepository.deleteAllByDate(date);
        logger.info("Diary deleted successfully.");
    }

    private DateWeather getDateWeather(LocalDate date) {
        logger.info("Start getting dateWeather for date: {}", date);
        validateDate(date);
        List<DateWeather> dateWeatherListFromDB = dateWeatherRepository.findAllByDate(date);
        if (dateWeatherListFromDB.isEmpty()) {
            // 과거 데이터 -> 과금
            // 따라서, 정책 : 과거 날짜의 경우 현재 날씨를 가져온다.

            return getWeatherFromApi();
        }
        DateWeather dateWeather = dateWeatherListFromDB.get(0);
        logger.info("DateWeather retrieved successfully.");
        return dateWeather;
    }

    private DateWeather getWeatherFromApi() {
        logger.info("Start getting dateWeather from APi");
        String weatherData = getWeatherString();
        Map<String, Object> parsedWeather = parseWeather(weatherData);
        DateWeather dateWeather = new DateWeather();
        dateWeather.setDate(LocalDate.now());
        dateWeather.setWeather(parsedWeather.get("main").toString());
        dateWeather.setIcon(parsedWeather.get("icon").toString());
        dateWeather.setTemperature((double) parsedWeather.get("temp"));
        logger.info("DateWeather retrieved successfully from API.");
        return dateWeather;
    }

    private String getWeatherString() {
        logger.info("Start getting weather data string from API.");
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=seoul&&APPID=" + apikey;
        try {
            BufferedReader br = getBufferedReader(apiUrl);

            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            logger.info("Weather data string retrieved successfully from API.");
            return response.toString();
        } catch (IOException e) {
            throw new DiaryException(FAIL_TO_FETCH_WEATHER_DATA_FROM_API);
        }
    }

    private static BufferedReader getBufferedReader(String apiUrl) throws IOException {
        logger.info("Start getting buffered reader from api url.");
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        BufferedReader br;
        if (responseCode == 200) {
            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        } else {
            br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        }
        logger.info("Buffered reader retrieved successfully from api url .");
        return br;
    }

    private Map<String, Object> parseWeather(String jsonString) {
        logger.info("Start parsing weather json string.");
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;

        try {
            jsonObject = (JSONObject) jsonParser.parse(jsonString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Map<String, Object> resultMap = new HashMap<>();
        JSONObject mainData = (JSONObject) jsonObject.get("main");
        resultMap.put("temp", mainData.get("temp"));

        JSONObject weatherData = (JSONObject) ((JSONArray) jsonObject.get("weather")).get(0);
        resultMap.put("main", weatherData.get("main"));
        resultMap.put("icon", weatherData.get("icon"));
        logger.info("Weather json string parsed successfully.");
        return resultMap;
    }

    public void validateDate(LocalDate date) {
        if (date.isAfter(LocalDate.of(2100, 1, 1))
                || date.isBefore(LocalDate.of(1900, 1, 1))) {
            throw new DiaryException(TOO_FAR_IN_THE_PAST_OR_FUTURE);
        }
    }
}
