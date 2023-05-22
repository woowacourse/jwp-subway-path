package subway.domain.vo;

import java.util.List;

public class Shortcut {
    private final List<Station> path;
    private final int fee;

    public Shortcut(List<Station> path, int fee) {
        this.path = path;
        this.fee = fee;
    }

    public List<Station> getPath() {
        return path;
    }

    public int getFee() {
        return fee;
    }
}
