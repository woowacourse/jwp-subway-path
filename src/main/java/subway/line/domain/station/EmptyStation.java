package subway.line.domain.station;

public class EmptyStation extends Station {
    @Override
    public boolean equals(Object o) {
        return getClass() == o.getClass();
    }
}
