package subway.application.core.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import subway.application.core.domain.LineProperty;
import subway.application.core.service.dto.in.IdCommand;
import subway.application.core.service.dto.in.SaveLinePropertyCommand;
import subway.application.core.service.dto.in.UpdateLinePropertyCommand;
import subway.application.core.service.dto.out.LinePropertyResult;
import subway.application.port.LinePropertyRepository;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
@Transactional
public class LinePropertyService {

    private final LinePropertyRepository linePropertyRepository;

    public LinePropertyService(LinePropertyRepository linePropertyRepository) {
        this.linePropertyRepository = linePropertyRepository;
    }

    public LinePropertyResult saveLineProperty(@Valid SaveLinePropertyCommand command) {
        LineProperty lineProperty = linePropertyRepository.insert(command.toEntity());
        return new LinePropertyResult(lineProperty);
    }

    public List<LinePropertyResult> findAllLineProperty() {
        List<LineProperty> allLineProperties = linePropertyRepository.findAll();
        return allLineProperties.stream()
                .map(lineProperty -> new LinePropertyResult(lineProperty.getId(),
                        lineProperty.getName(), lineProperty.getColor()))
                .collect(Collectors.toList());
    }

    public LinePropertyResult findLinePropertyById(@Valid IdCommand command) {
        LineProperty lineProperty = linePropertyRepository.findById(command.getId());
        return new LinePropertyResult(lineProperty);
    }

    public void updateLineProperty(@Valid UpdateLinePropertyCommand command) {
        linePropertyRepository.update(command.toEntity());
    }

    public void deleteLinePropertyById(@Valid IdCommand command) {
        linePropertyRepository.deleteById(command.getId());
    }
}
