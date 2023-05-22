package subway.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.SectionDao;
import subway.domain.section.Section;

@Transactional
@Service
public class SectionDeleteService {

    private final SectionDao sectionDao;

    public SectionDeleteService(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    public void deleteSection(final Long lineId, final Long stationId) {
        final Optional<Section> upSection = sectionDao.findUpSection(lineId, stationId);
        final Optional<Section> downSection = sectionDao.findDownSection(lineId, stationId);

        validateRegistered(upSection, downSection);
        deleteSection(upSection, downSection);
        if (upSection.isPresent() && downSection.isPresent()) {
            insertNewSection(lineId, upSection.get(), downSection.get());
        }
    }

    private void validateRegistered(final Optional<Section> upSection, final Optional<Section> downSection) {
        if (upSection.isEmpty() && downSection.isEmpty()) {
            throw new IllegalArgumentException("등록되어있지 않은 역은 지울 수 없습니다.");
        }
    }

    private void deleteSection(final Optional<Section> upSection, final Optional<Section> downSection) {
        upSection.ifPresent(section -> sectionDao.deleteById(section.getId()));
        downSection.ifPresent(section -> sectionDao.deleteById(section.getId()));
    }

    private void insertNewSection(final Long lineId, final Section upSection, final Section downSection) {
        final Section section = Section.builder()
                .lineId(lineId)
                .upStation(upSection.getUpStation().getId())
                .downStation(downSection.getDownStation().getId())
                .distance(upSection.getDistance().getValue() + downSection.getDistance().getValue())
                .build();

        sectionDao.insert(section);
    }
}
