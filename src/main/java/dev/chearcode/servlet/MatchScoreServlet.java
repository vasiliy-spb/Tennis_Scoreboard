package dev.chearcode.servlet;

import dev.chearcode.config.ContextAttribute;
import dev.chearcode.entity.Match;
import dev.chearcode.entity.Player;
import dev.chearcode.model.TennisMatch;
import dev.chearcode.repository.MatchRepository;
import dev.chearcode.service.OngoingMatchesService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/match-score")
public class MatchScoreServlet extends HttpServlet {
    private OngoingMatchesService ongoingMatchesService;
    private MatchRepository matchRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.ongoingMatchesService = (OngoingMatchesService) getServletContext().getAttribute(ContextAttribute.ONGOING_MATCH_SERVICE);
        this.matchRepository = (MatchRepository) getServletContext().getAttribute(ContextAttribute.MATCH_REPOSITORY);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uuidString = req.getParameter("uuid");
        if (uuidString == null || uuidString.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing uuid parameter");
            return;
        }

        UUID uuid;
        try {
            uuid = UUID.fromString(uuidString);
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid uuid format");
            return;
        }

        TennisMatch tennisMatch = ongoingMatchesService.get(uuid);
        if (tennisMatch == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Match not found");
            return;
        }

        putMatchAttributes(req, uuid, tennisMatch);

        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/jsp/match-score.jsp");
        requestDispatcher.forward(req, resp);
    }

    private void putMatchAttributes(HttpServletRequest req, UUID uuid, TennisMatch match) {
        Player first = match.getFirstPlayer();
        Player second = match.getSecondPlayer();

        req.setAttribute("uuid", uuid);
        req.setAttribute("match", match);

        req.setAttribute("firstPlayer", first);
        req.setAttribute("secondPlayer", second);

        req.setAttribute("firstSets", match.getScoreValue(first));
        req.setAttribute("firstGames", match.getCurrentSetValue(first));
        req.setAttribute("firstPoints", match.getCurrentGameValue(first));

        req.setAttribute("secondSets", match.getScoreValue(second));
        req.setAttribute("secondGames", match.getCurrentSetValue(second));
        req.setAttribute("secondPoints", match.getCurrentGameValue(second));

        req.setAttribute("isFinished", match.isFinished());

        if (match.isFinished()) {
            req.setAttribute("winnerId", match.getWinner().getId());
            req.setAttribute("winnerName", match.getWinner().getName());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String matchIdString = req.getParameter("uuid");
        String playerIdString = req.getParameter("playerId");

        if (matchIdString == null || matchIdString.isBlank() || playerIdString == null || playerIdString.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
            return;
        }

        UUID matchId;
        UUID playerId;
        try {
            matchId = UUID.fromString(matchIdString);
            playerId = UUID.fromString(playerIdString);
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect uuid format");
            return;
        }

        TennisMatch tennisMatch = ongoingMatchesService.get(matchId);
        if (tennisMatch == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Match not found");
            return;
        }

        Player player;
        if (tennisMatch.getFirstPlayer().getId().equals(playerId)) {
            player = tennisMatch.getFirstPlayer();
        } else if (tennisMatch.getSecondPlayer().getId().equals(playerId)) {
            player = tennisMatch.getSecondPlayer();
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Player not found in this match");
            return;
        }

        ongoingMatchesService.pointWonBy(matchId, player);

        if (tennisMatch.isFinished()) {
            Match match = new Match();
            match.setFirstPlayer(tennisMatch.getFirstPlayer());
            match.setSecondPlayer(tennisMatch.getSecondPlayer());
            match.setWinner(tennisMatch.getWinner());
            matchRepository.save(match);
            ongoingMatchesService.remove(matchId);
        }

        putMatchAttributes(req, matchId, tennisMatch);

        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/jsp/match-score.jsp");
        requestDispatcher.forward(req, resp);
    }
}
