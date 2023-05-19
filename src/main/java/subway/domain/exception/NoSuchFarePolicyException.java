package subway.domain.exception;

public class NoSuchFarePolicyException extends BusinessException {

    public NoSuchFarePolicyException() {
        super("적절한 요금 정책이 없습니다");
    }
}
