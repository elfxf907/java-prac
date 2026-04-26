<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<html>
<head>
    <title>Карточка компании</title>
</head>
<body>
<jsp:include page="../fragments/header.jsp"/>

<h1>Карточка компании</h1>

<p><strong>ID:</strong> ${details.company.id}</p>
<p><strong>Название:</strong> ${details.company.name}</p>
<p><strong>Адрес:</strong> ${details.company.address}</p>

<p>
    <a href="${pageContext.request.contextPath}/companies/${details.company.id}/edit">Редактировать</a>
</p>

<h2>Преподаватели компании</h2>
<c:choose>
    <c:when test="${empty details.teachers}">
        <p>Преподавателей пока нет.</p>
    </c:when>
    <c:otherwise>
        <ul>
            <c:forEach var="teacher" items="${details.teachers}">
                <li>${teacher.fullName}</li>
            </c:forEach>
        </ul>
    </c:otherwise>
</c:choose>

<h2>Курсы компании</h2>
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
    <a href="${pageContext.request.contextPath}/companies">Назад к списку</a>
</p>

</body>
</html>