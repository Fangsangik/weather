package zerobase.weather.service;

import org.springframework.stereotype.Service;
import zerobase.weather.demo.Diary;

import java.time.LocalDate;
import java.util.List;

@Service
public interface DiaryService {
    void createService(LocalDate date, String text);

    List<Diary> readDiary(LocalDate date);

    List<Diary> readDiaries(LocalDate startDate, LocalDate endDate);

    void updateDiary(LocalDate date, String text);

    void deleteDiary(LocalDate date);

    void saveWeatherDate();
}
