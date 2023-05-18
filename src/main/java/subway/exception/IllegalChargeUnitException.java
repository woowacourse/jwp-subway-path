package subway.exception;

public class IllegalChargeUnitException extends IllegalArgumentException{
    public IllegalChargeUnitException() {
        super("추가 운임 거리 단위는 양수여야합니다.");
    }
}
