<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <title>Форма компании</title>
</head>
<body>
<jsp:include page="../fragments/header.jsp"/>

<c:choose>
    <c:when test="${formMode == 'edit'}">
        <h1>Редактирование компании</h1>
    </c:when>
    <c:otherwise>
        <h1>Добавление компании</h1>
    </c:otherwise>
</c:choose>

<c:if test="${not empty saveError}">
    <p style="color:red;">${saveError}</p>
</c:if>

<c:choose>
    <c:when test="${formMode == 'edit'}">
        <c:set var="formAction" value="${pageContext.request.contextPath}/companies/${companyId}"/>
    </c:when>
    <c:otherwise>
        <c:set var="formAction" value="${pageContext.request.contextPath}/companies"/>
    </c:otherwise>
</c:choose>

<form:form method="post" action="${formAction}" modelAttribute="companyForm">
    <div>
        <label for="name">Название компании:</label><br/>
        <form:input path="name" id="name"/>
        <br/>
        <form:errors path="name" cssStyle="color:red;"/>
    </div>

    <div style="margin-top: 10px;">
        <label for="address">Адрес:</label><br/>
        <form:textarea path="address" id="address" rows="4" cols="40"/>
        <br/>
        <form:errors path="address" cssStyle="color:red;"/>
    </div>

    <div style="margin-top: 10px;">
        <button type="submit">Сохранить</button>
    </div>
</form:form>

<p>
    <a href="${pageContext.request.contextPath}/companies">Назад к списку</a>
</p>

</body>
</html>