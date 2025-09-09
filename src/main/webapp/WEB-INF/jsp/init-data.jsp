<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tennis Scoreboard | Initialize Data</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;700&display=swap" rel="stylesheet">
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
        <h1>Add Data</h1>
        <div class="init-data-image"></div>
        <div class="form-container center">
            <form method="post" action="${pageContext.request.contextPath}/init-data">
                <div class="form-group">
                    <label class="label-player" for="matchCount">Number of Matches</label>
                    <input class="input-player" type="number" id="matchCount" name="matchCount"
                           min="1" max="1000" value="100" required>
                </div>

                <div class="form-group">
                    <label class="label-player" for="nameSet">Name Set</label>
                    <select class="input-player" id="nameSet" name="nameSet" required>
                        <option value="REAL">Real Tennis Players</option>
                        <option value="COMICS" selected>Comics Characters</option>
                    </select>
                </div>

                <input class="form-button" type="submit" value="Generate Data">
            </form>
        </div>
    </div>
</main>
<footer>
    <div class="footer">
        <p>&copy; Tennis Scoreboard, project from <a href="https://zhukovsd.github.io/java-backend-learning-course/">zhukovsd/java-backend-learning-course</a> roadmap.</p>
    </div>
</footer>
</body>
</html>