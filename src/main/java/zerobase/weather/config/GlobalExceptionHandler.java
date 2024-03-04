package zerobase.weather.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice //전역의 예외 처리
public class GlobalExceptionHandler { // 클래스 내부에 전역 예외가 모일 수 있도록

    //client -> server
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class) //controller 안에 있는 예외 처리
    public Exception handleException() {
        log.error("error from GlobalExceptionHandler");
        return new Exception();
    }
}
