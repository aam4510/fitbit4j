<%@page pageEncoding="UTF-8" %>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layout" uri="http://fitbit.com/api/layout" %>
<layout:shell>
    <%--@elvariable id="unitSystem" type="com.fitbit.api.common.model.units.UnitSystem"--%>
    <%--@elvariable id="userInfo" type="com.fitbit.api.common.model.user.UserInfo"--%>

    <c:if test="${isAuthorized}">
        <h2>Profile details</h2>

        <form method="post" action="${exampleBaseUrl}/profile">
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
                        <div class="line" style="padding:10px;">Full name:</div>
                        <div class="line" style="padding:10px;">Nickname:</div>
                        <div class="line" style="padding:10px;">Gender:</div>
                        <div class="line" style="padding:10px;">Birthday:</div>
                        <div class="line" style="padding:10px;">Height:</div>
                        <div class="line" style="padding:10px;">Weight:</div>
                        <div class="line" style="padding:10px;">Timezone:</div>
                    </div>
                    <div class="column">
                        <div class="line"><input name="fullName" type="text" value="${userInfo.fullName}"/></div>
                        <div class="line"><input name="nickname" type="text" value="${userInfo.nickname}"/></div>
                        <div class="line"><input name="gender" type="text" value="${userInfo.gender}"/></div>
                        <div class="line"><input name="dateOfBirth" type="text" value="${userInfo.dateOfBirth}"/></div>
                        <div class="line"><input name="height" type="text" value="${userInfo.height}"/></div>
                        <div class="line"><input name="weight" type="text" value="${userInfo.weight}"/></div>
                        <div class="line"><input name="timezone" type="text" value="${userInfo.timezone}"/></div>
                    </div>
                </div>
                <div class="columns">
                    <div class="column">
                        <div class="line"><input type="submit" value="Update Profile"/></div>
                    </div>
                </div>
            </div>
        </form>
    </c:if>
    <div class="parag">
        <p><a href="${exampleBaseUrl}/">Back to your activities</a></p>
    </div>
</layout:shell>