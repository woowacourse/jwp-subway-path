package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.strategy.delete.SectionDeleter;
import subway.application.strategy.insert.BetweenStationInserter;
import subway.application.strategy.insert.InsertSection;
import subway.dao.LineDao;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.SectionDeleteRequest;
import subway.dto.SectionRequest;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

import java.util.Objects;

@Service
@Transactional
public class SectionService {

    private final LineDao lineDao;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;
    private final BetweenStationInserter betweenStationInserter;
    private final SectionDeleter sectionDeleter;

    public SectionService(LineDao lineDao, StationRepository stationRepository, SectionRepository sectionRepository, BetweenStationInserter betweenStationInserter, SectionDeleter sectionDeleter) {
        this.lineDao = lineDao;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
        this.betweenStationInserter = betweenStationInserter;
        this.sectionDeleter = sectionDeleter;
    }

    public Long insertSection(SectionRequest request) {
        validateInput(request);

        final Station upStation = findById(request.getUpStationId());
        final Station downStation = findById(request.getDownStationId());
        final Sections sections = sectionRepository.findAllByLineId(request.getLineId());

        checkCanInsert(upStation, downStation, sections);

        if (sections.isUpTerminal(downStation) || sections.inDownTerminal(upStation)) {
            return insertToTerminal(request);
        }

        final InsertSection insertSection = new InsertSection(upStation, downStation, request.getDistance());
        return betweenStationInserter.insert(sections, insertSection);
    }

    private void validateInput(SectionRequest request) {
        if (lineDao.findById(request.getLineId()).isEmpty()) {
            throw new IllegalArgumentException("존재 하지 않는 노선에는 구간을 추가 할 수 없습니다.");
        }

        if (Objects.equals(request.getUpStationId(), request.getDownStationId())) {
            throw new IllegalArgumentException("같은 역을 구간으로 등록할 수 없습니다.");
        }

        if (sectionRepository.exists(request.getDownStationId(), request.getUpStationId())
                || sectionRepository.exists(request.getUpStationId(), request.getDownStationId())) {
            throw new IllegalArgumentException("동일한 구간을 추가할 수 없습니다.");
        }
    }

    private Station findById(Long stationId) {
        return stationRepository.findById(stationId);
    }

    private static void checkCanInsert(Station upStation, Station downStation, Sections sortedSections) {
        if (sortedSections.canInsert(upStation, downStation)) {
            throw new IllegalArgumentException("역이 존재하지 않으면 추가할 수 없습니다.");
        }
    }

    private Long insertToTerminal(SectionRequest request) {
        final Section section = new Section(
                request.getDistance(),
                new Station(request.getUpStationId()),
                new Station(request.getDownStationId()),
                request.getLineId()
        );
        return sectionRepository.insert(section);
    }

    public void deleteStation(Long targetId, SectionDeleteRequest request) {
        final Station targetStation = stationRepository.findById(targetId);
        final Sections sections = sectionRepository.findAllByLineId(request.getLineId());

        sectionDeleter.delete(sections, targetStation);
    }
}
