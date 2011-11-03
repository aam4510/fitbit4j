<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<%@ taglib prefix='fn' uri='http://java.sun.com/jsp/jstl/functions' %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Fitbit Client Example Application</title>
    <link href="${pageContext.request.contextPath}/ui/css/include.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<div class="wrap">
    <div class="logo"></div>
    <div class="content">
        <h1>Fitbit Client Example Application</h1>

        <c:if test="${!empty message}">
        <div class="message">
            <c:out value="${message}"/>
        </div>
        </c:if>

        <c:if test="${errors != null && fn:length(errors) > 0}">
        <ul class="error">
            <c:forEach items="${errors}" var="error">
                <li><c:out value="${error}"/></li>
            </c:forEach>
        </ul>
        </c:if>

        <div class="parag">
            <h3>Your local user ID in this application is: ${actionBean.ourUser.userId}</h3>
        </div>
        <div class="parag left">
            <c:choose>
                <c:when test="${!isAuthorized}">
                    <p>Status: <b class="uppercase">UNAUTHORIZED</b></p>

                    <p><a href="${exampleBaseUrl}/authorize">Authorize this application on Fitbit.</a></p>
                </c:when>
                <c:otherwise>
                    <p>Status: <b class="uppercase">AUTHORIZED</b>
                        (as user <a href="${userProfileURL}">${encodedUserId})</a>
                    </p>

                    <p><a href="${exampleBaseUrl}/expireResourceCredentials">Expire your cached Fitbit token
                        credentials.</a> (You can re-authorize afterwards.)</p>
                </c:otherwise>
            </c:choose>
        </div>
        <jsp:doBody/>
        <div class="hr"></div>

        <c:if test="${isAuthorized}">
        <h2>Example User Actions:</h2>

        <div class="parag left normal">
                    <p><a href="${exampleBaseUrl}/profile">See and update profile</a></p>

                    <p><a href="${exampleBaseUrl}/sleep">Sleep</a></p>

                    <p><a href="${exampleBaseUrl}/water">Water</a></p>
            <c:choose>
                <c:when test="${!isSubscribed}">
                    <p><a href="${exampleBaseUrl}/subscribe">Subscribe this application to your activity and food
                        streams</a>
                        (subscription status tracked locally)</p>
                </c:when>
                <c:otherwise>
                    <p><a href="${exampleBaseUrl}/unsubscribe">Unsubscribe this application from your activity and food
                        streams</a>
                        (subscription status tracked locally)</p>
                </c:otherwise>
            </c:choose>

            <p class="left"><i>If subscribed, the Fitbit server will notify this example application when you change
                your data on fitbit.com.</i></p>
        </div>
        </c:if>
        <h2>Example Global Actions:</h2>

        <div class="parag left normal">
            <c:if test="${isAuthorized}">
                <p><a href="${exampleBaseUrl}/createFoodForm">Create New Food</a></p>
                <p><a href="${exampleBaseUrl}/invitations">Invitations</a></p>
            </c:if>
            <c:if test="${isAuthorized}">
                <p><a href="${exampleBaseUrl}/allSubscriptions">View all of this application's subscriptions and received
                    notifications</a> (localtransient data)</p>
                <p class="left"><i>This application maintains a list of activity stream subscriptions, and the last time
                    each was updated. This list is transient and may reset periodically.</i></p>
            </c:if>
        </div>
    </div>
</div>
</body>
</html>