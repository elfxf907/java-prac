<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <title>Форма обучающегося</title>
</head>
<body>
<jsp:include page="../fragments/header.jsp"/>

<c:choose>
    <c:when test="${formMode == 'edit'}">
        <h1>Редактирование обучающегося</h1>
    </c:when>
    <c:otherwise>
        <h1>Добавление обучающегося</h1>
    </c:otherwise>
</c:choose>

<c:choose>
    <c:when test="${formMode == 'edit'}">
        <c:set var="formAction" value="${pageContext.request.contextPath}/students/${studentId}"/>
    </c:when>
    <c:otherwise>
        <c:set var="formAction" value="${pageContext.request.contextPath}/students"/>
    </c:otherwise>
</c:choose>

<form:form method="post" action="${formAction}" modelAttribute="studentForm">
    <div>
        <label for="fullName">ФИО:</label><br/>
        <form:input path="fullName" id="fullName"/>
        <br/>
        <form:errors path="fullName" cssStyle="color:red;"/>
    </div>

    <div style="margin-top: 10px;">
        <button type="submit">Сохранить</button>
    </div>
</form:form>

<p>
    <a href="${pageContext.request.contextPath}/students">Назад к списку</a>
</p>

</body>
</html>