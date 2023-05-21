package subway.domain.section.general;

import subway.domain.line.Line;
import subway.domain.section.Distance;
import subway.domain.station.Station;

public class NewGeneralSectionMaker {

    public static GeneralSection makeSectionToSave(Station upStation,
                                                   Station downStation, Distance distance) {
        NearbyStations sectionToSaveNearByStations =
                NearbyStations.createByUpStationAndDownStation(upStation, downStation);

        Line line = sectionToSaveNearByStations.getDownStation().getLine();

        return new GeneralSection(null, sectionToSaveNearByStations, line, distance);
    }

    public static GeneralSection makeNewUpSection(GeneralSection sectionToSave, GeneralSection currentSection) {
        NearbyStations newUpSectionNearByStations
                = NearbyStations.createByUpStationAndDownStation(currentSection.getUpStation(), sectionToSave.getUpStation());
        Line line = sectionToSave.getLine();

        Distance currentSectionDistance = new Distance(currentSection.getDistance());
        Distance downSectionDistance = new Distance(sectionToSave.getDistance());

        if (downSectionDistance.isGreaterThanOrEqual(currentSectionDistance)) {
            throw new IllegalArgumentException("등록할 구간의 길이는 기존 구간 길이보다 작아야합니다.");
        }
        Distance newUpSectionDistance = currentSectionDistance.subtract(downSectionDistance);

        return new GeneralSection(null, newUpSectionNearByStations, line, newUpSectionDistance);
    }

    public static GeneralSection makeNewDownSection(GeneralSection sectionToSave, GeneralSection currentSection) {
        NearbyStations newDownSectionNearByStations
                = NearbyStations.createByUpStationAndDownStation(sectionToSave.getDownStation(), currentSection.getDownStation());
        Line line = sectionToSave.getLine();

        Distance currentSectionDistance = new Distance(currentSection.getDistance());
        Distance upSectionDistance = new Distance(sectionToSave.getDistance());

        if (upSectionDistance.isGreaterThanOrEqual(currentSectionDistance)) {
            throw new IllegalArgumentException("등록할 구간의 길이는 기존 구간 길이보다 작아야합니다.");
        }
        Distance newDownSectionDistance = currentSectionDistance.subtract(upSectionDistance);

        return new GeneralSection(null, newDownSectionNearByStations, line, newDownSectionDistance);
    }
}
