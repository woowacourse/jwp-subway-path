package subway.domain.fare;

public interface FarePolicy {
    FareInfo doCalculate(final FareInfo fareInfo);
}
