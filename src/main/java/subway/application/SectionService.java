package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.SectionRequest;
import subway.entity.SectionEntity;

import java.util.List;
import java.util.NoSuchElementException;
import subway.entity.StationEntity;

@Service
public class SectionService {

    private final SectionDao sectionDao;
    private final StationDao stationDao;
    private final LineDao lineDao;

    public SectionService(final SectionDao sectionDao, final StationDao stationDao, final LineDao lineDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    public void save(final SectionRequest sectionRequest) {
        // 예외: sectionRequest에 들어온 호선이 존재하지 않는 경우
        if (lineDao.findById(sectionRequest.getLineId()).isEmpty()) {
            throw new NoSuchElementException("해당하는 호선이 존재하지 않습니다.");
        }

        // 예외: sectionRequest에 들어온 2개의 역이 모두 Station에 존재하는 역인지 검증
        if (stationDao.findById(sectionRequest.getUpStationId()).isEmpty() || stationDao.findById(sectionRequest.getDownStationId()).isEmpty()) {
            throw new NoSuchElementException("해당하는 역이 존재하지 않습니다.");
        }

        // 예외: sectionRequest에 들어온 2개의 역이 구간이 있는지 검증
        if (sectionDao.findByStationIds(sectionRequest.getUpStationId(), sectionRequest.getDownStationId()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 구간입니다.");
        }

        // 1. 노선에 대한 구간이 아예 없는 경우 -> 그냥 추가
        List<SectionEntity> sectionEntities = sectionDao.findByLineId(sectionRequest.getLineId());
        if (sectionEntities.isEmpty()) {
            final Station upStation = stationDao.findById(sectionRequest.getUpStationId())
                    .orElseThrow(() -> new NoSuchElementException("해당하는 역이 존재하지 않습니다."))
                    .toDomain();
            final Station downStation = stationDao.findById(sectionRequest.getDownStationId())
                    .orElseThrow(() -> new NoSuchElementException("해당하는 역이 존재하지 않습니다."))
                    .toDomain();
            final Section section = new Section(upStation, downStation, sectionRequest.getDistance());
            sectionDao.insert(SectionEntity.toEntity(sectionRequest.getLineId(), section));
        }

        List<SectionEntity> upSections = sectionDao.findByLineIdAndStationId(sectionRequest.getLineId(), sectionRequest.getUpStationId());
        List<SectionEntity> downSections = sectionDao.findByLineIdAndStationId(sectionRequest.getLineId(), sectionRequest.getDownStationId());

        // 예외: 기준이 되는 역이 노선에 존재하지 않는 경우 예외 처리
        if (upSections.isEmpty() && downSections.isEmpty()) {
            throw new IllegalArgumentException("노선에 기준이 되는 역이 존재하지 않습니다.");
        }

        // 2. 종점에 넣는 경우 -> up의 section이 하나 => 그냥 추가
        // 하행 종점 추가
        if (upSections.size() == 1 && downSections.isEmpty()) {
            final Station upStation = stationDao.findById(sectionRequest.getUpStationId())
                    .orElseThrow(() -> new NoSuchElementException("해당하는 역이 존재하지 않습니다."))
                    .toDomain();
            final Station downStation = stationDao.findById(sectionRequest.getDownStationId())
                    .orElseThrow(() -> new NoSuchElementException("해당하는 역이 존재하지 않습니다."))
                    .toDomain();
            final Section section = new Section(upStation, downStation, sectionRequest.getDistance());
            sectionDao.insert(SectionEntity.toEntity(sectionRequest.getLineId(), section));
        }
        // 상행 종점 추가
        if (downSections.size() == 1 && upSections.isEmpty()) {
            final Station upStation = stationDao.findById(sectionRequest.getUpStationId())
                    .orElseThrow(() -> new NoSuchElementException("해당하는 역이 존재하지 않습니다."))
                    .toDomain();
            final Station downStation = stationDao.findById(sectionRequest.getDownStationId())
                    .orElseThrow(() -> new NoSuchElementException("해당하는 역이 존재하지 않습니다."))
                    .toDomain();
            final Section section = new Section(upStation, downStation, sectionRequest.getDistance());
            sectionDao.insert(SectionEntity.toEntity(sectionRequest.getLineId(), section));
        }

        // 3. 중간에 넣는 경우 -> up의 section이 둘 => up과 연결 + up이 up인 section의 down과 연결
        Long upStationId = sectionRequest.getUpStationId();
        Long downStationId = sectionRequest.getDownStationId();

        if (upSections.size() == 2 && downSections.isEmpty()) {
            final Station upStation = stationDao.findById(sectionRequest.getUpStationId())
                    .orElseThrow(() -> new NoSuchElementException("해당하는 역이 존재하지 않습니다."))
                    .toDomain();
            final Station newStation = stationDao.findById(sectionRequest.getDownStationId())
                    .orElseThrow(() -> new NoSuchElementException("해당하는 역이 존재하지 않습니다."))
                    .toDomain();
            final Section section1 = new Section(upStation, newStation, sectionRequest.getDistance());
            sectionDao.insert(SectionEntity.toEntity(sectionRequest.getLineId(), section1));

            final SectionEntity sectionEntity = upSections.stream()
                    .filter(it -> it.getUpStationId() == sectionRequest.getUpStationId())
                    .findFirst()
                    .orElseThrow();
            final Station downStation = stationDao.findById(sectionEntity.getDownStationId())
                    .orElseThrow(() -> new NoSuchElementException("해당하는 역이 존재하지 않습니다."))
                    .toDomain();
            final Section section2 = new Section(newStation, downStation, sectionEntity.getDistance());
            sectionDao.insert(SectionEntity.toEntity(sectionRequest.getLineId(), section2));
        }

        if (downSections.size() == 2 && upSections.isEmpty()) {
            final Station newStation = stationDao.findById(sectionRequest.getUpStationId())
                    .orElseThrow(() -> new NoSuchElementException("해당하는 역이 존재하지 않습니다."))
                    .toDomain();
            final Station downStation = stationDao.findById(sectionRequest.getDownStationId())
                    .orElseThrow(() -> new NoSuchElementException("해당하는 역이 존재하지 않습니다."))
                    .toDomain();
            final Section section1 = new Section(newStation, downStation, sectionRequest.getDistance());
            sectionDao.insert(SectionEntity.toEntity(sectionRequest.getLineId(), section1));

            final SectionEntity sectionEntity = downSections.stream()
                    .filter(it -> it.getDownStationId() == sectionRequest.getDownStationId())
                    .findFirst()
                    .orElseThrow();
            final Station upStation = stationDao.findById(sectionEntity.getUpStationId())
                    .orElseThrow(() -> new NoSuchElementException("해당하는 역이 존재하지 않습니다."))
                    .toDomain();
            final Section section2 = new Section(upStation, newStation, sectionEntity.getDistance());
            sectionDao.insert(SectionEntity.toEntity(sectionRequest.getLineId(), section2));
        }
    }

    // TODO
    public void deleteByLineIdAndStationId(final Long lineId, final Long stationId) {
        List<SectionEntity> sectionEntities = sectionDao.findByLineIdAndStationId(lineId, stationId);
        if (sectionEntities.isEmpty()) {
            throw new NoSuchElementException("해당하는 값이 없습니다.");
        }

        sectionDao.deleteByLineIdAndStationId(lineId, stationId);
        // TODO: 라인에 역이 2개만 있는 경우, 삭제 로직 수정 필요
        if (sectionEntities.size() == 1) {
            linkIfFinalStation(lineId, stationId, sectionEntities.get(0));
            return;
        }
        link(lineId, stationId, sectionEntities);
    }

    private void linkIfFinalStation(final Long lineId, final Long stationId, final SectionEntity section) {
        if (sectionDao.findByLineId(lineId).isEmpty()) {
            return;
        }
        if (section.getDownStationId().equals(stationId)) {
            Long upStationId = section.getUpStationId();
            sectionDao.insert(new SectionEntity(lineId, upStationId, null, 0));
            return;
        }
        Long downStationId = section.getDownStationId();
        sectionDao.insert(new SectionEntity(lineId, null, downStationId, 0));
    }

    private void link(final Long lineId, final Long stationId, final List<SectionEntity> sectionEntities) {
        SectionEntity previousSection = sectionEntities.get(0);
        SectionEntity nextSection = sectionEntities.get(1);
        int newDistance = previousSection.getDistance() + nextSection.getDistance();

        if (previousSection.getUpStationId().equals(stationId)) {
            Long upStationId = nextSection.getUpStationId();
            Long downStationId = previousSection.getDownStationId();
            sectionDao.insert(new SectionEntity(lineId, upStationId, downStationId, newDistance));
            return;
        }
        Long upStationId = previousSection.getUpStationId();
        Long downStationId = nextSection.getDownStationId();
        sectionDao.insert(new SectionEntity(lineId, upStationId, downStationId, newDistance));
    }
}
