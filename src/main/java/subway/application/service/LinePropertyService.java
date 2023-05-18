package subway.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.domain.LineProperty;
import subway.application.repository.LinePropertyRepository;
import subway.application.service.dto.in.IdCommand;
import subway.application.service.dto.in.SaveLinePropertyCommand;
import subway.application.service.dto.in.UpdateLinePropertyCommand;
import subway.application.service.dto.out.LinePropertyResult;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LinePropertyService {

    private final LinePropertyRepository linePropertyRepository;

    public LinePropertyService(LinePropertyRepository linePropertyRepository) {
        this.linePropertyRepository = linePropertyRepository;
    }

    public LinePropertyResult saveLineProperty(SaveLinePropertyCommand command) {
        LineProperty lineProperty = linePropertyRepository.insert(command.toEntity());
        return new LinePropertyResult(lineProperty);
    }

    public List<LinePropertyResult> findLinePropertyResponses() {
        List<LineProperty> allLineProperties = linePropertyRepository.findAll();
        return allLineProperties.stream()
                .map(lineProperty -> new LinePropertyResult(lineProperty.getId(),
                        lineProperty.getName(), lineProperty.getColor()))
                .collect(Collectors.toList());
    }

    public LinePropertyResult findLinePropertyResponseById(IdCommand command) {
        LineProperty lineProperty = linePropertyRepository.findById(command.getId());
        return new LinePropertyResult(lineProperty);
    }

    public void updateLineProperty(UpdateLinePropertyCommand command) {
        linePropertyRepository.update(command.toEntity());
    }

    public void deleteLinePropertyById(IdCommand command) {
        linePropertyRepository.deleteById(command.getId());
    }
}
