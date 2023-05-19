package subway.domain.calculator;

public class DefaultFee implements FeeCalculator {
    @Override
    public int calculate(double distance) {
        if (distance <= 10) {
            return 1250;
        }
        int intDistance = (int) distance;
        return 1250 + new UnderFifthKilometer().calculate(intDistance);
    }

    @Override
    public int calculate(int distance) {
        throw new IllegalArgumentException("기본 운임 계산시 double값을 입력하셔야합니다.");
    }
}
