<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<html>
<head>
    <title>Список обучающихся</title>
</head>
<body>
<jsp:include page="../fragments/header.jsp"/>

<h1>Список обучающихся</h1>

<p>
    <a href="${pageContext.request.contextPath}/students/new">Добавить обучающегося</a>
</p>

<table border="1" cellpadding="6" cellspacing="0">
    <tr>
        <th>ID</th>
        <th>ФИО</th>
        <th>Действия</th>
    </tr>
    <c:forEach var="student" items="${students}">
        <tr>
            <td>${student.id}</td>
            <td>${student.fullName}</td>
            <td>
                <a href="${pageContext.request.contextPath}/students/${student.id}">Открыть</a>
                |
                <a href="${pageContext.request.contextPath}/students/${student.id}/edit">Редактировать</a>
                |
                <form action="${pageContext.request.contextPath}/students/${student.id}/delete"
                      method="post" style="display:inline;">
                    <button type="submit">Удалить</button>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>

</body>
</html>