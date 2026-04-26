<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <title>Добавление занятия</title>
</head>
<body>
<jsp:include page="../fragments/header.jsp"/>

<h1>Добавление занятия</h1>

<p><strong>Курс:</strong> ${course.title}</p>

<c:if test="${not empty saveError}">
    <p style="color:red;">${saveError}</p>
</c:if>

<form:form method="post"
           action="${pageContext.request.contextPath}/courses/${course.id}/lessons"
           modelAttribute="lessonForm">

    <div>
        <label for="teacherId">Преподаватель:</label><br/>
        <form:select path="teacherId" id="teacherId">
            <form:option value="" label="-- выберите преподавателя --"/>
            <form:options items="${teachers}" itemValue="id" itemLabel="fullName"/>
        </form:select>
        <br/>
        <form:errors path="teacherId" cssStyle="color:red;"/>
    </div>

    <div style="margin-top: 10px;">
        <label for="startTime">Дата и время начала:</label><br/>
        <form:input path="startTime" id="startTime" type="datetime-local"/>
        <br/>
        <form:errors path="startTime" cssStyle="color:red;"/>
    </div>

    <div style="margin-top: 10px;">
        <label for="endTime">Дата и время окончания:</label><br/>
        <form:input path="endTime" id="endTime" type="datetime-local"/>
        <br/>
        <form:errors path="endTime" cssStyle="color:red;"/>
    </div>

    <div style="margin-top: 10px;">
        <button type="submit">Сохранить занятие</button>
    </div>
</form:form>

<p>
    <a href="${pageContext.request.contextPath}/courses/${course.id}">Назад к курсу</a>
</p>

</body>
</html>