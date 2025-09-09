<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isErrorPage="true" %>
<html>
<head>
    <title>Ошибка ${statusCode}</title>
</head>
<body>
    <h1>Произошла ошибка</h1>
    <p>Код ошибки: ${statusCode}</p>
    <p>Сообщение: ${errorMessage}</p>
    <a href="${pageContext.request.contextPath}/">Вернуться на главную</a>
</body>
</html>