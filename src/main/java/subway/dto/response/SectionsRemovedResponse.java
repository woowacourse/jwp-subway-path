package subway.dto.response;

import java.util.List;

public class SectionsRemovedResponse {

    private List<Long> removedSectionsIds;

    private SectionsRemovedResponse(final List<Long> removedSectionsIds) {
        this.removedSectionsIds = removedSectionsIds;
    }

    public static SectionsRemovedResponse value(final List<Long> removedSectionsIds) {
        return new SectionsRemovedResponse(removedSectionsIds);
    }

    public List<Long> getRemovedSectionsIds() {
        return removedSectionsIds;
    }
}
