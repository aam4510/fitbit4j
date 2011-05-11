<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--@elvariable id="activities" type="com.fitbit.api.common.model.activities.Activities"--%>
<%--@elvariable id="foods" type="com.fitbit.api.common.model.foods.Foods"--%>
<%--@elvariable id="showSubscriptions" type="java.lang.Boolean"--%>
<%--@elvariable id="isAuthorized" type="java.lang.Boolean"--%>
<%--@elvariable id="isSubscribed" type="java.lang.Boolean"--%>
<%--@elvariable id="exampleBaseUrl" type="java.lang.String"--%>
<%--@elvariable id="encodedUserId" type="java.lang.String"--%>
<%--@elvariable id="userProfileURL" type="java.lang.String"--%>
<%--@elvariable id="showAccountRegistrationForm" type="java.lang.Boolean"--%>
<%--@elvariable id="messages" type="java.util.List"--%>
<%--@elvariable id="email" type="java.lang.String"--%>
<%--@elvariable id="password" type="java.lang.String"--%>
<%--@elvariable id="timezone" type="java.lang.String"--%>
<%--@elvariable id="emailSubscribe" type="java.lang.Boolean"--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Fitbit Client Example Application</title>
    <link href="ui/css/include.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<div class="wrap">
<div class="logo"></div>
<div class="content">
<h1>Fitbit Client Example Application</h1>

<div class="parag">
    <h3>Your local user ID in this application is: ${actionBean.ourUser.userId}</h3>
</div>
<div class="parag left">
    <c:choose>
        <c:when test="${!isAuthorized}">
            <p>Status: <b class="uppercase">UNAUTHORIZED</b></p>

            <p><a href="${exampleBaseUrl}?authorize=">Authorize this application on Fitbit.</a></p>
        </c:when>
        <c:otherwise>
            <p>Status: <b class="uppercase">AUTHORIZED</b>
                (as user <a href="${userProfileURL}">${encodedUserId})</a>
            </p>

            <p><a href="${exampleBaseUrl}?expireResourceCredentials=">Expire your cached Fitbit token
                credentials.</a> (You can re-authorize afterwards.)</p>
        </c:otherwise>
    </c:choose>
</div>
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


<c:if test="${isAuthorized && !showSubscriptions}">
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
            </c:forEach>
        </c:if>
    </c:if>
</c:if>

<c:if test="${isAuthorized && showSubscriptions}">
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
                <th width="19%">Last Notification</th>
                <th width="19%">Notes</th>
            </tr>
            </thead>
            <c:forEach items="${subscriptions}" var="subscription">
                <tr
                        <c:if test="${actionBean.ourUser.userId == subscription.subscriptionDetail.subscriptionId}">
                            style="background-color: lightyellow; font-weight: bold;"
                        </c:if>
                        >
                    <td>${subscription.subscriptionDetail.subscriptionId}</td>
                    <td>${subscription.subscriptionDetail.owner.resourceOwnerType}</td>
                    <td>${subscription.subscriptionDetail.owner.id}</td>
                    <td>${subscription.subscriptionDetail.collectionType}</td>
                    <td>
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
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
    <div class="parag">
        <p><a href="${exampleBaseUrl}">Back to your activities</a></p>
    </div>
</c:if>

<div class="hr"></div>
<h2>Example User Actions:</h2>

<div class="parag left normal">
    <p><a href="${exampleBaseUrl}?expireResourceCredentials=">Expire your cached Fitbit token credentials</a>. (You can
        re-authorize afterwards.)</p>

    <c:choose>
        <c:when test="${!isSubscribed}">
            <p><a href="${exampleBaseUrl}?subscribe=">Subscribe this application to your activity and food streams</a>
                (subscription status tracked locally)</p>
        </c:when>
        <c:otherwise>
            <p><a href="${exampleBaseUrl}?unsubscribe=">Unsubscribe this application from your activity and food streams</a>
                (subscription status tracked locally)</p>
        </c:otherwise>
    </c:choose>

    <p class="left"><i>If subscribed, the Fitbit server will notify this example application when you change
        your data on fitbit.com.</i></p>
</div>
<h2>Example Global Actions:</h2>

<div class="parag left normal">
    <p><a href="${exampleBaseUrl}?allSubscriptions=">View all of this application's subscriptions and received
        notifications</a> (local
        transient data)</p>

    <p class="left"><i>This application maintains a list of activity stream subscriptions, and the last time
        each was updated. This list is transient and may reset periodically.</i></p>
</div>

<c:if test="${showAccountRegistrationForm}">
    <div class="hr"></div>
    <h2>Example of account registration:</h2>
    <form method="post" action="${exampleBaseUrl}">

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
                    <div class="line"><input name="emailSubscribe" type="checkbox" ${emailSubscribe ? 'checked' : ''}/></div>
                </div>
            </div>
            <div class="columns">
                <div class="column">
                    <div class="line"><input type="submit" value="Register Account"/></div>
                </div>
            </div>
        </div>
    </form>
    <div class="hr"></div>
</c:if>

</div>
</div>
</body>
</html>