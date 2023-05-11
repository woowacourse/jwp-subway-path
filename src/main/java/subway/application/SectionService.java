package subway.application;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.dto.SectionDto;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dao.dto.LineDto;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.ui.dto.SectionRequest;

@Service
public class SectionService {

    private final SectionDao sectionDao;
    private final LineDao lineDao;
    private final StationDao stationDao;

    public SectionService(SectionDao sectionDao, LineDao lineDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
        this.stationDao = stationDao;
    }

    @Transactional
    public void createSection(SectionRequest sectionRequest) {
        Long lineId = sectionRequest.getLineId();

        // 1. 노선 검증
        LineDto foundLine = lineDao.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException("노선이 존재하지 않습니다."));

        // 2. DB로 부터 노선 만들기
        List<SectionDto> sectionDtos = sectionDao.findByLineId(lineId);
        LinkedList<Section> sections = sectionDtos.stream()
                .map(sectionDto -> new Section(
                                stationDao.findById(sectionDto.getLeftStationId()),
                                stationDao.findById(sectionDto.getRightStationId()),
                                sectionDto.getDistance()
                        )
                ).collect(Collectors.toCollection(LinkedList::new));

        Line line = new Line(lineId, foundLine.getName(), sections);

        // 3. 넣는 역 검증
        Station leftStation = stationDao.findByName(sectionRequest.getLeftStationName())
                .orElseThrow(() -> new IllegalArgumentException("일치하는 역이 없습니다."));

        Station rightStation = stationDao.findByName(sectionRequest.getRightStationName())
                .orElseThrow(() -> new IllegalArgumentException("일치하는 역이 없습니다."));

        // 4. 라인에 아무것도 없으면 그냥 2개 넣기
        if (line.getSections().size() == 0) {
            SectionDto sectionDto = new SectionDto(lineId, leftStation.getId(), rightStation.getId(),
                    sectionRequest.getDistance());
            sectionDao.insert(sectionDto);
            return;
        }

        // 5. 왼쪽 역, 오른 쪽 역 검증하기
        boolean leftFlag = line.hasStation(leftStation);
        boolean rightFlag = line.hasStation(rightStation);

        if (leftFlag == true && rightFlag == true) {
            throw new IllegalArgumentException("잘못된 입력입니다.");
        }

        if (leftFlag == false && rightFlag == false) {
            throw new IllegalArgumentException("잘못된 입력입니다.");
        }

        if (leftFlag == true) {
            // 가장 오른쪽에 넣음
            if (!line.hasLeftStationInSection(leftStation)) {
                sectionDao.insert(new SectionDto(lineId, leftStation.getId(), rightStation.getId(),
                        sectionRequest.getDistance()));
                return;
            }

            Section section = line.findSectionByLeftStation(leftStation);
            Station originLeft = section.getLeft();
            Station originRight = section.getRight();
            int originDistance = section.getDistance();

            if (sectionRequest.getDistance() >= originDistance) {
                throw new IllegalArgumentException("기존 거리보다 멀 수 없습니다.");
            }

            sectionDao.deleteByStationId(originLeft.getId(), originRight.getId());
            sectionDao.insert(new SectionDto(lineId, leftStation.getId(), rightStation.getId(),
                    sectionRequest.getDistance()));
            sectionDao.insert(new SectionDto(lineId, rightStation.getId(), originRight.getId(),
                    originDistance - sectionRequest.getDistance()));
        }

        if (rightFlag == true) {
            if (!line.hasRightStationInSection(rightStation)) {
                sectionDao.insert(new SectionDto(lineId, leftStation.getId(), rightStation.getId(),
                        sectionRequest.getDistance()));
                return;
            }

            Section section = line.findSectionByRightStation(rightStation);
            Station originLeft = section.getLeft();
            Station originRight = section.getRight();
            int originDistance = section.getDistance();

            if (sectionRequest.getDistance() >= originDistance) {
                throw new IllegalArgumentException("기존 거리보다 멀 수 없습니다.");
            }

            sectionDao.deleteByStationId(originLeft.getId(), originRight.getId());
            sectionDao.insert(new SectionDto(lineId, originLeft.getId(), leftStation.getId(),
                    originDistance - sectionRequest.getDistance()));
            sectionDao.insert(new SectionDto(lineId, leftStation.getId(), rightStation.getId(),
                    sectionRequest.getDistance()));
        }
    }
}
