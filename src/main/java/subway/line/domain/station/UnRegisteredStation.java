package subway.line.domain.station;

import subway.line.application.exception.NotRegisteredLineException;
import subway.line.domain.station.application.exception.NotRegisteredStationException;

public class UnRegisteredStation extends Station {
    public UnRegisteredStation(String name) {
        super(null, name);
    }

    @Override
    public void changeName(String name) {
        super.changeName(name);
    }

    @Override
    public Long getId() {
        throw new NotRegisteredStationException();
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
