package zerobase.weather.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR("내부 서부 오류"),
    START_DATE_MUST_BE_EARLIER_OR_EQUAL_TO_END_DATE("입력한 끝 날짜가 시작 날짜보다 빠릅니다."),
    TOO_FAR_IN_THE_PAST_OR_FUTURE("너무 과거 혹은 미래의 날짜입니다."),
    FAIL_TO_FETCH_WEATHER_DATA_FROM_API("API에서 날씨 데이터를 가져오는데 실패했습니다."),
    INVALID_DATE("잘못된 날짜 형식입니다."),
    INVALID_REQUEST("잘못된 요청입니다.");
    private final String description;
}
