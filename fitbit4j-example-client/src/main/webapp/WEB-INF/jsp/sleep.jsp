<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layout" uri="http://fitbit.com/api/layout" %>

<%--@elvariable id="messages" type="java.util.List"--%>
<%--@elvariable id="exampleBaseUrl" type="java.lang.String"--%>
<%--@elvariable id="userId" type="java.lang.String"--%>
<%--@elvariable id="date" type="java.lang.String"--%>
<%--@elvariable id="logDate" type="java.lang.String"--%>
<%--@elvariable id="startTime" type="java.lang.String"--%>
<%--@elvariable id="duration" type="java.lang.Long"--%>
<%--@elvariable id="sleepLogId" type="java.lang.Long"--%>
<%--@elvariable id="sleep" type="com.fitbit.api.common.model.sleep.Sleep"--%>

<layout:shell>
    <h2>Example of Sleep callbacks:</h2>

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

    <form method="post" action="${exampleBaseUrl}/getSleepLogs">
        <div class="parag left">
            <p>Get sleep for defined user.</p>
            <div class="columns">
                <div class="column">
                    <div class="line" style="padding:10px;">User ID (dash for current authorized user):</div>
                    <div class="line" style="padding:10px;">Date (yyyy-MM-dd):</div>
                </div>
                <div class="column">
                    <div class="line"><input name="userId" type="text" value="${userId}"/></div>
                    <div class="line"><input name="date" type="text" value="${date}"/></div>
                </div>
            </div>
            <div class="columns">
                <div class="column">
                    <div class="line"><input type="submit" value="Get Sleep Logs"/></div>
                </div>
            </div>
        </div>
    </form>
    <c:if test="${sleep != null}">
        <div class="parag">
            <div class="columns">
                <div class="column">
                    <div class="line">Total Sleep Records</div>
                    <div class="line red">${sleep.summary.totalSleepRecords}</div>
                </div>
                <div class="column">
                    <div class="line">Total minutes asleep</div>
                    <div class="line red">${sleep.summary.totalMinutesAsleep}</div>
                </div>
                <div class="column">
                    <div class="line">Total time in bed</div>
                    <div class="line red">${sleep.summary.totalTimeInBed}</div>
                </div>
            </div>
        </div>
        <c:if test="${!empty sleep.sleepLogs}">
            <h2>Sleep Logs:</h2>
            <c:forEach items="${sleep.sleepLogs}" var="sleepLog">
                <%--@elvariable id="sleepLog" type="com.fitbit.api.common.model.sleep.SleepLog"--%>
                <h3>${activityLog.name}</h3>

                <div>LogId: ${sleepLog.logId}</div>
                <div>Start time: ${sleepLog.startTime}</div>
                <div>Main sleep?: ${sleepLog.mainSleep}</div>
                <div>Duration: ${sleepLog.duration}</div>
                <div>Minutes to fall asleep: ${sleepLog.minutesToFallAsleep}</div>
                <div>Minutes asleep: ${sleepLog.minutesAsleep}</div>
                <div>Minutes awake: ${sleepLog.minutesAwake}</div>
                <div>Minutes after wakeup: ${sleepLog.minutesAfterWakeup}</div>
                <div>Awakenings count: ${sleepLog.awakeningsCount}</div>
                <div>Time in bed: ${sleepLog.timeInBed}</div>
            </c:forEach>
        </c:if>
    </c:if>
    <div class="hr"></div>

    <form method="post" action="${exampleBaseUrl}/logSleep">
        <div class="parag left">
            <p>Log sleep for current user.</p>
            <div class="columns">
                <div class="column">
                    <div class="line" style="padding:10px;">Date (yyyy-MM-dd):</div>
                    <div class="line" style="padding:10px;">Start time (HH:mm):</div>
                    <div class="line" style="padding:10px;">Duration (ms):</div>
                </div>
                <div class="column">
                    <div class="line"><input name="logDate" type="text" value="${logDate}"/></div>
                    <div class="line"><input name="startTime" type="text" value="${startTime}"/></div>
                    <div class="line"><input name="duration" type="text" value="${duration}"/></div>
                </div>
            </div>
            <div class="columns">
                <div class="column">
                    <div class="line"><input type="submit" value="Log sleep"/></div>
                </div>
            </div>
        </div>
    </form>

    <div class="hr"></div>

    <form method="post" action="${exampleBaseUrl}/deleteSleepLog">
        <div class="parag left">
            <p>Delete sleep log.</p>
            <div class="columns">
                <div class="column">
                    <div class="line" style="padding:10px;">Sleep Log ID:</div>
                </div>
                <div class="column">
                    <div class="line"><input name="sleepLogId" type="text" value="${sleepLogId}"/></div>
                </div>
                <div class="column">
                    <div class="line"><input type="submit" value="Delete sleep log"/></div>
                </div>
            </div>
        </div>
    </form>

</layout:shell>