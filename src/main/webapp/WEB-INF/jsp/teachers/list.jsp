<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<html>
<head>
    <title>Список преподавателей</title>
</head>
<body>
<jsp:include page="../fragments/header.jsp"/>

<h1>Список преподавателей</h1>

<p>
    <a href="${pageContext.request.contextPath}/teachers/new">Добавить преподавателя</a>
</p>

<table border="1" cellpadding="6" cellspacing="0">
    <tr>
        <th>ID</th>
        <th>ФИО</th>
        <th>Компания</th>
        <th>Действия</th>
    </tr>

    <c:forEach var="teacher" items="${teachers}">
        <tr>
            <td>${teacher.id}</td>
            <td>${teacher.fullName}</td>
            <td>${teacher.company.name}</td>
            <td>
                <a href="${pageContext.request.contextPath}/teachers/${teacher.id}">Открыть</a>
                |
                <a href="${pageContext.request.contextPath}/teachers/${teacher.id}/edit">Редактировать</a>
                |
                <form action="${pageContext.request.contextPath}/teachers/${teacher.id}/delete"
                      method="post" style="display:inline;">
                    <button type="submit">Удалить</button>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>

</body>
</html>