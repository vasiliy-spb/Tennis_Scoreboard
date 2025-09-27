<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Tennis Scoreboard | Finished Matches</title>
    <link rel="preconnect" href="https://fonts.googleapis.com"/>
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin/>
    <link
            href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;700&display=swap"
            rel="stylesheet"
    />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"/>
    <script src="${pageContext.request.contextPath}/js/app.js"></script>
</head>
<body>
<header class="header">
    <section class="nav-header">
        <div class="brand">
            <div class="nav-toggle">
                <img
                        src="${pageContext.request.contextPath}/images/menu.png"
                        alt="Logo"
                        class="logo"
                />
            </div>
            <span class="logo-text">TennisScoreboard</span>
        </div>
        <nav class="nav-links">
            <a class="nav-link" href="${pageContext.request.contextPath}/index">Home</a>
            <a class="nav-link" href="${pageContext.request.contextPath}/init-data">Add Data</a>
        </nav>
    </section>
</header>

<main>
    <div class="container">
        <h1>Matches</h1>
        <div class="frame-matches-image"></div>

        <div class="input-container">
            <form method="get" action="${pageContext.request.contextPath}/matches" class="filter-form">
                <div class="filter-group">
                    <input class="input-filter" type="text" name="filter_by_player_name"
                           placeholder="Filter by name" value="<c:out value='${filter}'/>"/>
                    <div class="button-group">
                        <button class="btn-filter" type="submit">Filter</button>
                        <a href="${pageContext.request.contextPath}/matches" class="btn-filter reset-btn">Reset</a>
                    </div>
                </div>
            </form>
        </div>

        <table class="table-matches">
            <thead>
            <tr>
                <th>Player One</th>
                <th>Player Two</th>
                <th>Winner</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="m" items="${matches}">
                <tr>
                    <td>
                        <c:out value='${m.firstPlayer.name}'/>
                    </td>
                    <td>
                        <c:out value='${m.secondPlayer.name}'/>
                    </td>
                    <td>
                        <c:out value='${m.winner.name}'/>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${fn:length(matches) == 0}">
                <tr>
                    <td colspan="3" style="text-align:center; color:#888;">
                        No matches found
                    </td>
                </tr>
            </c:if>
            </tbody>
        </table>

        <c:if test="${hasPrev or hasNext}">
            <c:if test="${totalPages > 1}">
                <div class="pagination">
                    <!-- Кнопка "Предыдущая" -->
                    <c:if test="${currentPage > 1}">

                        <c:url var="pageUrl" value="/matches">
                            <c:param name="page" value="${currentPage - 1}"/>
                            <c:param name="filter_by_player_name" value="${filter}"/>
                        </c:url>
                        <a class="pagination-btn prev-btn" href="${pageUrl}">< Prev</a>

                    </c:if>

                    <!-- Умная нумерация страниц -->
                    <c:choose>
                        <c:when test="${totalPages <= 7}">
                            <!-- Если страниц мало, показываем все -->
                            <c:forEach begin="1" end="${totalPages}" var="pageNum">
                                <c:choose>
                                    <c:when test="${pageNum == currentPage}">
                                        <span class="pagination-btn current-page">${pageNum}</span>
                                    </c:when>
                                    <c:otherwise>

                                        <c:url var="pageUrl" value="/matches">
                                            <c:param name="page" value="${pageNum}"/>
                                            <c:param name="filter_by_player_name" value="${filter}"/>
                                        </c:url>
                                        <a class="pagination-btn" href="${pageUrl}">${pageNum}</a>

                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <!-- Если страниц много, показываем урезанный список -->
                            <!-- Всегда показываем первую страницу -->
                            <c:choose>
                                <c:when test="${1 == currentPage}">
                                    <span class="pagination-btn current-page">1</span>
                                </c:when>
                                <c:otherwise>
                                    <c:url var="pageUrl" value="/matches">
                                        <c:param name="page" value="1"/>
                                        <c:param name="filter_by_player_name" value="${filter}"/>
                                    </c:url>
                                    <a class="pagination-btn" href="${pageUrl}">1</a>
                                </c:otherwise>
                            </c:choose>

                            <c:if test="${currentPage > 4}">
                                <span class="pagination-ellipsis">...</span>
                            </c:if>

                            <!-- Показываем страницы вокруг текущей -->
                            <c:forEach var="pageNum" begin="${currentPage - 2 > 2 ? currentPage - 2 : 2}"
                                       end="${currentPage + 2 < totalPages - 1 ? currentPage + 2 : totalPages - 1}">
                                <c:choose>
                                    <c:when test="${pageNum == currentPage}">
                                        <span class="pagination-btn current-page">${pageNum}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <c:url var="pageUrl" value="/matches">
                                            <c:param name="page" value="${pageNum}"/>
                                            <c:param name="filter_by_player_name" value="${filter}"/>
                                        </c:url>
                                        <a class="pagination-btn" href="${pageUrl}">${pageNum}</a>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>

                            <c:if test="${currentPage < totalPages - 3}">
                                <span class="pagination-ellipsis">...</span>
                            </c:if>

                            <!-- Всегда показываем последнюю страницу -->
                            <c:choose>
                                <c:when test="${totalPages == currentPage}">
                                    <span class="pagination-btn current-page">${totalPages}</span>
                                </c:when>
                                <c:otherwise>
                                    <c:url var="pageUrl" value="/matches">
                                        <c:param name="page" value="${totalPages}"/>
                                        <c:param name="filter_by_player_name" value="${filter}"/>
                                    </c:url>
                                    <a class="pagination-btn" href="${pageUrl}">${totalPages}</a>
                                </c:otherwise>
                            </c:choose>
                        </c:otherwise>
                    </c:choose>

                    <!-- Кнопка "Следующая" -->
                    <c:if test="${currentPage < totalPages}">

                        <c:url var="pageUrl" value="/matches">
                            <c:param name="page" value="${currentPage + 1}"/>
                            <c:param name="filter_by_player_name" value="${filter}"/>
                        </c:url>
                        <a class="pagination-btn next-btn" href="${pageUrl}">Next ></a>

                    </c:if>
                </div>
            </c:if>
        </c:if>
    </div>
</main>

<footer>
    <div class="footer">
        <p>
            &copy; Tennis Scoreboard, project from
            <a href="https://zhukovsd.github.io/java-backend-learning-course/">
                zhukovsd/java-backend-learning-course
            </a>
            roadmap.
        </p>
    </div>
</footer>
</body>
</html>