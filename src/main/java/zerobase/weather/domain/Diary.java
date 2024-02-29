package zerobase.weather.domain;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "diary")
@Builder
@Schema(description = "날씨 일기 데이터")
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "id")
    private int id;
    @Schema(description = "날씨")
    private String weather;
    @Schema(description = "아이콘")
    private String icon;
    @Schema(description = "기온")
    private double temperature;
    @Schema(description = "일기 본문")
    private String text;
    @Schema(description = "날짜")
    private LocalDate date;

    @Hidden
    public void setDateWeather(DateWeather dateWeather) {
        this.date = dateWeather.getDate();
        this.weather = dateWeather.getWeather();
        this.icon = dateWeather.getIcon();
        this.temperature = dateWeather.getTemperature();
    }
}
