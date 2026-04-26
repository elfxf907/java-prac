<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<html>
<head>
    <title>Карточка курса</title>
</head>
<body>
<jsp:include page="../fragments/header.jsp"/>

<h1>Карточка курса</h1>

<p><strong>ID:</strong> ${details.course.id}</p>
<p><strong>Название:</strong> ${details.course.title}</p>
<p><strong>Компания:</strong> ${details.course.company.name}</p>
<p><strong>Тип длительности:</strong> ${details.course.durationType}</p>
<p><strong>Часов в день:</strong> ${details.course.hoursPerDay}</p>
<p><strong>Описание:</strong> ${details.course.description}</p>

<p>
    <a href="${pageContext.request.contextPath}/courses/${details.course.id}/edit">Редактировать</a>
</p>

<h2>Преподаватели курса</h2>
<c:choose>
    <c:when test="${empty details.teachers}">
        <p>Преподаватели пока не назначены.</p>
    </c:when>
    <c:otherwise>
        <ul>
            <c:forEach var="teacher" items="${details.teachers}">
                <li>${teacher.fullName}</li>
            </c:forEach>
        </ul>
    </c:otherwise>
</c:choose>

<h2>Обучающиеся курса</h2>
<c:choose>
    <c:when test="${empty details.students}">
        <p>Обучающихся пока нет.</p>
    </c:when>
    <c:otherwise>
        <ul>
            <c:forEach var="student" items="${details.students}">
                <li>${student.fullName}</li>
            </c:forEach>
        </ul>
    </c:otherwise>
</c:choose>

<h2>Занятия курса</h2>
<c:choose>
    <c:when test="${empty details.lessons}">
        <p>Занятий пока нет.</p>
    </c:when>
    <c:otherwise>
        <ul>
            <c:forEach var="lesson" items="${details.lessons}">
                <li>
                    ${lesson.startTime} — ${lesson.endTime}
                </li>
            </c:forEach>
        </ul>
    </c:otherwise>
</c:choose>
<p>
    <a href="${pageContext.request.contextPath}/courses/${details.course.id}/lessons/new">
        Добавить занятие
    </a>
</p>

<p>
    <a href="${pageContext.request.contextPath}/courses">Назад к списку</a>
</p>

</body>
</html>