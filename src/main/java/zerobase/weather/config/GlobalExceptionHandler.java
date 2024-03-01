package zerobase.weather.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import zerobase.weather.dto.ErrorResponse;
import zerobase.weather.exception.DiaryException;

import static zerobase.weather.type.ErrorCode.INTERNAL_SERVER_ERROR;
import static zerobase.weather.type.ErrorCode.INVALID_REQUEST;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DiaryException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDiaryException(DiaryException e) {
        logger.error("DiaryException occurred: {}", e.getMessage(), e);
        return new ErrorResponse(
                e.getErrorCode(),
                e.getErrorMessage()
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        logger.error("MethodArgumentTypeMismatchException occurred: {}", e.getMessage(), e);
        return new ErrorResponse(
                INVALID_REQUEST,
                INVALID_REQUEST.getDescription()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.error("MethodArgumentNotValidException occurred: {}", e.getMessage(), e);
        return new ErrorResponse(
                INVALID_REQUEST,
                INVALID_REQUEST.getDescription());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        logger.error("DataIntegrityViolationException occurred: {}", e.getMessage(), e);
        return new ErrorResponse(
                INVALID_REQUEST,
                INVALID_REQUEST.getDescription());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception e) {
        logger.error("Exception occurred: {}", e.getMessage(), e);
        return new ErrorResponse(
                INTERNAL_SERVER_ERROR,
                INTERNAL_SERVER_ERROR.getDescription());
    }
}
