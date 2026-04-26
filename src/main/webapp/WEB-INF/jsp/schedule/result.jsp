<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<html>
<head>
    <title>Результат поиска расписания</title>
</head>
<body>
<jsp:include page="../fragments/header.jsp"/>

<h1>Результат поиска расписания</h1>

<p>
    <strong>Тип поиска:</strong>
    <c:choose>
        <c:when test="${result.searchType == 'student'}">Обучающийся</c:when>
        <c:otherwise>Преподаватель</c:otherwise>
    </c:choose>
</p>

<p><strong>Объект поиска:</strong> ${result.subjectName}</p>
<p><strong>Период:</strong> ${from} — ${to}</p>

<h2>Найденные занятия</h2>

<c:choose>
    <c:when test="${empty result.lessons}">
        <p>За указанный период занятий не найдено.</p>
    </c:when>
    <c:otherwise>
        <table border="1" cellpadding="6" cellspacing="0">
            <tr>
                <th>ID</th>
                <th>Курс</th>
                <th>Преподаватель</th>
                <th>Начало</th>
                <th>Окончание</th>
            </tr>
            <c:forEach var="lesson" items="${result.lessons}">
                <tr>
                    <td>${lesson.id}</td>
                    <td>${lesson.course.title}</td>
                    <td>${lesson.teacher.fullName}</td>
                    <td>${lesson.startTime}</td>
                    <td>${lesson.endTime}</td>
                </tr>
            </c:forEach>
        </table>
    </c:otherwise>
</c:choose>

<p style="margin-top: 15px;">
    <a href="${pageContext.request.contextPath}/schedule">Новый поиск</a>
</p>

</body>
</html>