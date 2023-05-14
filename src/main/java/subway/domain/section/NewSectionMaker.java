package subway.domain.section;

import subway.domain.line.Line;
import subway.domain.station.Station;

public class NewSectionMaker {

    public static Section makeSectionToSave(Line line, Station upStation,
                                            Station downStation, Distance distance) {
        NearbyStations sectionToSaveNearByStations =
                NearbyStations.createByUpStationAndDownStation(upStation, downStation);

        return new Section(null, sectionToSaveNearByStations, line, distance);
    }

    public static Section makeNewUpSection(Section sectionToSave, Section currentSection) {
        NearbyStations newUpSectionNearByStations
                = NearbyStations.createByUpStationAndDownStation(currentSection.getUpStation(), sectionToSave.getUpStation());
        Line line = sectionToSave.getLine();

        Distance currentSectionDistance = new Distance(currentSection.getDistance().getDistance());
        Distance downSectionDistance = sectionToSave.getDistance();

        if (downSectionDistance.isGreaterThanOrEqual(currentSectionDistance)) {
            throw new IllegalArgumentException("등록할 구간의 길이는 기존 구간 길이보다 작아야합니다.");
        }
        Distance newUpSectionDistance = currentSectionDistance.subtract(downSectionDistance);

        return new Section(null, newUpSectionNearByStations, line, newUpSectionDistance);
    }

    public static Section makeNewDownSection(Section sectionToSave, Section currentSection) {
        NearbyStations newDownSectionNearByStations
                = NearbyStations.createByUpStationAndDownStation(currentSection.getDownStation(), sectionToSave.getDownStation());
        Line line = sectionToSave.getLine();

        Distance currentSectionDistance = new Distance(currentSection.getDistance().getDistance());
        Distance upSectionDistance = sectionToSave.getDistance();

        if (upSectionDistance.isGreaterThanOrEqual(currentSectionDistance)) {
            throw new IllegalArgumentException("등록할 구간의 길이는 기존 구간 길이보다 작아야합니다.");
        }
        Distance newDownSectionDistance = currentSectionDistance.subtract(upSectionDistance);

        return new Section(null, newDownSectionNearByStations   , line, newDownSectionDistance);
    }
}
