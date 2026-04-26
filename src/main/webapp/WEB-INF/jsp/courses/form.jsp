<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <title>Форма курса</title>
</head>
<body>
<jsp:include page="../fragments/header.jsp"/>

<c:choose>
    <c:when test="${formMode == 'edit'}">
        <h1>Редактирование курса</h1>
    </c:when>
    <c:otherwise>
        <h1>Добавление курса</h1>
    </c:otherwise>
</c:choose>

<c:if test="${not empty saveError}">
    <p style="color:red;">${saveError}</p>
</c:if>

<c:choose>
    <c:when test="${formMode == 'edit'}">
        <c:set var="formAction" value="${pageContext.request.contextPath}/courses/${courseId}"/>
    </c:when>
    <c:otherwise>
        <c:set var="formAction" value="${pageContext.request.contextPath}/courses"/>
    </c:otherwise>
</c:choose>

<form:form method="post" action="${formAction}" modelAttribute="courseForm">
    <div>
        <label for="title">Название курса:</label><br/>
        <form:input path="title" id="title"/>
        <br/>
        <form:errors path="title" cssStyle="color:red;"/>
    </div>

    <div style="margin-top: 10px;">
        <label for="description">Описание:</label><br/>
        <form:textarea path="description" id="description" rows="5" cols="50"/>
        <br/>
        <form:errors path="description" cssStyle="color:red;"/>
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
        <label for="durationType">Тип длительности:</label><br/>
        <form:select path="durationType" id="durationType">
            <form:option value="" label="-- выберите тип длительности --"/>
            <form:option value="DAY" label="DAY"/>
            <form:option value="SEVERAL_DAYS" label="SEVERAL_DAYS"/>
            <form:option value="TWO_WEEKS" label="TWO_WEEKS"/>
            <form:option value="MONTH" label="MONTH"/>
        </form:select>
        <br/>
        <form:errors path="durationType" cssStyle="color:red;"/>
    </div>

    <div style="margin-top: 10px;">
        <label for="hoursPerDay">Часов в день:</label><br/>
        <form:input path="hoursPerDay" id="hoursPerDay" type="number"/>
        <br/>
        <form:errors path="hoursPerDay" cssStyle="color:red;"/>
    </div>

    <div style="margin-top: 10px;">
        <button type="submit">Сохранить</button>
    </div>
</form:form>

<p>
    <a href="${pageContext.request.contextPath}/courses">Назад к списку</a>
</p>

</body>
</html>