<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layout" uri="http://fitbit.com/api/layout" %>
<%--@elvariable id="activities" type="com.fitbit.api.common.model.activities.Activities"--%>
<%--@elvariable id="foods" type="com.fitbit.api.common.model.foods.Foods"--%>
<%--@elvariable id="showSubscriptions" type="java.lang.Boolean"--%>
<%--@elvariable id="isAuthorized" type="java.lang.Boolean"--%>
<%--@elvariable id="isSubscribed" type="java.lang.Boolean"--%>
<%--@elvariable id="exampleBaseUrl" type="java.lang.String"--%>
<%--@elvariable id="encodedUserId" type="java.lang.String"--%>
<%--@elvariable id="userProfileURL" type="java.lang.String"--%>
<layout:shell>
    <h2>Example of account registration:</h2>

    <form method="post" action="${exampleBaseUrl}/register">

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
                    <div class="line" style="padding:10px;">Email:</div>
                    <div class="line" style="padding:10px;">Password:</div>
                    <div class="line" style="padding:10px;">Timezone(RFC 822 ex:-0200) :</div>
                    <div class="line" style="padding:10px;">Mail subscription :</div>
                </div>
                <div class="column">
                    <div class="line"><input name="email" type="text" value="${email}"/></div>
                    <div class="line"><input name="password" type="password" value="${password}"/></div>
                    <div class="line"><input name="timezone" type="text" value="${timezone}"/></div>
                    <div class="line"><input name="emailSubscribe" type="checkbox" ${emailSubscribe ? 'checked' : ''}/>
                    </div>
                </div>
            </div>
            <div class="columns">
                <div class="column">
                    <div class="line"><input type="submit" value="Register Account"/></div>
                </div>
            </div>
        </div>
    </form>
</layout:shell>