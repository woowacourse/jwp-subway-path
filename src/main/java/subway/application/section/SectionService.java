package subway.application.section;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.persistence.dao.SectionDao;

@Service
@Transactional
public class SectionService {

	private final SectionDao sectionDao;

	public SectionService(final SectionDao sectionDao) {
		this.sectionDao = sectionDao;
	}
}
