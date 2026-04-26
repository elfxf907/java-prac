<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<html>
<head>
    <title>Список компаний</title>
</head>
<body>
<jsp:include page="../fragments/header.jsp"/>

<h1>Список компаний</h1>

<p>
    <a href="${pageContext.request.contextPath}/companies/new">Добавить компанию</a>
</p>

<table border="1" cellpadding="6" cellspacing="0">
    <tr>
        <th>ID</th>
        <th>Название</th>
        <th>Адрес</th>
        <th>Действия</th>
    </tr>

    <c:forEach var="company" items="${companies}">
        <tr>
            <td>${company.id}</td>
            <td>${company.name}</td>
            <td>${company.address}</td>
            <td>
                <a href="${pageContext.request.contextPath}/companies/${company.id}">Открыть</a>
                |
                <a href="${pageContext.request.contextPath}/companies/${company.id}/edit">Редактировать</a>
                |
                <form action="${pageContext.request.contextPath}/companies/${company.id}/delete"
                      method="post" style="display:inline;">
                    <button type="submit">Удалить</button>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>

</body>
</html>