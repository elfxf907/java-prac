<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title>Ошибка</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>

<h2>Ошибка</h2>
<p>${errorMessage}</p>

<p><a href="${pageContext.request.contextPath}/">На главную</a></p>
</body>
</html>