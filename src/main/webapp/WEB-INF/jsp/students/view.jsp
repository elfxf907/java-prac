<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <title>Карточка обучающегося</title>
</head>
<body>
<jsp:include page="../fragments/header.jsp"/>

<h1>Карточка обучающегося</h1>

<p><strong>ID:</strong> ${details.student.id}</p>
<p><strong>ФИО:</strong> ${details.student.fullName}</p>

<p>
    <a href="${pageContext.request.contextPath}/students/${details.student.id}/edit">Редактировать</a>
</p>

<h2>Курсы обучающегося</h2>

<c:choose>
    <c:when test="${empty details.courses}">
        <p>Обучающийся пока не записан ни на один курс.</p>
    </c:when>
    <c:otherwise>
        <ul>
            <c:forEach var="course" items="${details.courses}">
                <li>
                    <a href="${pageContext.request.contextPath}/courses/${course.id}">
                        ${course.title}
                    </a>
                </li>
            </c:forEach>
        </ul>
    </c:otherwise>
</c:choose>

<h2>Записать на курс</h2>

<c:if test="${not empty saveError}">
    <p style="color:red;">${saveError}</p>
</c:if>

<c:choose>
    <c:when test="${empty details.availableCourses}">
        <p>Нет доступных курсов для записи.</p>
    </c:when>
    <c:otherwise>
        <form:form method="post"
                   action="${pageContext.request.contextPath}/students/${details.student.id}/enroll"
                   modelAttribute="enrollmentForm">
            <div>
                <label for="courseId">Курс:</label><br/>
                <form:select path="courseId" id="courseId">
                    <form:option value="" label="-- выберите курс --"/>
                    <form:options items="${details.availableCourses}" itemValue="id" itemLabel="title"/>
                </form:select>
                <br/>
                <form:errors path="courseId" cssStyle="color:red;"/>
            </div>

            <div style="margin-top: 10px;">
                <button type="submit">Записать</button>
            </div>
        </form:form>
    </c:otherwise>
</c:choose>

<p>
    <a href="${pageContext.request.contextPath}/students">Назад к списку</a>
</p>

</body>
</html>