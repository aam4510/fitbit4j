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
<div class="parag left">
    <h3>This application's Fitbit API Rate Limit Status</h3>
</div>
<%--@elvariable id="ipRateLimitStatus" type="com.fitbit.api.model.ApiRateLimitStatus"--%>
<%--@elvariable id="clientAndUserRateLimitStatus" type="com.fitbit.api.model.ApiRateLimitStatus"--%>
<div class="parag left">
    <div class="columns">
        <div class="column">
            <div class="line">Rate Limit Type</div>
            <div class="line red">IP-ADDRESS:</div>
            <div class="line red">CLIENT+OWNER</div>
        </div>
        <div class="column">
            <div class="line">Remaining Hits</div>
            <div class="line red">${ipRateLimitStatus.remainingHits}</div>
            <div class="line red">${!empty clientAndUserRateLimitStatus ? clientAndUserRateLimitStatus.remainingHits : "N/A"}</div>
        </div>
        <div class="column">
            <div class="line">Hourly Limit</div>
            <div class="line red">${ipRateLimitStatus.hourlyLimit}</div>
            <div class="line red">${!empty clientAndUserRateLimitStatus ? clientAndUserRateLimitStatus.hourlyLimit : "N/A"}</div>
        </div>
        <div class="column">
            <div class="line">Reset Time</div>
            <div class="line red">${ipRateLimitStatus.resetTime}</div>
            <div class="line red">${!empty clientAndUserRateLimitStatus ? clientAndUserRateLimitStatus.resetTime : "N/A"}</div>
        </div>
    </div>
</div>


<c:if test="${isAuthorized}">
    <%--@elvariable id="unitSystem" type="com.fitbit.api.common.model.units.UnitSystem"--%>
    <h2>Your activities recorded on Fitbit</h2>
    <c:if test="${!empty activities}">
        <div class="parag">
            <div class="columns">
                <div class="column">
                    <div class="line">Calories Out</div>
                    <div class="line red">${activities.summary.caloriesOut}</div>
                </div>
                <div class="column">
                    <div class="line">Active Score</div>
                    <div class="line red">${activities.summary.activeScore}</div>
                </div>
                <div class="column">
                    <div class="line">Steps</div>
                    <div class="line red">${activities.summary.steps}</div>
                </div>
                <div class="column">
                    <div class="line">Sedentary Minutes</div>
                    <div class="line red">${activities.summary.sedentaryMinutes}</div>
                </div>
                <div class="column">
                    <div class="line">Lightly Active Minutes</div>
                    <div class="line red">${activities.summary.lightlyActiveMinutes}</div>
                </div>
                <div class="column">
                    <div class="line">Fairly Active Minutes</div>
                    <div class="line red">${activities.summary.fairlyActiveMinutes}</div>
                </div>
                <div class="column">
                    <div class="line">Very Active Minutes</div>
                    <div class="line red">${activities.summary.veryActiveMinutes}</div>
                </div>
            </div>
        </div>
        <div class="parag">
            <c:forEach items="${activities.summary.distances}" var="distance">
                <%--@elvariable id="distance" type="com.fitbit.api.common.model.activities.ActivityDistance"--%>
                <p>${distance.activity} Distance: <b>${distance.distance}</b> ${unitSystem.distanceUnits.text}</p>
            </c:forEach>
        </div>

        <c:if test="${!empty activities.activities}">
            <h2>Logged Activities:</h2>
            <c:forEach items="${activities.activities}" var="activityLog">
                <%--@elvariable id="activityLog" type="com.fitbit.api.common.model.activities.ActivityLog"--%>
                <h3>${activityLog.name}</h3>

                <div>Distance: ${activityLog.distance}</div>
                <div>Distance: ${activityLog.duration}</div>
                <div>Calories: ${activityLog.calories}</div>
                <div>Favorite?: ${activityLog.favorite}</div>
            </c:forEach>
        </c:if>

    </c:if>
    <c:if test="${!empty foods}">
        <h2>Your foods recorded on Fitbit</h2>

        <h3>Summary</h3>

        <div class="parag">
            <div class="columns">
                <div class="column">
                    <div class="line">Calories</div>
                    <div class="line red"><fmt:formatNumber value="${foods.summary.calories}" pattern="#####"/></div>
                </div>
                <div class="column">
                    <div class="line">Fat</div>
                    <div class="line red"><fmt:formatNumber value="${foods.summary.fat}" pattern="####.#"/>
                        <span>g</span></div>
                </div>
                <div class="column">
                    <div class="line">Fiber</div>
                    <div class="line red"><fmt:formatNumber value="${foods.summary.fiber}" pattern="####.#"/>
                        <span>g</span></div>
                </div>
                <div class="column">
                    <div class="line">Carbs</div>
                    <div class="line red"><fmt:formatNumber value="${foods.summary.carbs}" pattern="####.#"/>
                        <span>g</span></div>
                </div>
                <div class="column">
                    <div class="line">Sodium</div>
                    <div class="line red"><fmt:formatNumber value="${foods.summary.sodium}" pattern="####.#"/>
                        <span>mg</span></div>
                </div>
                <div class="column">
                    <div class="line">Protein</div>
                    <div class="line red"><fmt:formatNumber value="${foods.summary.protein}" pattern="####.#"/>
                        <span>g</span></div>
                </div>
                <div class="column">
                    <div class="line">Water</div>
                    <div class="line red"><fmt:formatNumber value="${foods.summary.water}" pattern="####.#"/>
                        <span>${unitSystem.volumeUnits.text}</span></div>
                </div>
            </div>
        </div>
        <c:if test="${!empty foods.foods}">
            <h2>Logged Foods:</h2>
            <c:forEach items="${foods.foods}" var="foodLog">
                <%--@elvariable id="foodLog" type="com.fitbit.api.common.model.foods.FoodLog"--%>
                <h3>${foodLog.loggedFood.name}</h3>

                <div>
                    Amount: ${foodLog.loggedFood.amount} ${foodLog.loggedFood.amount != 0.0 ? foodLog.loggedFood.unit.plural : foodLog.loggedFood.unit.name}</div>
                <div>Calories: ${foodLog.nutritionalValues.calories}</div>
                <div>Fat: ${foodLog.nutritionalValues.fat}</div>
                <div>Fiber: ${foodLog.nutritionalValues.fiber}</div>
                <div>Carbs: ${foodLog.nutritionalValues.carbs}</div>
                <div>Sodium: ${foodLog.nutritionalValues.sodium}</div>
                <div>Protein: ${foodLog.nutritionalValues.protein}</div>
                <div>Favorite?: ${foodLog.favorite}</div>
                <div>FoodAccessLevel : ${foodLog.loggedFood.accessLevel}</div>
            </c:forEach>
        </c:if>
    </c:if>
</c:if>
</layout:shell>