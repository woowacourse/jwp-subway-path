package subway.application;

import static java.util.stream.Collectors.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import subway.dao.SectionDao;
import subway.domain.LineInfo;
import subway.domain.Section;
import subway.domain.SectionSorter;
import subway.domain.Station;
import subway.dto.AddStationRequest;
import subway.dto.AddStationResponse;

@Service
public class SectionService {

	private final SectionDao sectionDao;

	public SectionService(final SectionDao sectionDao) {
		this.sectionDao = sectionDao;
	}

	public List<AddStationResponse> addStationByLineId(final Long id, AddStationRequest addStationRequest) {
		final List<Section> allSections = sectionDao.findSectionsByLineId(id);
		final int sectionCount = allSections.size();

		// 해당 노선의 첫번째 역 추가인 경우 검증없이 추가
		if (isFirstSave(sectionCount)) {
			final long sectionId = sectionDao.saveSection(id, addStationRequest.getDistance(),
				addStationRequest.getDepartureStation(),
				addStationRequest.getArrivalStation());
			return List.of(new AddStationResponse(sectionId, addStationRequest.getDepartureStation(),
				addStationRequest.getArrivalStation(), addStationRequest.getDistance()));
		}
		final SectionSorter sectionSorter = SectionSorter.getInstance();
		final List<Section> sortedSections = sectionSorter.sortSections(allSections);

		List<Section> departureSections = allSections.stream()
				.filter(section -> hasSection(section,addStationRequest.getDepartureStation()))
				.collect(toList());

		List<Section> arrivalSections = allSections.stream()
			.filter(section -> hasSection(section, addStationRequest.getArrivalStation()))
			.collect(toList());

		// 검증 후 연결할 수 없을 시 예외 발생
		validate(departureSections, arrivalSections);

		// 종점 여부 확인
		if (isTerminalAdding(addStationRequest, sortedSections)) {
			final long sectionId = sectionDao.saveSection(id, addStationRequest.getDistance(),
				addStationRequest.getDepartureStation(),
				addStationRequest.getArrivalStation());
			return List.of(new AddStationResponse(sectionId, addStationRequest.getDepartureStation(),
				addStationRequest.getArrivalStation(), addStationRequest.getDistance()));
		}

		// 비교할 구간 선정
		Section section = findSection(addStationRequest, departureSections, arrivalSections);

		// 거리검증
		if (section.getDistance().getDistance() <= addStationRequest.getDistance()) {
			throw new IllegalArgumentException("기존의 거리보다 작아야 합니다.");
		}

		// 연결되는 역 찾기
		final String connectedStation = findConnectedStation(addStationRequest, section);

		// 두개의 섹션 저장
		if (connectedStation.equals(addStationRequest.getDepartureStation())) {
			final int upLineDistance = addStationRequest.getDistance();
			final String upLineDeparture = addStationRequest.getDepartureStation();
			final String upLineArrival = addStationRequest.getArrivalStation();
			final long upLineSectionId = sectionDao.saveSection(id, upLineDistance, upLineDeparture, upLineArrival);
			final int downLineDistance = section.getDistance().getDistance();
			final String downLineDeparture = addStationRequest.getArrivalStation();
			final String downLineArrival = section.getArrival().getName();
			final long downLineSectionId = sectionDao.saveSection(id, downLineDistance, downLineDeparture,
				downLineArrival);
			sectionDao.deleteSection(section.getId());

			return List.of(new AddStationResponse(upLineSectionId, upLineDeparture, upLineArrival, upLineDistance),
				new AddStationResponse(downLineSectionId, downLineDeparture, downLineArrival, downLineDistance));
		}
		final int upLineDistance = section.getDistance().getDistance();
		final String upLineDeparture = section.getDeparture().getName();
		final String upLineArrival = addStationRequest.getDepartureStation();
		final long upLineSectionId = sectionDao.saveSection(id, upLineDistance, upLineDeparture, upLineArrival);

		final int downLineDistance = addStationRequest.getDistance();
		final String downLineDeparture = addStationRequest.getDepartureStation();
		final String downLineArrival = addStationRequest.getArrivalStation();
		final long downLineSectionId = sectionDao.saveSection(id, downLineDistance, downLineDeparture,
			downLineArrival);
		sectionDao.deleteSection(section.getId());

		return List.of(new AddStationResponse(upLineSectionId, upLineDeparture, upLineArrival, upLineDistance),
			new AddStationResponse(downLineSectionId, downLineDeparture, downLineArrival, downLineDistance));

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
		final List<Section> sections = sectionDao.findSectionByLineIdAndStationId(lineId, sectionId);
		final SectionSorter sectionSorter = SectionSorter.getInstance();
		final List<Section> sortedSections = sectionSorter.sortSections(sections);
		if (sections.size() == 0) {
			throw new IllegalArgumentException("해당하는 역이 없습니다.");
		}
		sectionDao.deleteSection(sections.get(0).getId());
		if (sections.size() == 2) {
			sectionDao.deleteSection(sections.get(1).getId());
			sectionDao.saveSection(lineId,
					sections.get(0).getDistance().getDistance() + sections.get(1).getDistance().getDistance(),
					sortedSections.get(0).getDeparture().getName(), sortedSections.get(1).getArrival().getName());
		}
	}

	private boolean isFirstSave(final int sectionCount) {
		return sectionCount == 0;
	}

	private boolean hasSection(Section section, String requestStation) {
		return section.getDeparture().getName().equals(requestStation)||
				section.getArrival().getName().equals(requestStation);
	}

	private void validate(List<Section> departureSections, List<Section> arrivalSections) {
		int departureMatchStationCount = departureSections.size();
		int arrivalMatchStationCount = arrivalSections.size();

		if (departureMatchStationCount == 0 && arrivalMatchStationCount == 0) {
			throw new IllegalArgumentException("노선에 연결될 수 없는 역입니다.");
		}

		if (isAbnormalCase(departureMatchStationCount, arrivalMatchStationCount)) {
			throw new IllegalArgumentException("순환 노선입니다");
		}
	}

	private boolean isAbnormalCase(final long departureMatchCount, final long arrivalMatchCount) {
		return !List.of(departureMatchCount, arrivalMatchCount).contains(0l);
	}

	private boolean isTerminalAdding(final AddStationRequest addStationRequest,
		final List<Section> sortedSections) {
		final String upLineTerminal = sortedSections.get(0).getDeparture().getName();
		final String downLineTerminal = sortedSections.get(sortedSections.size() - 1).getArrival().getName();

		return addStationRequest.getDepartureStation().equals(downLineTerminal)
			|| addStationRequest.getArrivalStation().equals(upLineTerminal);
	}

	private Section findSection(AddStationRequest addStationRequest, List<Section> departureSections,
		List<Section> arrivalSections) {

		Optional<Section> optionalDeparture = arrivalSections.stream()
			.filter(section -> section.getArrival().getName().equals(addStationRequest.getArrivalStation()))
			.findFirst();

		Optional<Section> optionalArrival = departureSections.stream()
			.filter(section -> section.getDeparture().getName().equals(addStationRequest.getDepartureStation()))
			.findFirst();

		return optionalDeparture.orElseGet(optionalArrival::get);
	}

	private String findConnectedStation(final AddStationRequest addStationRequest, final Section section) {
		if (section.getArrival().getName().equals(addStationRequest.getArrivalStation())) {
			return section.getArrival().getName();
		}
		return section.getDeparture().getName();
	}
}
