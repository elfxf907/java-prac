<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <title>Форма преподавателя</title>
</head>
<body>
<jsp:include page="../fragments/header.jsp"/>

<c:choose>
    <c:when test="${formMode == 'edit'}">
        <h1>Редактирование преподавателя</h1>
    </c:when>
    <c:otherwise>
        <h1>Добавление преподавателя</h1>
    </c:otherwise>
</c:choose>

<c:if test="${not empty saveError}">
    <p style="color:red;">${saveError}</p>
</c:if>

<c:choose>
    <c:when test="${formMode == 'edit'}">
        <c:set var="formAction" value="${pageContext.request.contextPath}/teachers/${teacherId}"/>
    </c:when>
    <c:otherwise>
        <c:set var="formAction" value="${pageContext.request.contextPath}/teachers"/>
    </c:otherwise>
</c:choose>

<form:form method="post" action="${formAction}" modelAttribute="teacherForm">
    <div>
        <label for="fullName">ФИО:</label><br/>
        <form:input path="fullName" id="fullName"/>
        <br/>
        <form:errors path="fullName" cssStyle="color:red;"/>
    </div>

    <div style="margin-top: 10px;">
        <label for="companyId">Компания:</label><br/>
        <form:select path="companyId" id="companyId">
            <form:option value="" label="-- выберите компанию --"/>
            <form:options items="${companies}" itemValue="id" itemLabel="name"/>
        </form:select>
        <br/>
        <form:errors path="companyId" cssStyle="color:red;"/>
    </div>

    <div style="margin-top: 10px;">
        <button type="submit">Сохранить</button>
    </div>
</form:form>

<p>
    <a href="${pageContext.request.contextPath}/teachers">Назад к списку</a>
</p>

</body>
</html>