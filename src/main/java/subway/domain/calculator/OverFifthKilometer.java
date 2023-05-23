package subway.domain.calculator;

public class OverFifthKilometer implements FeeCalculator {
    @Override
    public int calculate(double distance) {
        throw new IllegalArgumentException("기본운임 계산이 아닐 시 double로 입력해야합니다.");
    }

    @Override
    public int calculate(int distance) {
        distance -= 50;
        return 900 + ((distance - 1) / 8) * 100;
    }
}
