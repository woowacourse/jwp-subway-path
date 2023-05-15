package subway.application;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dao.entity.LineEntity;
import subway.dao.entity.SectionEntity;
import subway.dao.entity.StationEntity;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.section.SectionCreateRequest;
import subway.dto.section.SectionDeleteRequest;
import subway.dto.section.SectionResponse;
import subway.exception.IllegalDistanceException;
import subway.exception.IllegalSectionException;
import subway.exception.LineNotFoundException;
import subway.exception.StationNotFoundException;

@Service
@Transactional
public class SectionService {
    private final SectionDao sectionDao;
    private final StationDao stationDao;
    private final LineDao lineDao;

    public SectionService(SectionDao sectionDao, StationDao stationDao, LineDao lineDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    public void saveSection(Long lineId, SectionCreateRequest sectionCreateRequest) {
        LineEntity line = findLineById(lineId);
        StationEntity startStation = findStationByName(sectionCreateRequest.getStartStationName());
        StationEntity endStation = findStationByName(sectionCreateRequest.getEndStationName());
        addSection(new SectionEntity(line.getId(), startStation.getId(), endStation.getId(),
                sectionCreateRequest.getDistance())); // TODO: 2023-05-15 Section 도메인으로 처리하는 방향 생각
        // 그러면 findStationByName에서 반환하는걸 Station 도메인으로 변경
        // 그리고 addSection(Section 객체 전달) -> 그런데 domain 은 내부에 id를 가지지 않으니까 도메인을 전달해서는 DB에 접근하는 로직을 수행할 수 없음
    }

    private LineEntity findLineById(Long id) {
        Optional<LineEntity> lineEntity = lineDao.findById(id);
        if (lineEntity.isPresent()) {
            return lineEntity.get();
        }
        throw new LineNotFoundException();
    }

    private StationEntity findStationByName(String name) {
        if (stationDao.existsByName(name)) {
            return stationDao.findByName(name); // TODO: 2023-05-15 if 분기 없애고 하나로 합치기
        }
        throw new StationNotFoundException();
    }

    private void addSection(SectionEntity section) {
        if (sectionDao.isEmptyByLineId(section.getLineId())) { // 조회해 온 Line 객체의 내부 값이 비어있다면? insert 바로 하는 걸로 변경할 수 있을 듯
            sectionDao.insert(section);
            return;
        }
        boolean hasStartStation = sectionDao.isStationInLineById(section.getLineId(), section.getStartStationId());
        boolean hasEndStation = sectionDao.isStationInLineById(section.getLineId(), section.getEndStationId()); // 얘도 Line 한테 물어볼 수 있음
        validateHasStation(hasStartStation, hasEndStation);
        if (hasStartStation) {
            addEndSection(section);
        }
        if (hasEndStation) {
            addStartSection(section);
        }
    }

    private void validateHasStation(boolean hasStartStation, boolean hasEndStation) {
        if (hasStartStation && hasEndStation) {
            throw new IllegalSectionException("이미 노선에 추가할 역이 존재합니다.");
        }
        if (!hasStartStation && !hasEndStation) {
            throw new IllegalSectionException("노선에 기준이 되는 역을 찾을 수 없습니다.");
        }
    }

    private void addEndSection(SectionEntity sectionToAdd) { // 하행역을 새롭게 추가한다. <B, C(new)>
        Long lineId = sectionToAdd.getLineId();
        sectionDao.findByStartStationIdAndLineId(sectionToAdd.getStartStationId(), lineId)
            .ifPresentOrElse(prevSection -> { // <B, D> 가 기존에 있음 -> <B, C> 로 업데이트 + <C, D> 새로 추가 => 얘도 Line 한테 물어볼 수 있을 듯? 그러면 startStationId 필요 없어짐
                // 그니까 Line 한테 Station 던져주면서 이 Station 을 상행역으로 가지는 Section 을 알려줘~ 하는거임
                int distance = prevSection.getDistance();
                int newDistance = sectionToAdd.getDistance();
                validateDistance(distance, newDistance);
                Long newSectionEndStationId = prevSection.getEndStationId(); // D -> C 로 업데이트 할거니까 D는 임시값으로 빼두기 **이거 때문에 id 필요할 것 같은데?**
                // 아니면 이것도 prevEndStation 을 따로 변수로 저장해둠 (Station 객체 그 자체)
                prevSection.updateEndStationId(sectionToAdd.getEndStationId()); // 이 업데이트도 Section 객체를 업데이트 -> 그리고 이걸 저장할 때만 entity 로 바꾼다? 이게 맞나?
                // 근데 또 entity로 바꿀거면 id 라는 식별자를 통해서 바꿔주어야 함 -> 그러면 도메인이 id를 가질 수 밖에 없는데?? 그래도 도메인이 id를 가지는게 맞을까...
                // 사실은 도메인의 행위는 id와 상관이 없기 때문에 일부러 id를 가진 entity와 id가 없이 행위만 하는 domain을 분리한건데, 변경된 도메인을 엔티티로 변환하기 위해서는 도메인이 id를 가지는
                // 수 밖에.. 없을 것 같은데 흐으으음
                prevSection.updateDistance(newDistance); // 기존의 D -> C 로 업데이트 (이것도 Section 객체한테 update 명령 보내기)
                sectionDao.update(prevSection);
                sectionDao.insert(new SectionEntity(lineId, sectionToAdd.getEndStationId(), newSectionEndStationId, distance - newDistance)); // insert는 entity로 변환해서 수행
            }, () -> sectionDao.insert(sectionToAdd)); // 기존에 B로 시작하는 역이 없다면? B가 하행종점이었음 -> <B, C> 추가하면 끝
    }

    private void validateDistance(int distance, int newDistance) {
        if (distance <= newDistance) {
            throw new IllegalDistanceException("새로운 구간의 길이는 기존 구간의 길이보다 작아야 합니다.");
        }
    }

    private void addStartSection(SectionEntity sectionToAdd) {
        Long lineId = sectionToAdd.getLineId();
        sectionDao.findByEndStationIdAndLineId(sectionToAdd.getEndStationId(), lineId)
                .ifPresentOrElse(prevSection -> {
                    int distance = prevSection.getDistance();
                    int newDistance = sectionToAdd.getDistance();
                    validateDistance(distance, newDistance);
                    Long newSectionStartStationId = prevSection.getStartStationId();
                    prevSection.updateStartStationId(sectionToAdd.getStartStationId());
                    prevSection.updateDistance(newDistance);
                    sectionDao.update(prevSection);
                    sectionDao.insert(new SectionEntity(lineId, newSectionStartStationId, sectionToAdd.getStartStationId(), distance - newDistance));
                }, () -> sectionDao.insert(sectionToAdd));
    }

    public void deleteSection(Long lineId, SectionDeleteRequest sectionDeleteRequest) {
        LineEntity line = findLineById(lineId);
        String stationName = sectionDeleteRequest.getStationName();
        StationEntity station = findStationByName(stationName);
        validateStationInLine(line.getId(), station);
        Optional<SectionEntity> startSection = sectionDao.findByEndStationIdAndLineId(station.getId(), line.getId());
        Optional<SectionEntity> endSection = sectionDao.findByStartStationIdAndLineId(station.getId(), line.getId());
        if (startSection.isPresent() && endSection.isPresent()) {
            mergeSection(startSection.get(), endSection.get());
            return;
        }
        endSection.ifPresent(sectionDao::deleteBy);
        startSection.ifPresent(sectionDao::deleteBy);
    }

    private void validateStationInLine(Long lineId, StationEntity station) {
        if (!sectionDao.isStationInLineById(lineId, station.getId())) {
            throw new StationNotFoundException();
        }
    }

    private void mergeSection(SectionEntity startSection, SectionEntity endSection) {
        sectionDao.deleteBy(startSection);
        endSection.updateDistance(startSection.getDistance() + endSection.getDistance());
        endSection.updateStartStationId(startSection.getStartStationId());
        sectionDao.update(endSection);
    }

    @Transactional(readOnly = true)
    public List<SectionResponse> findSectionsByLineId(Long lineId) {
        LineEntity line = findLineById(lineId);
        Sections sections = sectionDao.findAllByLineId(line.getId()).stream()
                .map(entity -> new Section(
                        Station.fromEntity(stationDao.findById(entity.getStartStationId())),
                        Station.fromEntity(stationDao.findById(entity.getEndStationId())),
                        entity.getDistance()))
                .collect(collectingAndThen(toList(), Sections::new));
        return sections.getSections().stream()
                .map(SectionResponse::from)
                .collect(toList());
    }
}
