package dev.chearcode.servlet;

import dev.chearcode.config.ContextAttribute;
import dev.chearcode.repository.MatchRepository;
import dev.chearcode.repository.PlayerRepository;
import dev.chearcode.config.TestDataInitializer;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/init-data")
public class DataInitializationServlet extends HttpServlet {
    private PlayerRepository playerRepository;
    private MatchRepository matchRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        playerRepository = (PlayerRepository) getServletContext().getAttribute(ContextAttribute.PLAYER_REPOSITORY);
        matchRepository = (MatchRepository) getServletContext().getAttribute(ContextAttribute.MATCH_REPOSITORY);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/jsp/init-data.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int matchCount = Integer.parseInt(req.getParameter("matchCount"));
        String nameSetParam = req.getParameter("nameSet");
        TestDataInitializer.NameSet nameSet = TestDataInitializer.NameSet.valueOf(nameSetParam);

        TestDataInitializer initializer = new TestDataInitializer(playerRepository, matchRepository);
        initializer.initialize(matchCount, nameSet);

        resp.sendRedirect(req.getContextPath() + "/matches");
    }
}
