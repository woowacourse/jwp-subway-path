package subway.exception;

public class IllegalSurchargeException extends SubwayException {

    public IllegalSurchargeException() {
        super("노선별 추가 요금은 음수일 수 없습니다.");
    }
}
