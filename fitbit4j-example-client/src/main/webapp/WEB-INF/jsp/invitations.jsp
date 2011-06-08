<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layout" uri="http://fitbit.com/api/layout" %>

<%--@elvariable id="messages" type="java.util.List"--%>
<%--@elvariable id="exampleBaseUrl" type="java.lang.String"--%>
<%--@elvariable id="invitationMethod" type="java.lang.String"--%>
<%--@elvariable id="invitedUserIdOrEMail" type="java.lang.String"--%>
<%--@elvariable id="senderUserId" type="java.lang.String"--%>

<layout:shell>
    <h2>Example of Send Invitation and Accept Invitation:</h2>

    <c:if test="${not empty messages}">
        <div class="columns">
            <div class="column">
                <c:forEach items="${messages}" var="message">
                    <div class="line red">${message}</div>
                </c:forEach>
            </div>
        </div>
    </c:if>

    <div class="hr"></div>

    <form method="post" action="${exampleBaseUrl}/sendInvitation">
        <div class="parag left">
            <p>Send Invitation</p>
            <div class="columns">
                <div class="column">
                    <div class="line" style="padding:10px;">Invitation method:</div>
                    <div class="line" style="padding:10px;">UserId or Email (depends on method):</div>
                </div>
                <div class="column">
                    <div class="line">
                        <select name="invitationMethod">
                            <option value="USER" ${invitationMethod eq 'USER' ? 'selected' : ''}>User Id</option>
                            <option value="EMAIL" ${invitationMethod eq 'EMAIL' ? 'selected' : ''}>Email</option>
                        </select>
                    </div>
                    <div class="line"><input name="invitedUserIdOrEMail" type="text" value="${invitedUserIdOrEMail}"/></div>
                </div>
            </div>
            <div class="columns">
                <div class="column">
                    <div class="line"><input type="submit" value="Send Invitation"/></div>
                </div>
            </div>
        </div>
    </form>

    <div class="hr"></div>

    <form method="post" action="${exampleBaseUrl}/acceptInvitation">
        <div class="parag left">
            <p>Accept Invitation</p>
            <div class="columns">
                <div class="column">
                    <div class="line" style="padding:10px;">UserId:</div>
                </div>
                <div class="column">
                    <div class="line"><input name="senderUserId" type="text" value="${senderUserId}"/></div>
                </div>
            </div>
            <div class="columns">
                <div class="column">
                    <div class="line"><input type="submit" value="Accept Invitation"/></div>
                </div>
            </div>
        </div>
    </form>
    <p><a href="${exampleBaseUrl}">Back to main page</a></p>
</layout:shell>