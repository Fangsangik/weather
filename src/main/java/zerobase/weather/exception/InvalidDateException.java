package zerobase.weather.exception;

public class InvalidDateException extends RuntimeException{
    private static final String MESSAGE = "너무 과거 또는 미래의 날씨 입니다";

    public InvalidDateException(){

    }
}
