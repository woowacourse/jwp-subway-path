package subway.exception;

public class ArrivalSameWithDepartureException extends RuntimeException{
    public ArrivalSameWithDepartureException() {
        super("출발역은 도착역과 동일할 수 없습니다.");
    }
}
