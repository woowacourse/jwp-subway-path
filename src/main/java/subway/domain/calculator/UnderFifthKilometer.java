package subway.domain.calculator;

public class UnderFifthKilometer implements FeeCalculator {

    @Override
    public int calculate(double distance) {
        throw new IllegalArgumentException("기본운임 계산이 아닐 시 double로 입력해야합니다.");
    }

    @Override
    public int calculate(int distance) {
        if (distance <= 50) {
            distance -= 10;
            return 100 + ((distance - 1) / 5) * 100;
        }
        return new OverFifthKilometer().calculate(distance);
    }
}
