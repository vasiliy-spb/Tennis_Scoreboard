package dev.chearcode.util;

import java.util.function.Supplier;

public final class PaginationHelper {
    private PaginationHelper() {
    }

    public static Pagination calculatePagination(String pageParam, Supplier<Long> totalItemsCounter, int itemPerPage) {
        int currentPage = getPage(pageParam);
        long totalItems = totalItemsCounter.get();
        int totalPages = calculateTotalPages(totalItems, itemPerPage);
        int offset = (currentPage - 1) * itemPerPage;

        return new Pagination(currentPage, totalPages, itemPerPage, offset);
    }

    private static int getPage(String pageParam) {
        int page = 1;
        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
                page = Math.max(1, page);
            } catch (NumberFormatException ignored) {
            }
        }
        return page;
    }

    private static int calculateTotalPages(long totalItems, int itemPerPage) {
        if (totalItems == 0) {
            return 1;
        }
        return (int) Math.ceil((double) totalItems / itemPerPage);
    }

    public record Pagination(
            int currentPage,
            int totalPages,
            int limit,
            int offset
    ) {
        public boolean hasNext() {
            return currentPage < totalPages;
        }

        public boolean hasPrevious() {
            return currentPage > 1;
        }
    }
}
