package zerobase.weather.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import zerobase.weather.demo.Diary;
import zerobase.weather.service.DiaryServiceImpl;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.format.annotation.DateTimeFormat.*;

@RestController //상태 코드들을 controller에서 지정을 해서 보내준다.
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryServiceImpl diaryService;

    @PostMapping("/create/diary")
    public void createDiary(
            @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate date,
            //Date는 여러 형식으로 저장이 될 수 있다. -> @DateTimeFormat(iso = ISO.DATE) format을 하나로 정해준다.
            @RequestBody String text
    ) {
        diaryService.createService(date, text);
    }

    @GetMapping("/read/diary")
    public List<Diary> readDiary(
            @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate date
    ) {
        return diaryService.readDiary(date);
    }

    @GetMapping("/read/diaries")
    public List<Diary> readDiaries(
            @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate endDate
    ) {
        return diaryService.readDiaries(startDate, endDate);
    }

    @PutMapping("/update/diary")
    public void updateDiary(
            @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate date,
            @RequestBody String text
    ) {
        diaryService.updateDiary(date, text);
    }

    @DeleteMapping("/delete/diary")
    public void deleteDiary(
            @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate date
            ){
        diaryService.deleteDiary(date);
    }
}
