<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tennis Scoreboard | Match Score</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Roboto+Mono:wght@300&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
</head>
<body>
<header class="header">
    <section class="nav-header">
        <div class="brand">
            <div class="nav-toggle">
                <img src="${pageContext.request.contextPath}/images/menu.png" alt="Logo" class="logo">
            </div>
            <span class="logo-text">TennisScoreboard</span>
        </div>
        <div>
            <nav class="nav-links">
                <a class="nav-link" href="${pageContext.request.contextPath}/index">Home</a>
                <a class="nav-link" href="${pageContext.request.contextPath}/matches">Matches</a>
            </nav>
        </div>
    </section>
</header>
<main>
    <div class="container">
        <h1>Current match</h1>
        <div class="current-match-image"></div>
        <section class="score">
            <table class="table">
                <thead class="result">
                <tr>
                    <th class="table-text">Player</th>
                    <th class="table-text">Sets</th>
                    <c:choose>
                        <c:when test="${!isFinished}">
                            <th class="table-text">Games</th>
                            <th class="table-text">Points</th>
                            <th class="table-text">Action</th>
                        </c:when>
                        <c:when test="${isFinished}">
                            <th class="table-text">Result</th>
                        </c:when>
                    </c:choose>
                </tr>
                </thead>
                <tbody>
                <tr class="player1">
                    <td class="table-text">${firstPlayer.name}</td>
                    <td class="table-text">${match.getScoreValue(firstPlayer)}</td>
                    <c:choose>
                        <c:when test="${!isFinished}">
                            <td class="table-text">${match.getCurrentSetValue(firstPlayer)}</td>
                            <td class="table-text">${match.getCurrentGameValue(firstPlayer)}</td>
                        </c:when>
                    </c:choose>
                    <td class="table-text">
                        <c:choose>
                            <c:when test="${!isFinished}">
                                <form action="${pageContext.request.contextPath}/match-score" method="post">
                                    <input type="hidden" name="uuid" value="${uuid}">
                                    <input type="hidden" name="playerId" value="${firstPlayer.id}">
                                    <button type="submit" class="score-btn">Point</button>
                                </form>
                            </c:when>
                            <c:when test="${winnerId == firstPlayer.id}">
                                <span class="winner-text">Winner</span>
                            </c:when>
                            <c:otherwise>
                                <span class="loser-text">Loser</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>

                <tr class="player2">
                    <td class="table-text">${secondPlayer.name}</td>
                    <td class="table-text">${match.getScoreValue(secondPlayer)}</td>
                    <c:choose>
                        <c:when test="${!isFinished}">
                            <td class="table-text">${match.getCurrentSetValue(secondPlayer)}</td>
                            <td class="table-text">${match.getCurrentGameValue(secondPlayer)}</td>
                        </c:when>
                    </c:choose>
                    <td class="table-text">
                        <c:choose>
                            <c:when test="${!isFinished}">
                                <form action="${pageContext.request.contextPath}/match-score" method="post">
                                    <input type="hidden" name="uuid" value="${uuid}">
                                    <input type="hidden" name="playerId" value="${secondPlayer.id}">
                                    <button type="submit" class="score-btn">Point</button>
                                </form>
                            </c:when>
                            <c:when test="${winnerId == secondPlayer.id}">
                                <span class="winner-text">Winner</span>
                            </c:when>
                            <c:otherwise>
                                <span class="loser-text">Loser</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                </tbody>
            </table>
        </section>
    </div>
</main>
<footer>
    <div class="footer">
        <p>&copy; Tennis Scoreboard, project from
            <a href="https://zhukovsd.github.io/java-backend-learning-course/">zhukovsd/java-backend-learning-course</a>
            roadmap.</p>
    </div>
</footer>
</body>
</html>