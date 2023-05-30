package subway.dto;

import subway.domain.Line;
import subway.domain.Section;

import java.util.Objects;

public class SectionResponse {

    private StationResponse upStation;
    private StationResponse downStation;
    private LineResponse line;
    private Integer distance;

    public SectionResponse() {
    }

    public SectionResponse(final StationResponse upStation, final StationResponse downStation, final LineResponse line, final Integer distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.line = line;
        this.distance = distance;
    }

    public static SectionResponse of(final Section section, final Line line) {
        return new SectionResponse(
                StationResponse.from(section.getUpStation()),
                StationResponse.from(section.getDownStation()),
                LineResponse.from(line),
                section.getDistance().getValue()
        );
    }

    public StationResponse getUpStation() {
        return upStation;
    }

    public StationResponse getDownStation() {
        return downStation;
    }

    public LineResponse getLine() {
        return line;
    }

    public Integer getDistance() {
        return distance;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SectionResponse that = (SectionResponse) o;
        return Objects.equals(upStation, that.upStation)
                && Objects.equals(downStation, that.downStation)
                && Objects.equals(line, that.line)
                && Objects.equals(distance, that.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(upStation, downStation, line, distance);
    }

    @Override
    public String toString() {
        return "SectionResponse{" +
                "upStation=" + upStation +
                ", downStation=" + downStation +
                ", line=" + line +
                ", distance=" + distance +
                '}';
    }
}
