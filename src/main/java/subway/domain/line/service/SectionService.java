package subway.domain.line.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.line.dao.SectionDao;
import subway.domain.line.domain.Direction;
import subway.domain.line.dto.SectionCreateRequest;
import subway.domain.line.entity.SectionEntity;

import java.util.List;
import java.util.Optional;

@Service
public class SectionService {

    private final SectionDao sectionDao;

    public SectionService(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    public List<SectionEntity> findByLineId(Long lindId){
        return sectionDao.findByLineId(lindId);
    }

    public List<SectionEntity> findAll(){
        return sectionDao.findAll();
    }

    @Transactional
    public List<SectionEntity> createSection(SectionCreateRequest sectionCreateRequest) {
        final Optional<SectionEntity> section = sectionDao.findNeighborSection(
                sectionCreateRequest.getLineId(),
                sectionCreateRequest.getBaseId(),
                Direction.from(sectionCreateRequest.getDirection())
        );

        if (section.isEmpty()) {
            return createSectionWhenNoNeighbor(sectionCreateRequest);
        }

        final SectionEntity existSectionEntity = section.get();

        if (existSectionEntity.getDistance() <= sectionCreateRequest.getDistance()) {
            throw new IllegalArgumentException("새롭게 등록하는 구간의 거리는 기존에 존재하는 구간의 거리보다 작아야합니다.");
        }

        return divideSectionByAddedStation(sectionCreateRequest, existSectionEntity);
    }

    private List<SectionEntity> createSectionWhenNoNeighbor(final SectionCreateRequest request) {
        if (Direction.from(request.getDirection()) == Direction.UP) {
            final SectionEntity newSectionEntity = new SectionEntity.Builder()
                    .setLineId(request.getLineId())
                    .setUpStationId(request.getAddedId())
                    .setDownStationId(request.getBaseId())
                    .setDistance(request.getDistance())
                    .build();

            final SectionEntity savedSectionEntity = sectionDao.insert(newSectionEntity);
            return List.of(savedSectionEntity);
        }

        final SectionEntity newSectionEntity = new SectionEntity.Builder()
                .setLineId(request.getLineId())
                .setUpStationId(request.getBaseId())
                .setDownStationId(request.getAddedId())
                .setDistance(request.getDistance())
                .build();

        final SectionEntity savedSectionEntity = sectionDao.insert(newSectionEntity);
        return List.of(savedSectionEntity);
    }

    private List<SectionEntity> divideSectionByAddedStation(final SectionCreateRequest request, final SectionEntity existSectionEntity) {
        if (Direction.from(request.getDirection()) == Direction.UP) {
            final SectionEntity upSectionEntity = new SectionEntity.Builder()
                    .setLineId(request.getLineId())
                    .setUpStationId(existSectionEntity.getUpStationId())
                    .setDownStationId(request.getAddedId())
                    .setDistance(existSectionEntity.getDistance() - request.getDistance())
                    .build();
            sectionDao.update(upSectionEntity);

            final SectionEntity downSectionEntity = new SectionEntity.Builder()
                    .setLineId(request.getLineId())
                    .setUpStationId(request.getAddedId())
                    .setDownStationId(existSectionEntity.getDownStationId())
                    .setDistance(request.getDistance())
                    .build();

            final SectionEntity downSavedSectionEntity = sectionDao.insert(downSectionEntity);
            return List.of(upSectionEntity, downSavedSectionEntity);
        }

        final SectionEntity upSectionEntity = new SectionEntity.Builder()
                .setLineId(request.getLineId())
                .setUpStationId(existSectionEntity.getUpStationId())
                .setDownStationId(request.getAddedId())
                .setDistance(request.getDistance())
                .build();

        sectionDao.update(upSectionEntity);

        final SectionEntity downSectionEntity = new SectionEntity.Builder()
                .setLineId(request.getLineId())
                .setUpStationId(request.getAddedId())
                .setDownStationId(existSectionEntity.getDownStationId())
                .setDistance(existSectionEntity.getDistance() - request.getDistance())
                .build();

        final SectionEntity downSavedSectionEntity = sectionDao.insert(downSectionEntity);
        return List.of(upSectionEntity, downSavedSectionEntity);
    }

    @Transactional
    public void deleteSection(final Long lineId, final Long stationId) {
        final Optional<SectionEntity> upSection = sectionDao.findNeighborSection(lineId, stationId, Direction.UP);
        final Optional<SectionEntity> downSection = sectionDao.findNeighborSection(lineId, stationId, Direction.DOWN);

        if (upSection.isEmpty() && downSection.isEmpty()) {
            throw new IllegalArgumentException("등록되어있지 않은 역은 지울 수 없습니다.");
        }

        if (upSection.isEmpty()) {
            sectionDao.deleteById(downSection.get().getId());
            return;
        }

        if (downSection.isEmpty()) {
            sectionDao.deleteById(upSection.get().getId());
            return;
        }

        final SectionEntity existUpSectionEntity = upSection.get();
        final SectionEntity existDownSectionEntity = downSection.get();

        sectionDao.deleteById(existUpSectionEntity.getId());
        sectionDao.deleteById(existDownSectionEntity.getId());

        final SectionEntity sectionEntity = new SectionEntity(
                lineId,
                existUpSectionEntity.getUpStationId(),
                existDownSectionEntity.getDownStationId(),
                existUpSectionEntity.getDistance() + existDownSectionEntity.getDistance()
        );

        sectionDao.insert(sectionEntity);
    }
}
