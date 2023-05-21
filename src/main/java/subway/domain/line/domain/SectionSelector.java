package subway.domain.line.domain;

import subway.domain.line.entity.SectionEntity;

import java.util.Optional;

public class SectionSelector {
    private final Optional<SectionEntity> upSectionOfUpStation;
    private final Optional<SectionEntity> downSectionOfUpStation;
    private final Optional<SectionEntity> upSectionOfDownStation;
    private final Optional<SectionEntity> downSectionOfDownStation;

    public SectionSelector(final Optional<SectionEntity> upSectionOfUpStation, final Optional<SectionEntity> downSectionOfUpStation, final Optional<SectionEntity> upSectionOfDownStation, final Optional<SectionEntity> downSectionOfDownStation) {
        this.upSectionOfUpStation = upSectionOfUpStation;
        this.downSectionOfUpStation = downSectionOfUpStation;
        this.upSectionOfDownStation = upSectionOfDownStation;
        this.downSectionOfDownStation = downSectionOfDownStation;
    }

    public boolean isEndSection(){
        return (upSectionOfUpStation.isPresent() && upSectionOfDownStation.isEmpty() && downSectionOfUpStation.isEmpty() && downSectionOfDownStation.isEmpty())
                || (upSectionOfUpStation.isEmpty() && upSectionOfDownStation.isPresent() && downSectionOfUpStation.isEmpty() && downSectionOfDownStation.isEmpty())
                || (upSectionOfUpStation.isEmpty() && upSectionOfDownStation.isEmpty() && downSectionOfUpStation.isPresent() && downSectionOfDownStation.isEmpty())
                || (upSectionOfUpStation.isEmpty() && upSectionOfDownStation.isEmpty() && downSectionOfUpStation.isEmpty() && downSectionOfDownStation.isPresent());
    }

    public boolean isUpSection(){
        return upSectionOfUpStation.isEmpty() &&
                upSectionOfDownStation.isPresent() &&
                downSectionOfUpStation.isEmpty() &&
                downSectionOfDownStation.isPresent();
    }

    public boolean isDownSection(){
        return upSectionOfUpStation.isPresent() &&
                upSectionOfDownStation.isEmpty() &&
                downSectionOfUpStation.isPresent() &&
                downSectionOfDownStation.isEmpty();
    }

    public boolean isNotFoundSection(){
        return (upSectionOfUpStation.isEmpty() && upSectionOfDownStation.isEmpty() && downSectionOfUpStation.isEmpty() && downSectionOfDownStation.isEmpty());
    }
}
