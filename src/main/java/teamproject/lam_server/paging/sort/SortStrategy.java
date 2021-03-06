package teamproject.lam_server.paging.sort;

import org.springframework.data.domain.Sort;

public interface SortStrategy<T extends Enum<T>> {
    Sort.Order getSortOrder(T domainKey, SortOption order);
}
