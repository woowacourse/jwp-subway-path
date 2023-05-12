package subway.application;

import java.util.List;
import org.springframework.stereotype.Service;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Direction;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.Subway;
import subway.dto.SectionRequest;

@Service
public class SectionService {

    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public SectionService(final SectionDao sectionDao, final StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public void addStations(SectionRequest sectionRequest) {
        Long lineId = sectionRequest.getLineId();
        List<Section> sections = sectionDao.findByLineId(lineId);
        if (sectionRequest.baseStationId() == null || sectionRequest.nextStationId() == null) {
            throw new IllegalArgumentException("올바른 값을 입력해주세요.");
        }

        Station baseStation = stationDao.findById(sectionRequest.baseStationId());
        Station nextStation = stationDao.findById(sectionRequest.nextStationId());
        Integer addingDistance = sectionRequest.getSectionStations().getDistance();

        if (sections.isEmpty()) {
            insertSectionToEmpty(sectionRequest, lineId, baseStation, nextStation, addingDistance);
            return;
        }

        Subway subway = Subway.of(new Line(lineId), sections);
        validateExistingNextStation(nextStation, subway);
        validateNonExistingBaseStation(subway, baseStation);

        if (sectionRequest.direction().equals(Direction.DOWN)) {
            if (!subway.hasRightSection(baseStation)) {
                insertSection(lineId, baseStation, nextStation, addingDistance);
                return;
            }
            splitAndInsertSectionDown(baseStation, nextStation, addingDistance, subway);
            return;
        }

        if (sections.isEmpty()) {
            insertSection(lineId, nextStation, baseStation, addingDistance);
            return;
        }

        if (!subway.hasLeftSection(baseStation)) {
            insertSection(lineId, nextStation, baseStation, addingDistance);
            return;
        }
        splitAndInsertSectionUp(baseStation, nextStation, addingDistance, subway);
    }

    private void insertSectionToEmpty(final SectionRequest sectionRequest, final Long lineId, final Station baseStation,
                                      final Station nextStation, final Integer addingDistance) {
        if (sectionRequest.direction().equals(Direction.DOWN)) {
            insertSection(lineId, baseStation, nextStation, addingDistance);
            return;
        }
        insertSection(lineId, nextStation, baseStation, addingDistance);
        return;
    }

    private void insertSection(final Long lineId, final Station leftStation, final Station rightStation,
                               final Integer addingDistance) {
        sectionDao.insert(lineId, new Section(leftStation, rightStation, new Distance(addingDistance)));
    }

    private void splitAndInsertSectionDown(final Station baseStation, final Station nextStation,
                                           final Integer addingDistance, final Subway subway) {
        Section existingSection = subway.findRightSection(baseStation);
        int existingDistance = existingSection.getDistance();
        if (addingDistance >= existingDistance) {
            throw new IllegalArgumentException("기존 역 사이 길이보다 크거나 같은 길이의 구간을 등록할 수 없습니다.");
        }
        Long lineId = subway.getLine().getId();
        Section leftSplited = new Section(baseStation, nextStation, new Distance(addingDistance));
        Section rightSplited = new Section(nextStation, existingSection.getRight(),
                new Distance(existingDistance - addingDistance));
        sectionDao.deleteByLeftStationIdAndRightStationId(lineId, leftSplited.getLeftId(), rightSplited.getRightId());
        sectionDao.insert(lineId, leftSplited);
        sectionDao.insert(lineId, rightSplited);
    }

    private void splitAndInsertSectionUp(final Station baseStation, final Station nextStation,
                                         final Integer addingDistance, final Subway subway) {
        Section existingSection = subway.findLeftSection(baseStation);
        int existingDistance = existingSection.getDistance();
        if (addingDistance >= existingDistance) {
            throw new IllegalArgumentException("기존 역 사이 길이보다 크거나 같은 길이의 구간을 등록할 수 없습니다.");
        }
        Long lineId = subway.getLine().getId();
        Section leftSplited = new Section(existingSection.getLeft(), nextStation,
                new Distance(existingDistance - addingDistance));
        Section rightSplited = new Section(nextStation, baseStation, new Distance(addingDistance));
        sectionDao.deleteByLeftStationIdAndRightStationId(lineId, leftSplited.getLeftId(), rightSplited.getRightId());
        sectionDao.insert(lineId, leftSplited);
        sectionDao.insert(lineId, rightSplited);
    }

    private void validateExistingNextStation(final Station nextStation, final Subway subway) {
        if (subway.hasStation(nextStation)) {
            throw new IllegalArgumentException("노선에 이미 존재하는 역을 등록할 수 없습니다.");
        }
    }

    private void validateNonExistingBaseStation(final Subway subway, final Station baseStation) {
        if (!subway.hasStation(baseStation)) {
            throw new IllegalArgumentException("존재하지 않는 역과의 구간을 등록할 수 없습니다.");
        }
    }

    public void deleteStation(Long lineId, Long stationId) {
        Station station = stationDao.findById(stationId);

        List<Section> sections = sectionDao.findByLineId(lineId);
        Subway subway = Subway.of(new Line(lineId), sections);

        if (!subway.hasStation(station)) {
            throw new IllegalArgumentException("역이 존재하지 않습니다.");
        }

        if (subway.hasLeftSection(station) && subway.hasRightSection(station)) {
            Section rightSection = subway.findRightSection(station);
            Section leftSection = subway.findLeftSection(station);
            Station right = rightSection.getRight();
            Station left = leftSection.getLeft();
            int leftDistance = leftSection.getDistance();
            int rightDistance = rightSection.getDistance();
            insertSection(lineId, left, right, leftDistance + rightDistance);
        }
        sectionDao.deleteByStationId(lineId, stationId);
    }
}
