package subway.domain;

public class Section {

    private final Long id;
    private final Station upStation;
    private final Station downStation;

    public Section(final Long id, final Station upStation, final Station downStation) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public Section(final Station upStation, final Station downStation) {
        this(null, upStation, downStation);
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }
}
