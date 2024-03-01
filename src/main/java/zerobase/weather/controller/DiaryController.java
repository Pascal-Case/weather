package zerobase.weather.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import zerobase.weather.domain.Diary;
import zerobase.weather.service.DiaryService;

import java.time.LocalDate;
import java.util.List;

@RestController
@Tag(name = "Diary", description = "Diary API")
public class DiaryController {
    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }


    @Operation(summary = "일기 저장", description = "일기 텍스트와 날씨를 이용해서 DB에 일기 저장")
    @PostMapping("/create/diary")
    void createDiary(
            @Parameter(description = "일기의 날짜", example = "2024-02-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,
            @Parameter(description = "일기 본문", example = "오늘은 Spring 공부를 했다.")
            @RequestBody
            String text
    ) {
        diaryService.createDiary(date, text);
    }


    @Operation(summary = "해당 날짜의 일기 조회", description = "해당 날짜의 모든 일기 데이터를 가져옵니다")
    @GetMapping("/read/diary")
    List<Diary> readDiary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "조회할 날짜", example = "2024-02-01")
            LocalDate date
    ) {
        return diaryService.readDiary(date);
    }

    @Operation(summary = "해당 기간의 일기 조회", description = "해당 기간의 모든 일기 데이터를 가져옵니다")
    @GetMapping("/read/diaries")
    List<Diary> readDiaries(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "조회할 기간의 첫 번째 날", example = "2024-02-01")
            LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "조회할 기간의 마지막 날", example = "2024-02-29")
            LocalDate endDate
    ) {
        return diaryService.readDiaries(startDate, endDate);
    }

    @Operation(summary = "일기 수정", description = "해당 날짜의 첫 번째 일기를 수정합니다.")
    @PutMapping("/update/diary")
    void updateDiary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "수정할 일기의 날짜", example = "2024-02-29")
            LocalDate date,
            @RequestBody
            @Parameter(description = "수정할 일기 본문", example = "오늘은 Spring 공부를 많이 했다.")
            String text
    ) {
        diaryService.updateDiary(date, text);
    }

    @Operation(summary = "일기 삭제", description = "해당 날짜의 모든 일기를 삭제합니다.")
    @DeleteMapping("/delete/diary")
    void deleteDiary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "삭제할 일기의 날짜", example = "2024-02-29")
            LocalDate date
    ) {
        diaryService.deleteDiary(date);
    }
}
