package subway.domain.line;

import subway.domain.edge.DirectionStrategy;
import subway.domain.edge.DownDirectionStrategy;
import subway.domain.edge.UpDirectionStrategy;

public enum Direction {

    UP {
        @Override
        public DirectionStrategy getDirectionStrategy() {
            return new UpDirectionStrategy();
        }
    }, DOWN {
        @Override
        public DirectionStrategy getDirectionStrategy() {
            return new DownDirectionStrategy();
        }
    };

    public abstract DirectionStrategy getDirectionStrategy();
}
