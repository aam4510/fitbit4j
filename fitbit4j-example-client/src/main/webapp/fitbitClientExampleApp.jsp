<%@ page import="com.fitbit.api.common.model.units.UnitSystem" %>
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
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>Fitbit Client Example Application</title>
    <link href="ui/css/include.css" rel="stylesheet" type="text/css" />
</head>
<%--
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

                        <p><a href="${exampleBaseUrl}?expireResourceCredentials=">Expire your cached Fitbit token credentials.</a> (You can re-authorize afterwards.)</p>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="parag left">
                <h3>This application's Fitbit API Rate Limit Status</h3>
            </div>
            &lt;%&ndash;@elvariable id="ipRateLimitStatus" type="com.fitbit.api.model.ApiRateLimitStatus"&ndash;%&gt;
            &lt;%&ndash;@elvariable id="clientAndUserRateLimitStatus" type="com.fitbit.api.model.ApiRateLimitStatus"&ndash;%&gt;
            <div class="parag left">
                <div class="columns">
                    <div class="column">
                        <div class="line">Rate Limit Type</div>
                        <div class="line normal">IP-ADDRESS:</div>
                        <div class="line normal">CLIENT+OWNER</div>
                    </div>
                    <div class="column">
                        <div class="line">Remaining Hits</div>
                        <div class="line normal">${ipRateLimitStatus.remainingHits}</div>
                        <div class="line normal">${!empty clientAndUserRateLimitStatus ? clientAndUserRateLimitStatus.remainingHits : "N/A"}</div>
                    </div>
                    <div class="column">
                        <div class="line">Hourly Limit</div>
                        <div class="line normal">${ipRateLimitStatus.hourlyLimit}</div>
                        <div class="line normal">${!empty clientAndUserRateLimitStatus ? clientAndUserRateLimitStatus.hourlyLimit : "N/A"}</div>
                    </div>
                    <div class="column">
                        <div class="line">Reset Time</div>
                        <div class="line normal">${ipRateLimitStatus.resetTime}</div>
                        <div class="line normal">${!empty clientAndUserRateLimitStatus ? clientAndUserRateLimitStatus.resetTime : "N/A"}</div>
                    </div>
                </div>
            </div>
            <div class="hr"></div>
            <h2>Example User Actions:</h2>

            <div class="parag left normal">
                <p><a href="#">Expire your cached Fitbit token credentials</a>. (You can re-authorize afterwards.)</p>

                <p><a href="#">Subscribe this application to your activity and food streams</a> (subscription status
                    tracked locally)</p>

                <p class="left"><i>If subscribed, the Fitbit server will notify this example application when you change
                    your data on fitbit.com.</i></p>
            </div>
            <h2>Example Global Actions:</h2>

            <div class="parag left normal">
                <p><a href="#">View all of this application's subscriptions and received notifications</a> (local
                    transient data)</p>

                <p class="left"><i>This application maintains a list of activity stream subscriptions, and the last time
                    each was updated. This list is transient and may reset periodically.</i></p>
            </div>
        </div>
    </div>
</body>
--%>






    <body>
        <a name="top"></a>
    <div id="main">
    <div id="logoStrip" class="clearfix">
            <div id="logo" style="margin-top: 4px;">
                <a href="http://local.fitbit.com:8080/example/app">
                    <img src="images/logo.gif"/>
                </a>
            </div>
            <div id="navigationNone">&nbsp;</div>
        </div>
        <div id="content">
            <div id="contentHead">
                <div id="pageMessage"></div>
            </div>
            
            <div id="contentBody" class="clearfix">
                <h1>Fitbit Client Example Application</h1>
                <div style="margin-bottom: 15px"></div>
                    
                <p>Your local user ID in this application is: ${actionBean.ourUser.userId}</p>
                   
                <div style="padding: 20px;">
                    <c:choose>
                    <c:when test="${!isAuthorized}">
                        <div style="margin-bottom: 3px">Status: <span style="font-weight: bold">UNAUTHORIZED</span></div>
                        <a href="${exampleBaseUrl}?authorize=">Authorize this application on Fitbit.</a>
                    </c:when>
                    <c:otherwise>
                        <div style="margin-bottom: 3px">Status: <span style="font-weight: bold">AUTHORIZED</span>
                            (as user <a href="${userProfileURL}">${encodedUserId})</a></div>
                        <a href="${exampleBaseUrl}?expireResourceCredentials=">Expire your cached Fitbit token credentials.</a> (You can re-authorize afterwards.)
                    </c:otherwise>
                    </c:choose>
                    <div style="margin-bottom: 20px"></div>
                    <div><h3>This application's Fitbit API Rate Limit Status</h3></div>
                    <div style="margin-bottom: 10px"></div>
                    <div id="rateLimitStatus" class="clearfix">
                        <%--@elvariable id="ipRateLimitStatus" type="com.fitbit.api.model.ApiRateLimitStatus"--%>
                        <%--@elvariable id="clientAndUserRateLimitStatus" type="com.fitbit.api.model.ApiRateLimitStatus"--%>
                        <div class="total">
                            <div class="label">
                                <div class="substance">Rate Limit Type</div>
                                <div class="amount">IP-ADDRESS:</div>
                                <div class="amount">CLIENT+OWNER:</div>
                            </div>
                        </div>
                        <div class="total">
                            <div class="label">
                                <div class="substance">Remaining Hits</div>
                                <div class="amount">${ipRateLimitStatus.remainingHits}</div>
                                <div class="amount">
                                    <c:if test="${!empty clientAndUserRateLimitStatus}">${clientAndUserRateLimitStatus.remainingHits}</c:if>
                                    <c:if test="${empty clientAndUserRateLimitStatus}">N/A</c:if>
                                </div>
                            </div>
                        </div>
                        <div class="total">
                            <div class="label">
                                <div class="substance">Hourly Limit</div>
                                <div class="amount">${ipRateLimitStatus.hourlyLimit}</div>
                                <div class="amount">
                                    <c:if test="${!empty clientAndUserRateLimitStatus}">${clientAndUserRateLimitStatus.hourlyLimit}</c:if>
                                    <c:if test="${empty clientAndUserRateLimitStatus}">N/A</c:if>
                                </div>
                             </div>
                        </div>
                        <div class="total">
                            <div class="label">
                                <div class="substance">Reset Time</div>
                                <div class="amount">${ipRateLimitStatus.resetTime}</div>
                                <div class="amount">
                                    <c:if test="${!empty clientAndUserRateLimitStatus}">${clientAndUserRateLimitStatus.resetTime}</c:if>
                                    <c:if test="${empty clientAndUserRateLimitStatus}">N/A</c:if>
                                </div>
                             </div>
                        </div>
                    </div>
                </div>
                <div style="margin-bottom: 15px"></div>
                <%--@elvariable id="unitSystem" type="com.fitbit.api.common.model.units.UnitSystem"--%>
                <c:choose>
                    <c:when test="${not showSubscriptions}">
                        <c:choose>
                            <c:when test="${!isAuthorized}">
                            </c:when>
                            <c:otherwise>
                                <div style="margin-bottom: 20px"></div>
                                
                                <c:if test="${!empty activities}">
                                    <div><h2>Your activities recorded on Fitbit</h2></div>
                                    <div style="margin-bottom: 5px"></div>
                                    <h3>Summary</h3>
                                    <div style="margin-bottom: 3px"></div>
                                    <div id="dailyTotals" class="clearfix">
                                        <div class="total">
                                            <div class="label">
                                                <div class="substance">Calories Out</div>
                                                <div class="amount">${activities.summary.caloriesOut}</div>
                                            </div>
                                        </div>
                                        <div class="total">
                                            <div class="label">
                                                <div class="substance">Active Score</div>
                                                <div class="amount">${activities.summary.activeScore}</div>
                                            </div>
                                        </div>
                                        <div class="total">
                                            <div class="label">
                                                <div class="substance">Steps</div>
                                                <div class="amount">${activities.summary.steps}</div>
                                            </div>
                                        </div>
                                        <div class="total">
                                            <div class="label">
                                                <div class="substance">Sedentary Minutes</div>
                                                <div class="amount">${activities.summary.sedentaryMinutes}</div>
                                            </div>
                                        </div>
                                        <div class="total">
                                            <div class="label">
                                                <div class="substance">Lightly Active Minutes</div>
                                                <div class="amount">${activities.summary.lightlyActiveMinutes}</div>
                                            </div>
                                        </div>
                                        <div class="total">
                                            <div class="label">
                                                <div class="substance">Fairly Active Minutes</div>
                                                <div class="amount">${activities.summary.fairlyActiveMinutes}</div>
                                            </div>
                                        </div>
                                        <div class="total">
                                            <div class="label">
                                                <div class="substance">Very Active Minutes</div>
                                                <div class="amount">${activities.summary.veryActiveMinutes}</div>
                                            </div>
                                        </div>
                                    </div>
                                    <div style="margin-bottom: 5px"></div>
                                    <c:forEach items="${activities.summary.distances}" var="distance">
                                    <%--@elvariable id="distance" type="com.fitbit.api.common.model.activities.ActivityDistance"--%>
                                        <div>${distance.activity} Distance:
                                            ${distance.distance} ${unitSystem.distanceUnits.text}</div>
                                    </c:forEach>
                                    <c:if test="${!empty activities.activities}">
                                        <div style="padding: 5px 5px;"></div>
                                        <div><h3>Logged Activities:</h3></div>
                                        <c:forEach items="${activities.activities}" var="activityLog"><%--@elvariable id="activityLog" type="com.fitbit.api.common.model.activities.ActivityLog"--%>
                                            <div>${activityLog.name}</div>
                                            <div>Distance: ${activityLog.distance}</div>
                                            <div>Distance: ${activityLog.duration}</div>
                                            <div>Calories: ${activityLog.calories}</div>
                                            <div>Favorite?: ${activityLog.favorite}</div>
                                        </c:forEach>
                                    </c:if>
                                </c:if>
                                <div style="margin-bottom: 20px"></div>
                                <c:if test="${!empty foods}">
                                    <div><h2>Your foods recorded on Fitbit</h2></div>
                                    <div style="margin-bottom: 5px"></div>
                                    <h3>Summary</h3>
                                    <div style="margin-bottom: 3px"></div>
                                    <div id="dailyTotals" class="clearfix">
                                        <div class="total">
                                            <div class="label">
                                                <div class="substance">Calories</div>
                                                <div class="amount"><fmt:formatNumber value="${foods.summary.calories}" pattern="#####"/></div>
                                            </div>
                                        </div>
                                        <div class="total">
                                            <div class="label">
                                                <div class="substance">Fat</div>
                                                <div class="amount"><fmt:formatNumber value="${foods.summary.fat}" pattern="####.#"/> <span class="unit">g</span></div>
                                            </div>
                                        </div>
                                        <div class="total">
                                            <div class="label">
                                                <div class="substance">Fiber</div>
                                                <div class="amount"><fmt:formatNumber value="${foods.summary.fiber}" pattern="####.#"/> <span class="unit">g</span></div>
                                            </div>
                                        </div>
                                        <div class="total">
                                            <div class="label">
                                                <div class="substance">Carbs</div>
                                                <div class="amount"><fmt:formatNumber value="${foods.summary.carbs}" pattern="####.#"/> <span class="unit">g</span></div>
                                            </div>
                                        </div>
                                        <div class="total">
                                            <div class="label">
                                                <div class="substance">Sodium</div>
                                                <div class="amount"><fmt:formatNumber value="${foods.summary.sodium}" pattern="####.#"/> <span class="unit">mg</span></div>
                                            </div>
                                        </div>
                                        <div class="total">
                                            <div class="label">
                                                <div class="substance">Protein</div>
                                                <div class="amount"><fmt:formatNumber value="${foods.summary.protein}" pattern="####.#"/> <span class="unit">g</span></div>
                                            </div>
                                        </div>
                                        <div class="total" style="margin-right: 0;">
                                            <div class="label">
                                                <div class="substance">Water</div>
                                                <div class="amount"><span id="waterAmount"><fmt:formatNumber value="${foods.summary.water}" pattern="####.#"/></span> <span id="waterAmountUnit" class="unit">${unitSystem.volumeUnits.text}</span></div>
                                            </div>
                                        </div>
                                    </div>
                                    <div style="margin-bottom: 5px"></div>
                                    <c:if test="${!empty foods.foods}">
                                        <div style="padding: 5px 5px;"></div>
                                        <div><h3>Logged Foods:</h3></div>
                                        <c:forEach items="${foods.foods}" var="foodLog"><%--@elvariable id="foodLog" type="com.fitbit.api.common.model.foods.FoodLog"--%>
                                            <div>${foodLog.loggedFood.name}</div>
                                            <div>Amount: ${foodLog.loggedFood.amount} ${foodLog.loggedFood.amount != 0.0 ? foodLog.loggedFood.unit.plural : foodLog.loggedFood.unit.name}</div>
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

                            </c:otherwise>
                        </c:choose>
                    </c:when>
                    <c:otherwise>
                    <h2>Complete list of subscriptions in this application</h2>
                    <p style="padding: 10px;">
                     This application uses your local user ID to identify your subscription with Fitbit.
                     <c:choose>
                       <c:when test="${isSubscribed}">
                         Your subscription is highlighted.
                       </c:when> 
                       <c:otherwise>
                         You are presently not subscribed.
                       </c:otherwise>
                     </c:choose>
                    </p>
	                  <table width="90%" id="subscriptionList" border="0">
	                    <thead>
	                      <tr>
	                        <td width="13%">Subscription ID</td>
	                        <td width="13%">Fitbit Owner Type</td>
	                        <td width="13%">Fitbit Owner ID</td>
	                        <td width="13%">Feed type</td>
	                        <td width="19%">Last Notification</td>
	                        <td width="19%">Notes</td>
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
	                              <fmt:formatDate 
	                                  value="${subscription.lastUpdateNotificationDate}" 
	                                  type="date" 
	                                  pattern="yyyy-MM-dd HH:mm:ss"
	                              />	                                   
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

                    <p style="padding: 10px;">
                       <a href="/">Back to your activities</a>
                    </p>

                    </c:otherwise>
                </c:choose>

                <div style="margin-bottom: 20px"></div>
                <hr size="1"/>
                <div style="margin-bottom: 20px"></div>

                <h2>Example User Actions:</h2>
                <dl>
                    <dt><a href="${exampleBaseUrl}?expireResourceCredentials=">Expire your cached Fitbit token credentials.</a> (You can re-authorize afterwards.)</dt>
                    <dd></dd>
                    <c:choose>
                        <c:when test="${not isSubscribed}">
                            <dt><a href="${exampleBaseUrl}?subscribe=">Subscribe this application to your activity and food
                                streams</a> (subscription status tracked locally)
                            </dt>
                        </c:when>
                        <c:otherwise>
                            <dt><a href="${exampleBaseUrl}?unsubscribe=">Unsubscribe this application from your activity and food
                                streams</a> (subscription status tracked locally)
                            </dt>
                        </c:otherwise>
                    </c:choose>
                    <dd> If subscribed, the Fitbit server will notify this example application
                        when you change your data on fitbit.com.
                    </dd>
                </dl>

                <div style="padding: 5px 5px;"></div>
                <h2>Example Global Actions:</h2>
                <dl>
                    <dt><a href="${exampleBaseUrl}?allSubscriptions=">View all of this application's subscriptions
                        and received notifications</a> (local transient data)
                    </dt>
                    <dd> This application maintains a list of activity stream subscriptions,
                        and the last time each was updated. This list is transient and may reset
                        periodically.
                    </dd>

                        <%--                  <dt> Unsubscribe from all of this application's subscriptions </dt>
                                          <dd> </dd>
                        --%>
                </dl>
            </div>
        </div>
    </div>
    </body>
</html>