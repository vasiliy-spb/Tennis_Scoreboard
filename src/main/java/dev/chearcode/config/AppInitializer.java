package dev.chearcode.config;

import dev.chearcode.repository.MatchRepository;
import dev.chearcode.repository.MatchRepositoryImpl;
import dev.chearcode.repository.PlayerRepository;
import dev.chearcode.repository.PlayerRepositoryImpl;
import dev.chearcode.service.MatchService;
import dev.chearcode.service.OngoingMatchesService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppInitializer implements ServletContextListener {
    private static final int INIT_MATCH_COUNT = 214;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContextListener.super.contextInitialized(sce);

        PlayerRepository playerRepository = new PlayerRepositoryImpl();
        sce.getServletContext().setAttribute(ContextAttribute.PLAYER_REPOSITORY, playerRepository);

        MatchRepository matchRepository = new MatchRepositoryImpl();
        sce.getServletContext().setAttribute(ContextAttribute.MATCH_REPOSITORY, matchRepository);

        OngoingMatchesService ongoingMatchesService = new OngoingMatchesService();
        sce.getServletContext().setAttribute(ContextAttribute.ONGOING_MATCH_SERVICE, ongoingMatchesService);

        MatchService matchService = new MatchService(playerRepository, matchRepository, ongoingMatchesService);
        sce.getServletContext().setAttribute(ContextAttribute.MATCH_SERVICE, matchService);

        TestDataInitializer testDataInitializer = new TestDataInitializer(playerRepository, matchRepository);
        testDataInitializer.initialize(INIT_MATCH_COUNT, TestDataInitializer.NameSet.COMICS);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        HibernateManager.closeSessionFactory();
    }
}
