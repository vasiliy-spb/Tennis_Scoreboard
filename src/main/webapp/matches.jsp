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
      <a class="nav-link" href="${pageContext.request.contextPath}/index.jsp">Home</a>
      <a class="nav-link" href="${pageContext.request.contextPath}/matches">Matches</a>
    </nav>
  </section>
</header>

<main>
  <div class="container">
    <h1>Matches</h1>

    <div class="input-container">
      <form method="get" action="${pageContext.request.contextPath}/matches">
        <input
          class="input-filter"
          type="text"
          name="filter_by_player_name"
          placeholder="Filter by name"
          value="${filter}"
        />
        <button class="btn-filter" type="submit">Filter</button>
        <a href="${pageContext.request.contextPath}/matches">
          <button type="button" class="btn-filter">Reset</button>
        </a>
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
            <td>${m.firstPlayer.name}</td>
            <td>${m.secondPlayer.name}</td>
            <td>${m.winner.name}</td>
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
      <div class="pagination">
        <c:if test="${hasPrev}">
          <a
            class="prev"
            href="${pageContext.request.contextPath}/matches
                  ?page=${currentPage - 1}
                  &filter_by_player_name=${fn:escapeXml(filter)}"
          >&lt;</a>
        </c:if>

        <span class="num-page current">${currentPage}</span>

        <c:if test="${hasNext}">
          <a
            class="next"
            href="${pageContext.request.contextPath}/matches
                  ?page=${currentPage + 1}
                  &filter_by_player_name=${fn:escapeXml(filter)}"
          >&gt;</a>
        </c:if>
      </div>
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