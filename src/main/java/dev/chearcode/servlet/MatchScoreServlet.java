package dev.chearcode.servlet;

import dev.chearcode.config.ContextAttribute;
import dev.chearcode.entity.Match;
import dev.chearcode.entity.Player;
import dev.chearcode.exception.EntityNotFoundException;
import dev.chearcode.model.TennisMatch;
import dev.chearcode.repository.MatchRepository;
import dev.chearcode.service.OngoingMatchesService;
import dev.chearcode.validator.UuidValidator;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@WebServlet("/match-score")
public class MatchScoreServlet extends HttpServlet {
    private OngoingMatchesService ongoingMatchesService;
    private MatchRepository matchRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.ongoingMatchesService =
                (OngoingMatchesService) getServletContext().getAttribute(ContextAttribute.ONGOING_MATCH_SERVICE);
        this.matchRepository = (MatchRepository) getServletContext().getAttribute(ContextAttribute.MATCH_REPOSITORY);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UUID uuid = getUuidParam("uuid", req);

        TennisMatch tennisMatch = getOngoingMatch(uuid);

        putMatchAttributes(req, uuid, tennisMatch);

        refreshPage(req, resp);
    }

    private UUID getUuidParam(String uuid, HttpServletRequest req) {
        String matchIdString = req.getParameter(uuid);
        UuidValidator.validate(matchIdString);
        UUID matchId = UUID.fromString(matchIdString);
        return matchId;
    }

    private TennisMatch getOngoingMatch(UUID uuid) {
        TennisMatch tennisMatch = ongoingMatchesService.get(uuid);
        if (tennisMatch == null) {
            throw new EntityNotFoundException("Match not found");
        }
        return tennisMatch;
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

    private void refreshPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/jsp/match-score.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UUID matchId = getUuidParam("uuid", req);
        UUID playerId = getUuidParam("playerId", req);

        TennisMatch tennisMatch = getOngoingMatch(matchId);

        Player player = definePointWinner(tennisMatch, playerId);

        processWinningPoint(player, tennisMatch, matchId);

        putMatchAttributes(req, matchId, tennisMatch);

        refreshPage(req, resp);
    }

    private void processWinningPoint(Player player, TennisMatch tennisMatch, UUID matchId) {
        ongoingMatchesService.pointWonBy(matchId, player);

        if (tennisMatch.isFinished()) {
            saveMathResult(tennisMatch);
            ongoingMatchesService.remove(matchId);
        }
    }

    private Player definePointWinner(TennisMatch tennisMatch, UUID playerId) {
        Map<UUID, Player> players = Map.of(
                tennisMatch.getFirstPlayer().getId(), tennisMatch.getFirstPlayer(),
                tennisMatch.getSecondPlayer().getId(), tennisMatch.getSecondPlayer()
        );
        return Optional.ofNullable(players.get(playerId))
                .orElseThrow(() -> new EntityNotFoundException("Player not found in this match"));
    }

    private void saveMathResult(TennisMatch tennisMatch) {
        Match match = new Match();
        match.setFirstPlayer(tennisMatch.getFirstPlayer());
        match.setSecondPlayer(tennisMatch.getSecondPlayer());
        match.setWinner(tennisMatch.getWinner());
        matchRepository.save(match);
    }
}
