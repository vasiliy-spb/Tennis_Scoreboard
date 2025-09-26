package dev.chearcode.servlet;

import dev.chearcode.config.ContextAttribute;
import dev.chearcode.dto.CreateMatchRequestDto;
import dev.chearcode.exception.ValidationException;
import dev.chearcode.service.MatchService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

@WebServlet("/new-match")
public class NewMatchServlet extends HttpServlet {
    private MatchService matchService;
    private Validator validator;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        matchService = (MatchService) config.getServletContext().getAttribute(ContextAttribute.MATCH_SERVICE);
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            this.validator = validatorFactory.getValidator();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/jsp/new-match.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String firstPlayerName = req.getParameter("firstPlayerName");
        String secondPlayerName = req.getParameter("secondPlayerName");

        setAttribute("firstPlayerName", firstPlayerName, req);
        setAttribute("secondPlayerName", secondPlayerName, req);

        CreateMatchRequestDto requestDto = new CreateMatchRequestDto(
                firstPlayerName,
                secondPlayerName
        );

        validate(requestDto);

        UUID matchId = matchService.add(requestDto);

        resp.sendRedirect(req.getContextPath() + "/match-score?uuid=" + matchId);
    }

    private void setAttribute(String attribute, String value, HttpServletRequest req) {
        req.setAttribute(attribute, value != null ? value : "");
    }

    private void validate(CreateMatchRequestDto requestDto) {
        Set<ConstraintViolation<CreateMatchRequestDto>> violations = validator.validate(requestDto);
        if (!violations.isEmpty()) {
            throw new ValidationException(violations);
        }
    }
}
