package subway.application;

import java.util.List;

import org.springframework.stereotype.Service;

import subway.dao.SectionDao;
import subway.domain.Section;
import subway.domain.SectionSorter;

@Service
public class SectionService {

	private final SectionDao sectionDao;

	public SectionService(final SectionDao sectionDao) {
		this.sectionDao = sectionDao;
	}

	public List<Section> findSectionsById(Long id) {
		final List<Section> sections = sectionDao.findSectionsByLineId(id);
		final SectionSorter sectionSorter = SectionSorter.getInstance();

		return sectionSorter.sortSections(sections);
	}

}
