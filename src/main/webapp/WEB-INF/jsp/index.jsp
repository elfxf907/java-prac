<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title>Учебный центр</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>

<h1>Система учебного центра</h1>

<ul>
    <li><a href="${pageContext.request.contextPath}/students">Обучающиеся</a></li>
    <li><a href="${pageContext.request.contextPath}/teachers">Преподаватели</a></li>
    <li><a href="${pageContext.request.contextPath}/companies">Компании</a></li>
    <li><a href="${pageContext.request.contextPath}/courses">Курсы</a></li>
    <li><a href="${pageContext.request.contextPath}/schedule">Поиск расписания</a></li>
</ul>

</body>
</html>