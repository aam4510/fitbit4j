<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layout" uri="http://fitbit.com/api/layout" %>

<%--@elvariable id="messages" type="java.util.List"--%>
<%--@elvariable id="exampleBaseUrl" type="java.lang.String"--%>
<%--@elvariable id="name" type="java.lang.String"--%>
<%--@elvariable id="description" type="java.lang.String"--%>
<%--@elvariable id="defaultFoodMeasurementUnitId" type="java.lang.Long"--%>
<%--@elvariable id="defaultServingSize" type="java.lang.Float"--%>
<%--@elvariable id="caloriesPerServingSize" type="java.lang.Integer"--%>
<%--@elvariable id="caloriesFromFat" type="java.lang.Integer"--%>

<layout:shell>
    <h2>Create new food:</h2>

    <form method="post" action="${exampleBaseUrl}/createFood">

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
                    <div class="line" style="padding:10px;">Food name (required):</div>
                    <div class="line" style="padding:10px;">Food description (optional):</div>
                    <div class="line" style="padding:10px;">Default Food Measurement Unit Id (required):</div>
                    <div class="line" style="padding:10px;">Default Serving Size (required) :</div>
                    <div class="line" style="padding:5px;">Food form type (optional): </div>
                    <div class="line" style="padding:10px;">Calories per Serving Size(required) :</div>
                </div>
                <div class="column">
                    <div class="line"><input name="name" type="text" value="${name}"/></div>
                    <div class="line"><input name="description" type="text" value="${description}"/></div>
                    <div class="line"><input name="defaultFoodMeasurementUnitId" type="text" value="${defaultFoodMeasurementUnitId}"/></div>
                    <div class="line"><input name="defaultServingSize" type="text" value="${defaultServingSize}"/></div>
                    <div class="line">
                        <select name="formType">
                            <option value="NA">NA</option>
                            <option value="LIQUID">LIQUID</option>
                            <option value="DRY">DRY</option>
                        </select>
                    </div>
                    <div class="line"><input name="caloriesPerServingSize" type="text" value="${caloriesPerServingSize}"/></div>
                </div>
            </div>
            <div class="columns">
                <div class="column">
                    <div class="line"><input type="submit" value="Create Food"/></div>
                </div>
            </div>
        </div>
    </form>
</layout:shell>