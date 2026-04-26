<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<html>
<head>
    <title>Список курсов</title>
</head>
<body>
<jsp:include page="../fragments/header.jsp"/>

<h1>Список курсов</h1>

<p>
    <a href="${pageContext.request.contextPath}/courses/new">Добавить курс</a>
</p>

<table border="1" cellpadding="6" cellspacing="0">
    <tr>
        <th>ID</th>
        <th>Название</th>
        <th>Компания</th>
        <th>Тип длительности</th>
        <th>Часов в день</th>
        <th>Действия</th>
    </tr>

    <c:forEach var="course" items="${courses}">
        <tr>
            <td>${course.id}</td>
            <td>${course.title}</td>
            <td>${course.company.name}</td>
            <td>${course.durationType}</td>
            <td>${course.hoursPerDay}</td>
            <td>
                <a href="${pageContext.request.contextPath}/courses/${course.id}">Открыть</a>
                |
                <a href="${pageContext.request.contextPath}/courses/${course.id}/edit">Редактировать</a>
                |
                <form action="${pageContext.request.contextPath}/courses/${course.id}/delete"
                      method="post" style="display:inline;">
                    <button type="submit">Удалить</button>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>

</body>
</html>