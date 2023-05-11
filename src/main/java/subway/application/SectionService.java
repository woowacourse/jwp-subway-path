package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.section.Distance;
import subway.domain.section.Section;
import subway.dto.SectionRequest;
import subway.entity.SectionEntity;

import java.util.List;
import java.util.NoSuchElementException;

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

    public Long save(final SectionRequest sectionRequest) {
        // 예외: sectionRequest에 들어온 호선이 존재하지 않는 경우
        if (lineDao.findById(sectionRequest.getLineId()).isEmpty()) {
            throw new NoSuchElementException("해당하는 호선이 존재하지 않습니다.");
        }

        // 예외: sectionRequest에 들어온 2개의 역이 모두 Station에 존재하는 역인지 검증
        if (stationDao.findById(sectionRequest.getUpStationId()).isEmpty() || stationDao.findById(sectionRequest.getStationId()).isEmpty()) {
            throw new NoSuchElementException("해당하는 역이 존재하지 않습니다.");
        }

        // 예외: sectionRequest에 들어온 2개의 역이 구간이 있는지 검증
        if (sectionDao.findByStationIds(sectionRequest.getUpStationId(), sectionRequest.getStationId()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 구간입니다.");
        }

        // 1. 노선에 대한 구간이 아예 없는 경우
        List<SectionEntity> sectionEntities = sectionDao.findByLineId(sectionRequest.getLineId());
        if (sectionEntities.isEmpty()) {
            sectionDao.insert(new Section(sectionRequest.getLineId(), null, sectionRequest.getUpStationId(), new Distance(sectionRequest.getDistance())));
            sectionDao.insert(new Section(sectionRequest.getLineId(), sectionRequest.getUpStationId(), sectionRequest.getStationId(), new Distance(sectionRequest.getDistance())));
            sectionDao.insert(new Section(sectionRequest.getLineId(), sectionRequest.getStationId(), null, new Distance(sectionRequest.getDistance())));
        }

        List<SectionEntity> upSections = sectionDao.findByLineIdAndStationId(sectionRequest.getLineId(), sectionRequest.getUpStationId());
        List<SectionEntity> downSections = sectionDao.findByLineIdAndStationId(sectionRequest.getLineId(), sectionRequest.getStationId());

        // 예외: 기준이 되는 역이 노선에 존재하지 않는 경우 예외 처리
        if (upSections.isEmpty() && downSections.isEmpty()) {
            throw new IllegalArgumentException("노선에 기준이 되는 역이 존재하지 않습니다.");
        }

        // 2. 종점에 넣는 경우
        // 하행 종점 추가
        if (downSections.isEmpty()) {
            upSections.stream()
                    .filter(it -> it.getDownStationId() == null)
                    .findAny()
                    .ifPresent(section -> {
                        sectionDao.deleteBySectionId(section.getId());
                        sectionDao.insert(new Section(sectionRequest.getLineId(), sectionRequest.getUpStationId(), sectionRequest.getStationId(), new Distance(sectionRequest.getDistance())));
                        sectionDao.insert(new Section(sectionRequest.getLineId(), sectionRequest.getStationId(), null, new Distance(0)));
                    });
        }
        // 상행 종점 추가
        if (upSections.isEmpty()) {
            downSections.stream()
                    .filter(it -> it.getUpStationId() == null)
                    .findAny()
                    .ifPresent(section -> {
                        sectionDao.deleteBySectionId(section.getId());
                        sectionDao.insert(new Section(sectionRequest.getLineId(), sectionRequest.getUpStationId(), sectionRequest.getStationId(), new Distance(sectionRequest.getDistance())));
                        sectionDao.insert(new Section(sectionRequest.getLineId(), null, sectionRequest.getUpStationId(), new Distance(0)));
                    });
        }

        // 3. 중간에 넣는 경우
        Long upStationId = sectionRequest.getUpStationId();
        Long downStationId = sectionRequest.getStationId();

        if (downSections.isEmpty()) {
            upSections.stream()
                    .filter(it -> it.getUpStationId().equals(upStationId))
                    .findAny()
                    .ifPresent(section -> {
                        sectionDao.deleteBySectionId(section.getId());
                        sectionDao.insert(new Section(sectionRequest.getLineId(), upStationId, sectionRequest.getStationId(), new Distance(sectionRequest.getDistance())));
                        sectionDao.insert(new Section(sectionRequest.getLineId(), sectionRequest.getStationId(), section.getDownStationId(),  new Distance(sectionRequest.getDistance())));
                    });
        }

        if (upSections.isEmpty()) {
            downSections.stream()
                    .filter(it -> it.getDownStationId().equals(downStationId))
                    .findAny()
                    .ifPresent(section -> {
                        sectionDao.deleteBySectionId(section.getId());
                        sectionDao.insert(new Section(sectionRequest.getLineId(), sectionRequest.getUpStationId(), downStationId, new Distance(sectionRequest.getDistance())));
                        sectionDao.insert(new Section(sectionRequest.getLineId(), section.getUpStationId(), sectionRequest.getUpStationId(),  new Distance(sectionRequest.getDistance())));
                    });
        }
        return 1L;
    }

    public void deleteByLineIdAndStationId(final Long lineId, final Long stationId) {
        List<SectionEntity> sectionEntities = sectionDao.findByLineIdAndStationId(lineId, stationId);
        if (sectionEntities.isEmpty()) {
            throw new NoSuchElementException("해당하는 값이 없습니다.");
        }

        sectionDao.deleteByLineIdAndStationId(lineId, stationId);
        if (sectionEntities.size() == 1) {
            linkIfFinalStation(lineId, stationId, sectionEntities.get(0));
            return;
        }
        link(lineId, stationId, sectionEntities);
    }

    private void linkIfFinalStation(final Long lineId, final Long stationId, final SectionEntity sectionEntity) {
        if (sectionDao.findByLineId(lineId).isEmpty()) {
            return;
        }
        if (sectionEntity.getDownStationId().equals(stationId)) {
            Long upStationId = sectionEntity.getUpStationId();
            sectionDao.insert(new Section(lineId, upStationId, null, new Distance(0)));
            return;
        }
        Long downStationId = sectionEntity.getDownStationId();
        sectionDao.insert(new Section(lineId, null, downStationId, new Distance(0)));
    }

    private void link(final Long lineId, final Long stationId, final List<SectionEntity> sectionEntities) {
        SectionEntity previousSection = sectionEntities.get(0);
        SectionEntity nextSection = sectionEntities.get(1);
        int newDistance = previousSection.getDistance() + nextSection.getDistance();

        if (previousSection.getUpStationId().equals(stationId)) {
            Long upStationId = nextSection.getUpStationId();
            Long downStationId = previousSection.getDownStationId();
            sectionDao.insert(new Section(lineId, upStationId, downStationId, new Distance(newDistance)));
            return;
        }
        Long upStationId = previousSection.getUpStationId();
        Long downStationId = nextSection.getDownStationId();
        sectionDao.insert(new Section(lineId, upStationId, downStationId, new Distance(newDistance)));
    }
}
