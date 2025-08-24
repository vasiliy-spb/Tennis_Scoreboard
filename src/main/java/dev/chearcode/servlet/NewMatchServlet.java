package dev.chearcode.servlet;

import dev.chearcode.dto.CreateMatchRequestDto;
import dev.chearcode.service.MatchService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/new-match")
public class NewMatchServlet extends HttpServlet {
    private final MatchService matchService;
    private final Validator validator;

    public NewMatchServlet(MatchService matchService) {
        this.matchService = matchService;
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            this.validator = validatorFactory.getValidator();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CreateMatchRequestDto requestDto = new CreateMatchRequestDto(
                req.getParameter("firstPlayerName"),
                req.getParameter("secondPlayerName")
        );

        validator.validate(requestDto);

        UUID matchId = matchService.add(requestDto);

        resp.sendRedirect("/match-score?uuid=" + matchId);
    }
}
