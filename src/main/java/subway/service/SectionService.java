package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.SectionDao;
import subway.dto.StationRegisterRequest;
import subway.entity.SectionEntity;

import java.util.List;
import java.util.Optional;

@Service
public class SectionService {

    private final SectionDao sectionDao;

    public SectionService(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    public List<SectionEntity> findByLineId(Long lindId) {
        return sectionDao.findByLineId(lindId);
    }

    public List<SectionEntity> findAll() {
        return sectionDao.findAll();
    }

    @Transactional
    public List<SectionEntity> createSection(Long lineId, StationRegisterRequest request) {
        SectionEntity section = new SectionEntity(lineId,
                request.getUpStationId(),
                request.getDownStationId(),
                request.getDistance()
        );

        if (sectionDao.isEmpty(lineId)) {
            SectionEntity insert = sectionDao.insert(section);
            return List.of(insert);
        }

        Optional<SectionEntity> updateSection = findUpdateSection(lineId, request);

        if (updateSection.isPresent()) {
            sectionDao.update(updateSection.get());
            SectionEntity insert = sectionDao.insert(section);
            return List.of(insert, updateSection.get());
        }

        if (updateSection.isEmpty() && isEndSection(lineId, request)) {
            SectionEntity insert = sectionDao.insert(section);
            return List.of(insert);
        }

        throw new IllegalArgumentException("해당 구간이 존재하지 않습니다.");
    }

    private Optional<SectionEntity> findUpdateSection(Long lineId, StationRegisterRequest request) {
        Optional<SectionEntity> upSectionOfUpStation = sectionDao.findByUpStationId(lineId, request.getUpStationId());
        Optional<SectionEntity> downSectionOfUpStation = sectionDao.findByDownStationId(lineId, request.getUpStationId());
        Optional<SectionEntity> upSectionOfDownStation = sectionDao.findByUpStationId(lineId, request.getDownStationId());
        Optional<SectionEntity> downSectionOfDownStation = sectionDao.findByDownStationId(lineId, request.getDownStationId());

        if (upSectionOfUpStation.isEmpty() && upSectionOfDownStation.isPresent() &&
                downSectionOfUpStation.isEmpty() && downSectionOfDownStation.isPresent()) {
            return Optional.ofNullable(downSectionOfDownStation.get().setDownStationId(request.getUpStationId())
                    .setDistance(downSectionOfDownStation.get().getDistance() - request.getDistance()));
        }

        if (upSectionOfUpStation.isPresent() && upSectionOfDownStation.isEmpty() &&
                downSectionOfUpStation.isPresent() && downSectionOfDownStation.isEmpty()) {
            return Optional.ofNullable(upSectionOfUpStation.get().setUpStationId(request.getDownStationId())
                    .setDistance(upSectionOfUpStation.get().getDistance() - request.getDistance()));
        }

        return Optional.empty();
    }

    private boolean isEndSection(Long lineId, StationRegisterRequest request) {
        Optional<SectionEntity> upSectionOfUpStation = sectionDao.findByUpStationId(lineId, request.getUpStationId());
        Optional<SectionEntity> downSectionOfDownStation = sectionDao.findByDownStationId(lineId, request.getDownStationId());
        return upSectionOfUpStation.isPresent() || downSectionOfDownStation.isPresent();
    }

    @Transactional
    public void deleteSection(final Long lineId, final Long stationId) {
        Optional<SectionEntity> upSection = sectionDao.findByUpStationId(lineId, stationId);
        Optional<SectionEntity> downSection = sectionDao.findByDownStationId(lineId, stationId);

        if (upSection.isEmpty() && downSection.isEmpty()) {
            throw new IllegalArgumentException("등록되어있지 않은 역은 지울 수 없습니다.");
        }

        if (upSection.isPresent() && downSection.isEmpty()) {
            sectionDao.deleteById(upSection.get().getId());
            return;
        }

        if (upSection.isEmpty() && downSection.isPresent()) {
            sectionDao.deleteById(downSection.get().getId());
            return;
        }

        sectionDao.update(upSection.get().mergeSection(downSection.get()));
        sectionDao.deleteById(upSection.get().getId());
    }
}
