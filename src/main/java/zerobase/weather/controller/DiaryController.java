package zerobase.weather.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import zerobase.weather.demo.Diary;
import zerobase.weather.exception.InvalidDateException;
import zerobase.weather.service.DiaryServiceImpl;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.format.annotation.DateTimeFormat.*;

@RestController //상태 코드들을 controller에서 지정을 해서 보내준다.
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryServiceImpl diaryService;

    @ApiOperation(value = "인기 텍스트와 날씨를 이용해서 DB에 저장", notes = "note")
    @PostMapping("/create/diary")
    public void createDiary(
            @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate date,
            //Date는 여러 형식으로 저장이 될 수 있다. -> @DateTimeFormat(iso = ISO.DATE) format을 하나로 정해준다.
            @RequestBody String text
    ) {
        diaryService.createService(date, text);
    }

    @ApiOperation("선텍한 날씨의 모든 일기 데이터를 가져옵니다")
    @GetMapping("/read/diary")
    public List<Diary> readDiary(
            @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate date
    ) {
        return diaryService.readDiary(date);
    }

    @ApiOperation("선택한 기간중의 모든 일기 데이터를 가져옵니다.")
    @GetMapping("/read/diaries")
    public List<Diary> readDiaries(
            @RequestParam @DateTimeFormat(iso = ISO.DATE)
            @ApiParam(value = "조회할 기간의 첫 날", example = "2024-03-04") LocalDate startDate,

            @RequestParam @DateTimeFormat(iso = ISO.DATE)
            @ApiParam(value = "조회할 기간의 마지막 날", example = "2024-03-04") LocalDate endDate
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
    ) {
        diaryService.deleteDiary(date);
    }

    //@ExceptionHandler(InvalidDateException.class)
    //컨트롤러가 많아지면 각각 exceptionhandler 처리 하기 어렵다.
    //동일한 기능이 많을 경우에는 매번 넣어주는 것은 비 효율적

}
