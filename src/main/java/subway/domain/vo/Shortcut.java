package subway.domain.vo;

import java.util.List;

public class Shortcut {
    private final List<Station> path;
    private final int Fee;

    public Shortcut(List<Station> path, int fee) {
        this.path = path;
        Fee = fee;
    }

    public List<Station> getPath() {
        return path;
    }

    public int getFee() {
        return Fee;
    }
}
