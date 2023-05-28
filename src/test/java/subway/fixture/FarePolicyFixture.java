package subway.fixture;

import subway.domain.fare.FarePolicy;

public abstract class FarePolicyFixture {

    public static FarePolicy 테스트용_비용_정책() {
        return fare -> {
            throw new UnsupportedOperationException("테스트용 비용 정책으로 계산할 수 없습니다.");
        };
    }
}
