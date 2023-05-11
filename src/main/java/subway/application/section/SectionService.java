package subway.application.section;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import subway.dao.section.SectionDao;
import subway.dao.section.dto.SectionDto;
import subway.domain.Line;
import subway.domain.LineInfo;
import subway.domain.Section;
import subway.domain.SectionSorter;
import subway.ui.dto.AddStationRequest;
import subway.ui.dto.AddStationResponse;

@Service
public class SectionService {

	private final SectionDao sectionDao;

	public SectionService(final SectionDao sectionDao) {
		this.sectionDao = sectionDao;
	}

	public List<AddStationResponse> addStationByLineId(final Long lineId, AddStationRequest addStationRequest) {
		final Line line = Line.from(sectionDao.findSectionsByLineId(lineId));

		if (line.isEmpty() || isTerminalAdding(addStationRequest, line)) {
			return addSingleSection(lineId, addStationRequest);
		}

		final Line departureSections = line.filterDepartureSections(addStationRequest);
		final Line arrivalSections = line.filterArrivalSection(addStationRequest);

		validateConnectivity(departureSections, arrivalSections);
		Section section = findSection(addStationRequest, departureSections, arrivalSections);
		validateDistance(addStationRequest, section);

		return addDoubleSection(lineId, addStationRequest, section);
	}

	public Map<LineInfo, List<Section>> findSections() {
		final SectionSorter sectionSorter = SectionSorter.getInstance();

		return sectionDao.findSections().entrySet().stream()
			.collect(toMap(Map.Entry::getKey, entry -> sectionSorter.sortSections(entry.getValue())));
	}

	public List<Section> findSectionsById(Long id) {
		final List<Section> sections = sectionDao.findSectionsByLineId(id);
		final SectionSorter sectionSorter = SectionSorter.getInstance();

		return sectionSorter.sortSections(sections);
	}

	public void deleteSectionByLineIdAndSectionId(Long lineId, Long sectionId) {
		final Line line = getLineFromSection(lineId, sectionId);

		deleteAndHandleTerminals(lineId, line);
	}

	private boolean isTerminalAdding(final AddStationRequest addStationRequest, final Line line) {

		return line.isUpLineTerminal(addStationRequest.getDepartureStation())
			|| line.isDownLineTerminal(addStationRequest.getArrivalStation());
	}

	private List<AddStationResponse> addSingleSection(final Long lineId, final AddStationRequest addStationRequest) {
		final SectionDto sectionDto = sectionDao.saveSection(lineId, addStationRequest.getDistance(),
			addStationRequest.getDepartureStation(), addStationRequest.getArrivalStation());

		return List.of(new AddStationResponse(sectionDto));
	}

	private void validateConnectivity(Line departureSections, Line arrivalSections) {
		if (departureSections.isEmpty() && arrivalSections.isEmpty()) {
			throw new IllegalArgumentException("노선에 연결될 수 없는 구간입니다.");
		}

		if (departureSections.isEnd() && arrivalSections.isEnd()) {
			throw new IllegalArgumentException("순환 노선입니다");
		}

		if (departureSections.size() + arrivalSections.size() >= 3) {
			throw new IllegalArgumentException("이미 존재하는 구간입니다.");
		}
	}

	private Section findSection(AddStationRequest addStationRequest, Line departureSections, Line arrivalSections) {
		Optional<Section> optionalDeparture = departureSections.findDeparture(addStationRequest.getDepartureStation());
		Optional<Section> optionalArrival = arrivalSections.findArrival(addStationRequest.getArrivalStation());

		return optionalDeparture.orElseGet(optionalArrival::get);
	}

	private void validateDistance(final AddStationRequest addStationRequest, final Section section) {
		if (section.isShorter(addStationRequest.getDistance())) {
			throw new IllegalArgumentException("기존의 거리보다 작아야 합니다.");
		}
	}

	private List<AddStationResponse> addDoubleSection(final Long lineId,
		final AddStationRequest addStationRequest, final Section section) {
		final String connectedStation = findConnectedStation(addStationRequest, section);

		SectionDto upLineSection = sectionDao.saveSection(lineId, addStationRequest.getDistance(),
			addStationRequest.getDepartureStation(), addStationRequest.getArrivalStation());
		SectionDto downLineSection = sectionDao.saveSection(lineId,
			section.subtractDistance(addStationRequest.getDistance()), addStationRequest.getArrivalStation(),
			section.findArrival());

		if (connectedStation.equals(addStationRequest.getArrivalStation())) {
			upLineSection = sectionDao.saveSection(lineId, section.getDistance(),
				section.findDeparture(), addStationRequest.getDepartureStation());
			downLineSection = sectionDao.saveSection(lineId, addStationRequest.getDistance(),
				addStationRequest.getDepartureStation(), addStationRequest.getArrivalStation());

		}

		sectionDao.deleteSection(section.getId());

		return List.of(new AddStationResponse(upLineSection), new AddStationResponse(downLineSection));
	}

	private String findConnectedStation(final AddStationRequest addStationRequest, final Section section) {
		if (section.isSameArrival(addStationRequest.getArrivalStation())) {
			return section.findArrival();
		}

		return section.findDeparture();
	}

	private Line getLineFromSection(Long lineId, Long sectionId) {
		final Line line = Line.from(sectionDao.findSectionByLineIdAndStationId(lineId, sectionId));

		if (line.isEmpty()) {
			throw new IllegalArgumentException("해당하는 역이 없습니다.");
		}

		return line;
	}

	private void deleteAndHandleTerminals(Long lineId, Line line) {
		final Section upLineTerminal = line.findUpLineTerminal();
		sectionDao.deleteSection(upLineTerminal.getId());

		if (line.isMiddle()) {
			final Section downLineTerminal = line.findDownLineTerminal();
			sectionDao.deleteSection(downLineTerminal.getId());

			sectionDao.saveSection(lineId, line.getTotalDistance(), upLineTerminal.findDeparture(),
				downLineTerminal.findArrival());
		}
	}
}
