package zerobase.weather.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.WeatherApplication;
import zerobase.weather.demo.DateWeather;
import zerobase.weather.demo.Diary;
import zerobase.weather.repository.DateWeatherRepository;
import zerobase.weather.repository.DiaryRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class DiaryServiceImpl implements DiaryService{

    @Autowired
    private final DiaryRepository diaryRepository;

    @Autowired
    private final DateWeatherRepository dateWeatherRepository;

    @Value("${openweathermap.key}")
    private String apiKey;

    @Override
    @Transactional(readOnly = true)
    public List<Diary> readDiary(LocalDate date) {
        return diaryRepository.findAllByDate(date);
    }

    @Override
    public List<Diary> readDiaries(LocalDate startDate, LocalDate endDate) {
        log.debug("read diary");
        return diaryRepository.findAllByDateBetween(startDate, endDate);
    }

    @Override
    public void updateDiary(LocalDate date, String text) {
        Diary nowDiary = diaryRepository.getFirstByDate(date);
        nowDiary.setText(text);
        diaryRepository.save(nowDiary);
    }

    @Override
    public void deleteDiary(LocalDate date) {
       diaryRepository.deleteAllByDate(date);
    }

    @Transactional //DB에 작업
    @Override
    @Scheduled(cron = "0 0 1 * * *") //초 분 시 일 월 (매일 새벽 1시마다 save 발동)
    public void saveWeatherDate() {
        dateWeatherRepository.save(getWeatherFromApi());
    }


    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE) //빡빡한
    public void createService(LocalDate date, String text) {
        log.info("Create Service Start");
//        //open weather map에서 데이터 가져오기
//        String weatherData = getWeatherString();
//
//        //받아온 날씨 json 파싱
//        Map<String, Object> parseWeather = parseWeather(weatherData);

        //날씨 데이터 가져오기 (DB에 있는 값을 가져오기)
        DateWeather dateWeather = getDataWeather(date);


        //파싱된 데이터 + 일기 값 db에 넣기
        Diary nowDiary = new Diary();
//        nowDiary.setWeather(parseWeather.get("main").toString());
//        nowDiary.setIcon(parseWeather.get("icon").toString());
//        nowDiary.setTemperature((Double) parseWeather.get("temp"));

        nowDiary.setDateWeather(dateWeather);
        nowDiary.setText(text);
//        nowDiary.setDate(date);

        diaryRepository.save(nowDiary);
        log.info("Create Service End");
    }

    private DateWeather getDataWeather(LocalDate date) {
        List<DateWeather> dateWeatherList = dateWeatherRepository.findAllByDate(date);
        if (dateWeatherList.size() == 0){
            //새로 api에서 날씨 정보를 가져온다.

            //정책상 .. 현 날씨를 가져오거나, 날씨 없이 일기를 쓰도록
            return getWeatherFromApi();
        } else {
            return dateWeatherList.get(0);
        }
    }

    private String getWeatherString() {
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?=seoul&appid=" + apiKey;

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();//받아온 응답 결과의 코드를 가져옴

            BufferedReader br; //응답 결과 또는 오류 결과
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            StringBuilder resp = new StringBuilder(); //resp에 응답 코드를 쌓는다.
            while ((inputLine = br.readLine()) != null) {
                resp.append(inputLine);
            }
            br.close();
            return resp.toString();

        } catch (Exception e) {
            return "failed to get response";
        }
    }

    //JSON 파싱 하는 작업
    private Map<String, Object> parseWeather(String jsonString){
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;

        try {
            jsonObject = (JSONObject) jsonParser.parse(jsonString);
        } catch (ParseException e) {
            throw new RuntimeException("JSON 응답을 파싱하는 중 오류가 발생했습니다", e);
        }

        HashMap<String, Object> rstMap = new HashMap<>();

        // "weather" 배열이 존재하고 비어 있지 않은지 확인합니다.
        if (jsonObject.containsKey("weather")) {
            JSONArray weatherArray = (JSONArray) jsonObject.get("weather");
            if (!weatherArray.isEmpty()) {
                JSONObject weatherData = (JSONObject) weatherArray.get(0);
                rstMap.put("main", weatherData.get("main"));
                rstMap.put("icon", weatherData.get("icon"));
            } else {
                throw new RuntimeException("날씨 데이터가 비어 있습니다");
            }
        } else {
            throw new RuntimeException("날씨 데이터가 누락되었습니다");
        }

        // "main" 객체가 존재하는지 확인합니다.
        if (jsonObject.containsKey("main")) {
            JSONObject mainData = (JSONObject) jsonObject.get("main");
            rstMap.put("temp", mainData.get("temp"));
        } else {
            throw new RuntimeException("기본 데이터가 누락되었습니다");
        }

        return rstMap;
    }

    private DateWeather getWeatherFromApi() { //파싱도 한번만
        //open weather map에서 데이터 가져오기
        String weatherData = getWeatherString();

        //받아온 날씨 json 파싱
        Map<String, Object> parseWeather = parseWeather(weatherData);

        //파싱된 날씨를 DateWeather 엔티티 내부에 넣어준다
        DateWeather dateWeather = new DateWeather();
        dateWeather.setDate(LocalDate.now()); //날씨를 가져오는 시점을 가져온다.
        dateWeather.setWeather(parseWeather.get("main").toString());
        dateWeather.setIcon(parseWeather.get("icon").toString());
        dateWeather.setTemperature((Double) parseWeather.get("temp"));

        return dateWeather;
    }
}
