package dev.chearcode.servlet;

import dev.chearcode.util.PaginationHelper;
import dev.chearcode.config.ContextAttribute;
import dev.chearcode.entity.Match;
import dev.chearcode.service.MatchService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/matches")
public class MatchesServlet extends HttpServlet {
    private static final int MATCHES_PER_PAGE = 20;
    private MatchService matchService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.matchService = (MatchService) getServletContext().getAttribute(ContextAttribute.MATCH_SERVICE);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String filter = clearInput(req.getParameter("filter_by_player_name"));
        String pageParam = req.getParameter("page");

        PaginationHelper.Pagination pagination =
                PaginationHelper.calculatePagination(
                        pageParam,
                        () -> matchService.getTotalMatches(filter),
                        MATCHES_PER_PAGE
                );

        List<Match> matches = matchService.getMatches(filter, pagination.limit(), pagination.offset());

        putAttributes(req, filter, matches, pagination);

        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/jsp/matches.jsp");
        requestDispatcher.forward(req, resp);
    }

    private String clearInput(String input) {
        return input == null ? "" : input.trim();
    }

    private void putAttributes(HttpServletRequest req,
                               String filter,
                               List<Match> matches,
                               PaginationHelper.Pagination pagination
    ) {
        req.setAttribute("filter", filter);
        req.setAttribute("matches", matches);
        req.setAttribute("currentPage", pagination.currentPage());
        req.setAttribute("totalPages", pagination.totalPages());
        req.setAttribute("hasPrev", pagination.hasPrevious());
        req.setAttribute("hasNext", pagination.hasNext());
    }
}
