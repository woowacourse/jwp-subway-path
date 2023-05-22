package subway.line.domain.station;

public class UnRegisteredStation extends Station {

    public static final String NOT_REGISTERED_STATION = "식별자가 등록되지 않은 역입니다.";

    public UnRegisteredStation(String name) {
        super(null, name);
    }

    @Override
    public void changeName(String name) {
        super.changeName(name);
    }

    @Override
    public Long getId() {
        throw new IllegalArgumentException(NOT_REGISTERED_STATION);
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
