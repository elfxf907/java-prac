<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <title>Поиск расписания</title>
</head>
<body>
<jsp:include page="../fragments/header.jsp"/>

<h1>Поиск расписания</h1>

<c:if test="${not empty saveError}">
    <p style="color:red;">${saveError}</p>
</c:if>

<form:form method="post"
           action="${pageContext.request.contextPath}/schedule"
           modelAttribute="scheduleSearchForm">

    <div>
        <label for="searchType">Тип поиска:</label><br/>
        <form:select path="searchType" id="searchType">
            <form:option value="" label="-- выберите тип --"/>
            <form:option value="student" label="По обучающемуся"/>
            <form:option value="teacher" label="По преподавателю"/>
        </form:select>
        <br/>
        <form:errors path="searchType" cssStyle="color:red;"/>
    </div>

    <div style="margin-top: 10px;">
        <label for="studentId">Обучающийся:</label><br/>
        <form:select path="studentId" id="studentId">
            <form:option value="" label="-- выберите обучающегося --"/>
            <form:options items="${students}" itemValue="id" itemLabel="fullName"/>
        </form:select>
    </div>

    <div style="margin-top: 10px;">
        <label for="teacherId">Преподаватель:</label><br/>
        <form:select path="teacherId" id="teacherId">
            <form:option value="" label="-- выберите преподавателя --"/>
            <form:options items="${teachers}" itemValue="id" itemLabel="fullName"/>
        </form:select>
    </div>

    <div style="margin-top: 10px;">
        <label for="from">Начало периода:</label><br/>
        <form:input path="from" id="from" type="datetime-local"/>
        <br/>
        <form:errors path="from" cssStyle="color:red;"/>
    </div>

    <div style="margin-top: 10px;">
        <label for="to">Конец периода:</label><br/>
        <form:input path="to" id="to" type="datetime-local"/>
        <br/>
        <form:errors path="to" cssStyle="color:red;"/>
    </div>

    <div style="margin-top: 10px;">
        <button type="submit">Найти расписание</button>
    </div>
</form:form>

</body>
</html>