package zerobase.weather.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import zerobase.weather.type.ErrorCode;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class DiaryException extends RuntimeException {
    private ErrorCode errorCode;
    private String errorMessage;

    public DiaryException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
