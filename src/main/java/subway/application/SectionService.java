package subway.application;

import java.util.List;
import org.springframework.stereotype.Service;
import subway.dao.SectionDao;
import subway.dao.StationDao;
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

    private static void validateNonExistingBaseStation(final Subway subway, final Station baseStation) {
        if (!subway.hasStation(baseStation)) {
            throw new IllegalArgumentException("존재하지 않는 역과의 구간을 등록할 수 없습니다.");
        }
    }

    public void addStations(SectionRequest sectionRequest) {
        Long lineId = sectionRequest.getLineId();
        List<Section> sections = sectionDao.findByLineId(lineId);

        Station nextStation = stationDao.findById(sectionRequest.nextStationId());
        Integer addingDistance = sectionRequest.getSectionStations().getDistance();

        // 빈 노선에 등록하는 경우 (두 역 모두 새로운 역이다)
        if (sections.isEmpty() && sectionRequest.baseStationId() != null) {
            Station baseStation = stationDao.findById(sectionRequest.baseStationId());
            sectionDao.insert(lineId, new Section(baseStation, nextStation, new Distance(addingDistance)));
            return;
        }

        Subway subway = Subway.of(new Line(lineId), sections);
        validateExistingNextStation(nextStation, subway);

        // baseStationId가 없는 경우
        if (sectionRequest.baseStationId() == null) {
            Station startingStation = subway.getStart();
            sectionDao.insert(lineId,
                    new Section(nextStation, startingStation, new Distance(addingDistance)));
            return;
        }

        // baseStationId가 있는 경우
        Station baseStation = stationDao.findById(sectionRequest.baseStationId());
        // 2. 기준 역 다음 등록일 경우
        // 2-1. 기준 역이 해당 노선에 존재하지 않으면 예외
        validateNonExistingBaseStation(subway, baseStation);
        // 2-2. 기준 역이 해당 노선에 존재하는 경우
        // 2-2-1. 현재 기준 역에 우측으로 연결된 구간이 없으면 별도 거리 작업 없이 가장 우측에 새 역을 가지는 구간 추가
        if (!subway.hasRightSection(baseStation)) {
            sectionDao.insert(
                    lineId,
                    new Section(baseStation, nextStation, new Distance(addingDistance)));
            return;
        }
        // 2-2-2. 현재 기준 역에 우측으로 연결된 구간이 있으면 거리 작업 후, 이를 나타내도록 구간 정보 수정 (삭제 및 추가)
        Section existingSection = subway.findRightSection(baseStation);
        int existingDistance = existingSection.getDistance();
        // addingDistance >= existingDistance 이면 예외
        if (addingDistance >= existingDistance) {
            throw new IllegalArgumentException("기존 역 사이 길이보다 크거나 같은 길이의 구간을 등록할 수 없습니다.");
        }
        // addingDistance < existingDistance 이면 거리 계산
        Section leftSplited = new Section(baseStation, nextStation, new Distance(addingDistance));
        Section rightSplited = new Section(nextStation, existingSection.getRight(),
                new Distance(existingDistance - addingDistance));
        sectionDao.deleteByLeftStationIdAndRightStationId(lineId, leftSplited.getLeftId(), rightSplited.getRightId());
        sectionDao.insert(lineId, leftSplited);
        sectionDao.insert(lineId, rightSplited);
    }

    private void validateExistingNextStation(final Station nextStation, final Subway subway) {
        if (subway.hasStation(nextStation)) {
            throw new IllegalArgumentException("노선에 이미 존재하는 역을 등록할 수 없습니다.");
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
            sectionDao.insert(lineId, new Section(left, right, new Distance(leftDistance + rightDistance)));
        }
        sectionDao.deleteByStationId(lineId, stationId);
    }
}
