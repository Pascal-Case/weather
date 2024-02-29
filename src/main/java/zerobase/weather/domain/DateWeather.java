package zerobase.weather.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity(name = "date_weather")
@NoArgsConstructor
@Schema(description = "날씨 정보 캐시 데이터")
public class DateWeather {
    @Id
    @Schema(description = "날짜")
    private LocalDate date;
    @Schema(description = "날씨")
    private String weather;
    @Schema(description = "아이콘")
    private String icon;
    @Schema(description = "기온")
    private double temperature;
}
