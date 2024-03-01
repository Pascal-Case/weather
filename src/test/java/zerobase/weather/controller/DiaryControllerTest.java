package zerobase.weather.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import zerobase.weather.domain.Diary;
import zerobase.weather.service.DiaryService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DiaryController.class)
class DiaryControllerTest {

    @MockBean
    private DiaryService diaryService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Diary diary;

    @BeforeEach
    void setDiary() {
        diary = Diary.builder()
                .id(123)
                .date(LocalDate.of(2024, 3, 1))
                .text("Test diary")
                .build();
    }

    @Test
    void createDiary() throws Exception {
        // when & then
        mockMvc.perform(post("/create/diary")
                        .param("date", "2024-03-01")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("Test diary"))
                .andExpect(status().isOk());
    }

    @Test
    void readDiary() throws Exception {
        // given
        given(diaryService.readDiary(LocalDate.of(2024, 3, 1)))
                .willReturn(Collections.singletonList(diary));
        // when & then
        mockMvc.perform(get("/read/diary")
                        .param("date", "2024-03-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].date")
                        .value("2024-03-01"))
                .andExpect(jsonPath("$[0].text")
                        .value("Test diary"));
    }

    @Test
    void readDiaries() throws Exception {
        // Given
        List<Diary> diaries = Collections.singletonList(diary);

        given(diaryService.readDiaries(
                LocalDate.of(2024, 3, 1),
                LocalDate.of(2024, 3, 31)
        ))
                .willReturn(diaries);

        // When & Then
        mockMvc.perform(get("/read/diaries")
                        .param("startDate", "2024-03-01")
                        .param("endDate", "2024-03-31"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].text").value("Test diary"));
    }

    @Test
    void updateDiary() throws Exception {
        // When & Then
        mockMvc.perform(put("/update/diary")
                        .param("date", "2024-03-01")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("Updated test diary"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteDiary() throws Exception {
        // When & Then
        mockMvc.perform(delete("/delete/diary")
                        .param("date", "2024-03-01"))
                .andExpect(status().isOk());
    }
}