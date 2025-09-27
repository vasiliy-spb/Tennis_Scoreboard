<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Server Error | Tennis Scoreboard</title>
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
            color: #dc3545;
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

        .support-contact {
            margin-top: 30px;
            font-size: 16px;
            color: #6c757d;
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
            <div class="error-code">500</div>
            <h1 class="error-message">Internal Server Error</h1>
            <p class="error-description">
                This wouldn't have happened if.. well, you know.
            </p>
            <div class="error-image">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100">
                    <circle cx="50" cy="50" r="45" fill="#f8f9fa" stroke="#dc3545" stroke-width="2"/>
                    <path d="M50,30 V50 M50,55 V60" stroke="#dc3545" stroke-width="2" stroke-linecap="round"/>
                    <circle cx="50" cy="65" r="2" fill="#dc3545"/>
                    <path d="M35,35 L30,30 M65,35 L70,30 M35,65 L30,70 M65,65 L70,70"
                          stroke="#6c757d" stroke-width="2" stroke-linecap="round"/>
                </svg>
            </div>
            <a href="${pageContext.request.contextPath}/index" class="home-button">Go to Homepage</a>
            <p class="support-contact">
            </p>
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