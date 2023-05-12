package subway.service;

import java.util.Optional;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import subway.dao.SectionDao;
import subway.entity.SectionEntity;

@Service
@AllArgsConstructor
public class SectionDeleteService {

    private SectionDao sectionDao;

    public void deleteSection(final Long lineId, final Long stationId) {
        final Optional<SectionEntity> upSection = sectionDao.findNeighborUpSection(lineId, stationId);
        final Optional<SectionEntity> downSection = sectionDao.findNeighborDownSection(lineId, stationId);

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
