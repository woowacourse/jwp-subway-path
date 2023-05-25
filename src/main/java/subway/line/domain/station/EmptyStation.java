package subway.line.domain.station;

public class EmptyStation extends Station {

    public EmptyStation() {
        super(null, null);
    }

    @Override
    public boolean equals(Object o) {
        return getClass() == o.getClass();
    }
}
