package zerobase.weather.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import zerobase.weather.domain.DateWeather;
import zerobase.weather.domain.Diary;
import zerobase.weather.repository.DateWeatherRepository;
import zerobase.weather.repository.DiaryRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiaryServiceTest {

    @Mock
    private DateWeatherRepository dateWeatherRepository;

    @Mock
    private DiaryRepository diaryRepository;

    @Spy
    @InjectMocks
    private DiaryService diaryService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(diaryService, "apikey", "test_api_key");
    }

    @Test
    void saveWeatherDate() {
        // given
        DateWeather mockWeatherData = new DateWeather();
        mockWeatherData.setDate(LocalDate.now());
        mockWeatherData.setWeather("Sunny");
        mockWeatherData.setTemperature(32.0);

        doReturn(mockWeatherData).when(diaryService).getWeatherFromApi();

        // when
        diaryService.saveWeatherDate();

        // then
        verify(dateWeatherRepository).save(any(DateWeather.class));
    }

    @Test
    void createDiary() {
        // Given
        LocalDate date = LocalDate.now();
        String text = "Test diary";
        DateWeather dateWeather = new DateWeather();
        dateWeather.setDate(date);
        dateWeather.setWeather("Sunny");
        when(dateWeatherRepository.findAllByDate(date))
                .thenReturn(Collections.singletonList(dateWeather));

        // When
        diaryService.createDiary(date, text);

        // Then
        verify(diaryRepository, times(1)).save(any(Diary.class));
    }

    @Test
    void readDiary() {
        // Given
        LocalDate date = LocalDate.now();
        List<Diary> expectedDiaries = Collections.singletonList(new Diary());
        when(diaryRepository.findAllByDate(date)).thenReturn(expectedDiaries);

        // When
        List<Diary> diaries = diaryService.readDiary(date);

        // Then
        assertEquals(expectedDiaries, diaries);
        verify(diaryRepository, times(1)).findAllByDate(date);
    }

    @Test
    void readDiaries() {
        // Given
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(30);
        List<Diary> expectedDiaries = Arrays.asList(new Diary(), new Diary());
        when(diaryRepository.findAllByDateBetween(startDate, endDate))
                .thenReturn(expectedDiaries);

        // When
        List<Diary> diaries = diaryService.readDiaries(startDate, endDate);

        // Then
        assertEquals(expectedDiaries, diaries);
        verify(diaryRepository, times(1))
                .findAllByDateBetween(startDate, endDate);
    }

    @Test
    void updateDiary() {
        // Given
        LocalDate date = LocalDate.now();
        String updatedText = "Updated diary";
        Diary diary = new Diary();
        diary.setDate(date);
        diary.setText("Original diary");
        when(diaryRepository.getFirstByDate(date)).thenReturn(diary);

        // When
        diaryService.updateDiary(date, updatedText);

        // Then
        assertEquals(updatedText, diary.getText());
        verify(diaryRepository, times(1)).save(diary);
    }

    @Test
    void deleteDiary() {
        // Given
        LocalDate date = LocalDate.now();

        // When
        diaryService.deleteDiary(date);

        // Then
        verify(diaryRepository, times(1)).deleteAllByDate(date);
    }

    @Test
    void getDateWeatherFromDb() {
        // Given
        LocalDate date = LocalDate.now();
        DateWeather expectedWeather = new DateWeather();
        when(dateWeatherRepository.findAllByDate(date)).thenReturn(Collections.singletonList(expectedWeather));

        // When
        DateWeather result = diaryService.getDateWeather(date);

        // Then
        assertEquals(expectedWeather, result);
        verify(dateWeatherRepository, times(1)).findAllByDate(date);
    }
}