package dev.chearcode.servlet;

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
        String filter = req.getParameter("filter_by_player_name");
        String pageParam = req.getParameter("page");
        int page = 1;
        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException ignored) {
            }
        }

        int offset = (page - 1) * MATCHES_PER_PAGE;

        List<Match> matches;
        long totalMatches;
        if (filter != null && !filter.isBlank()) {
            matches = matchService.getAllByName(filter, MATCHES_PER_PAGE, offset);
            totalMatches = matchService.countAllByPlayer(filter);
        } else {
            matches = matchService.getAll(MATCHES_PER_PAGE, offset);
            totalMatches = matchService.countAll();
        }

        int totalPages = (int) Math.ceil((double) totalMatches / MATCHES_PER_PAGE);

        req.setAttribute("matches", matches);
        req.setAttribute("currentPage", page);
        req.setAttribute("filter", filter == null ? "" : filter.trim());
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("hasPrev", page > 1);
        req.setAttribute("hasNext", page < totalPages);

        RequestDispatcher requestDispatcher = req.getRequestDispatcher("matches.jsp");
        requestDispatcher.forward(req, resp);
    }
}
