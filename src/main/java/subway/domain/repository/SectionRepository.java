package subway.domain.repository;

import java.util.List;

import subway.domain.Section;
import subway.domain.Station;

public interface SectionRepository {

    List<Section> findAll();

    void createSection(String lineName, List<Section> sections);

    List<Section> findAllByLineName(String lineName);

    void deleteBySection(String lineName, String upStation, String downStation);

    Section findIdByUpDown(String upStation, String downStation);

    List<Section> findSectionsContainStation(Station station);
}
