<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Page Not Found | Tennis Scoreboard</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .error-container {
            text-align: center;
            padding: 60px 20px;
        }

        .error-code {
            font-size: 120px;
            font-weight: 700;
            color: #42a7f5;
            margin-bottom: 20px;
            line-height: 1;
        }

        .error-message {
            font-size: 24px;
            margin-bottom: 30px;
            color: #333;
        }

        .error-description {
            font-size: 18px;
            margin-bottom: 40px;
            color: #666;
            max-width: 600px;
            margin-left: auto;
            margin-right: auto;
        }

        .error-image {
            max-width: 300px;
            margin: 0 auto 40px;
        }

        .home-button {
            display: inline-block;
            padding: 12px 30px;
            background-color: #42a7f5;
            color: white;
            text-decoration: none;
            border-radius: 25px;
            font-size: 18px;
            font-weight: 500;
            transition: background-color 0.3s;
        }

        .home-button:hover {
            background-color: #2b7fc3;
        }
    </style>
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
        <div class="error-container">
            <div class="error-code">404</div>
            <h1 class="error-message">Page Not Found</h1>
            <p class="error-description">
                The page you are looking isn't exists. If you want to create it, then <a
                    href="https://github.com/vasiliy-spb/Tennis_Scoreboard">here</a> is the source code
            </p>
            <div class="error-image">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100">
                    <circle cx="50" cy="50" r="45" fill="#f8f9fa" stroke="#42a7f5" stroke-width="2"/>
                    <path d="M35,35 L65,65 M65,35 L35,65" stroke="#dc3545" stroke-width="3" stroke-linecap="round"/>
                    <path d="M30,30 L70,30 L70,70 L30,70 Z" fill="none" stroke="#6c757d" stroke-width="2"/>
                </svg>
            </div>
            <a href="${pageContext.request.contextPath}/index" class="home-button">Go to Homepage</a>
        </div>
    </div>
</main>

<footer>
    <div class="footer">
        <p>&copy; Tennis Scoreboard, project from
            <a href="https://zhukovsd.github.io/java-backend-learning-course/">
                zhukovsd/java-backend-learning-course
            </a> roadmap.
        </p>
    </div>
</footer>
</body>
</html>