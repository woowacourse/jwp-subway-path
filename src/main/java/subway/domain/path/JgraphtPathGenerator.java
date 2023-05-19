package subway.domain.path;

import org.springframework.stereotype.Component;

@Component
public class JgraphtPathGenerator implements PathGenerator {

    @Override
    public Path generate() {
        return new JgraphtPath();
    }
}
