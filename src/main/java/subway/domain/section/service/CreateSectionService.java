package subway.domain.section.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.section.dao.SectionDao;
import subway.domain.section.domain.Direction;
import subway.domain.section.dto.SectionCreateRequest;
import subway.domain.section.entity.SectionEntity;

import java.util.List;
import java.util.Optional;

@Service
public class CreateSectionService {

    private final SectionDao sectionDao;

    public CreateSectionService(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
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
            sectionDao.updateStationInSection(upSectionEntity);

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

        sectionDao.updateStationInSection(upSectionEntity);

        final SectionEntity downSectionEntity = new SectionEntity.Builder()
                .setLineId(request.getLineId())
                .setUpStationId(request.getAddedId())
                .setDownStationId(existSectionEntity.getDownStationId())
                .setDistance(existSectionEntity.getDistance() - request.getDistance())
                .build();

        final SectionEntity downSavedSectionEntity = sectionDao.insert(downSectionEntity);
        return List.of(upSectionEntity, downSavedSectionEntity);
    }
}
