package subway.domain;

public interface FarePolicy {

    int calculate(Sections sections);
}
