package subway.domain;

public interface SubwayFareStrategy {

    Fare calculteFare(Distance distance);
}
