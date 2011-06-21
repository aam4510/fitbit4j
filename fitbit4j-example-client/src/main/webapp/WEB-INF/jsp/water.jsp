<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layout" uri="http://fitbit.com/api/layout" %>
<layout:shell>
    <%--@elvariable id="unitSystem" type="com.fitbit.api.common.model.units.UnitSystem"--%>
    <%--@elvariable id="userInfo" type="com.fitbit.api.common.model.user.UserInfo"--%>

    <c:if test="${isAuthorized}">
        <h2>Water</h2>

        <div class="parag left">
        <table class="blue">
            <thead>
            <tr>
                <th>Amount</th>
                <th>&nbsp;</th>
            </tr>
            </thead>
            <c:forEach items="${water.water}" var="waterLog">
                <tr>
                    <td>${waterLog.amount}</td>
                    <td>
                        <a href="${exampleBaseUrl}/deleteWater?id=${waterLog.logId}">Delete</a>
                    </td>
                </tr>
            </c:forEach>
        </table>

        <%--@elvariable id="water" type="com.fitbit.api.common.model.foods.Water"--%>
        <div>Water summary: <span class="red"><c:out value="${water.summary.water}"/></span></div>

        <h2>Water Log</h2>

        <form method="post" action="${exampleBaseUrl}/water">
            <div class="parag left">

                <c:if test="${not empty messages}">
                    <div class="columns">
                        <div class="column">
                            <c:forEach items="${messages}" var="message">
                                <div class="line red">${message}</div>
                            </c:forEach>
                        </div>
                    </div>
                </c:if>

                <div class="columns">
                    <div class="column">
                        <div class="line" style="padding:10px;">Water amount:</div>
                    </div>
                    <div class="column">
                        <div class="line"><input name="amount" type="text" value=""/></div>
                    </div>
                </div>
                <div class="columns">
                    <div class="column">
                        <div class="line"><input type="submit" value="Log"/></div>
                    </div>
                </div>
            </div>
        </form>
    </c:if>
    <div class="parag">
        <p><a href="${exampleBaseUrl}/">Back to your activities</a></p>
    </div>
</layout:shell>