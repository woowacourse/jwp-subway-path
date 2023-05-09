package subway.domain;

import java.util.Objects;

public class MyStation {

    private final String name;

    public MyStation(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MyStation myStation = (MyStation) o;
        return Objects.equals(name, myStation.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
