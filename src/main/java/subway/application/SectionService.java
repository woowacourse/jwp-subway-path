package subway.application;

import org.springframework.stereotype.Service;
import subway.domain.Line;
import subway.domain.Station;
import subway.domain.strategy.AddSectionStrategy;
import subway.domain.strategy.DeleteSectionStrategy;
import subway.domain.strategy.DirectionStrategy;
import subway.dto.DeleteSectionRequest;
import subway.dto.SectionRequest;
import subway.dto.SectionResponse;
import subway.repository.LineRepository;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class SectionService {

    private final LineRepository lineRepository;

    public SectionService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public List<SectionResponse> getSections(final long lineId) {
        return lineRepository.getSections(lineId)
                .stream()
                .map(SectionResponse::of)
                .collect(toList());
    }

    public List<SectionResponse> saveSection(final SectionRequest sectionRequest) {
        Line findLine = lineRepository.findById(sectionRequest.getLineId());
        Station newStation = lineRepository.findStationById(sectionRequest.getNewStationId());
        Station baseStation = lineRepository.findStationById(sectionRequest.getBaseStationId());

        AddSectionStrategy addSectionStrategy = findLine.readyToSave(
                baseStation,
                newStation,
                DirectionStrategy.from(sectionRequest.getDirection()),
                sectionRequest.getDistance()
        );

        addSectionStrategy.execute(lineRepository);
        Line line = lineRepository.findById(findLine.getId());
        return toSectionResponse(line);
    }

    private List<SectionResponse> toSectionResponse(Line line) {
        return line.getSections()
                .stream()
                .map(section -> new SectionResponse(
                        section.getId(),
                        section.getLineId(),
                        section.getUpStation().getId(),
                        section.getDownStation().getId(),
                        section.getDistance())
                ).collect(Collectors.toUnmodifiableList());
    }

    public void deleteSection(final DeleteSectionRequest deleteSectionRequest) {
        Line findLine = lineRepository.findById(deleteSectionRequest.getLineId());
        Station deletStation = lineRepository.findStationById(deleteSectionRequest.getStationId());

        DeleteSectionStrategy deleteSectionStrategy = findLine.readyToDelete(deletStation);
        deleteSectionStrategy.execute(lineRepository);
    }
}