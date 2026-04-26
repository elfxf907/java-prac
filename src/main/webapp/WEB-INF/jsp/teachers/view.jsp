<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<html>
<head>
    <title>Карточка преподавателя</title>
</head>
<body>
<jsp:include page="../fragments/header.jsp"/>

<h1>Карточка преподавателя</h1>

<p><strong>ID:</strong> ${details.teacher.id}</p>
<p><strong>ФИО:</strong> ${details.teacher.fullName}</p>
<p><strong>Компания:</strong> ${details.teacher.company.name}</p>

<p>
    <a href="${pageContext.request.contextPath}/teachers/${details.teacher.id}/edit">Редактировать</a>
</p>

<h2>Курсы преподавателя</h2>
<c:choose>
    <c:when test="${empty details.courses}">
        <p>Курсов пока нет.</p>
    </c:when>
    <c:otherwise>
        <ul>
            <c:forEach var="course" items="${details.courses}">
                <li>${course.title}</li>
            </c:forEach>
        </ul>
    </c:otherwise>
</c:choose>

<p>
    <a href="${pageContext.request.contextPath}/teachers">Назад к списку</a>
</p>

</body>
</html>