package subway.domain.path;

import org.springframework.stereotype.Component;

@Component
public class JgraphtGraphGenerator implements GraphGenerator{
    @Override
    public Graph generate() {
        return new JgraphtGraph();
    }
}
