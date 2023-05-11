package subway.application;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import subway.dao.SectionDao;
import subway.domain.LineInfo;
import subway.domain.Section;
import subway.domain.SectionSorter;

@Service
public class SectionService {

	private final SectionDao sectionDao;

	public SectionService(final SectionDao sectionDao) {
		this.sectionDao = sectionDao;
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

}
