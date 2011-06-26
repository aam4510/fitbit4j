<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layout" uri="http://fitbit.com/api/layout" %>
<layout:shell>

    <c:if test="${isAuthorized}">
    <h2>Complete list of subscriptions in this application</h2>

    <div class="parag left">
        <p>This application uses your local user ID to identify your subscription with Fitbit.
                ${isSubscribed ? "Your subscription is highlighted." : "You are presently not subscribed."}
        </p>
    </div>
    <div class="parag left">
        <table class="blue">
            <thead>
            <tr>
                <th width="13%">Subscription ID</th>
                <th width="13%">Fitbit Owner Type</th>
                <th width="13%">Fitbit Owner ID</th>
                <th width="13%">Feed type</th>
                <%--<th width="19%">Last Notification</th>--%>
                <%--<th width="19%">Notes</th>--%>
            </tr>
            </thead>
            <%--@elvariable id="subscriptions" type="java.util.List<com.fitbit.api.model.ApiSubscription>"--%>
            <c:forEach items="${subscriptions}" var="subscription">
                <tr>
                    <td>${subscription.subscriptionId}</td>
                    <td>${subscription.ownerType}</td>
                    <td>${subscription.ownerId}</td>
                    <td>${subscription.collectionType}</td>
<%--                    <td>
                        <c:choose>
                            <c:when test="${not empty subscription.lastUpdateNotificationDate}">
                                <fmt:formatDate value="${subscription.lastUpdateNotificationDate}"
                                                type="date" pattern="yyyy-MM-dd HH:mm:ss"/>
                            </c:when>
                            <c:otherwise>
                                Never
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:if test="${not subscription.knownSubscription}">
                            Auto-created after receiving update notification.
                        </c:if>
                    </td>--%>
                </tr>
            </c:forEach>
        </table>
    </div>
    </c:if>
    <div class="parag">
        <p><a href="${exampleBaseUrl}/">Back to your activities</a></p>
    </div>
</layout:shell>