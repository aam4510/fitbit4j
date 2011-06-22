<%--@elvariable id="userInfo" type="com.fitbit.api.common.model.user.UserInfo"--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Simple jsp page</title></head>
<body>
<table>
    <tr>
        <td>Full Name:</td>
        <td>${userInfo.fullName}</td>
    </tr>
    <tr>
        <td>Display Name:</td>
        <td>${userInfo.displayName}</td>
    </tr>
    <tr>
        <td>Nickname:</td>
        <td>${userInfo.nickname}</td>
    </tr>
    <tr>
        <td>Gender:</td>
        <td>${userInfo.gender}</td>
    </tr>
    <tr>
        <td>Date of Birth:</td>
        <td>${userInfo.dateOfBirth}</td>
    </tr>
    <tr>
        <td>Height:</td>
        <td>${userInfo.height}</td>
    </tr>
    <tr>
        <td>Weight:</td>
        <td>${userInfo.weight}</td>
    </tr>
    <tr>
        <td>Stride Lenght Walking:</td>
        <td>${userInfo.strideLengthWalking}</td>
    </tr>
    <tr>
        <td>Stride Lenght Running:</td>
        <td>${userInfo.strideLengthRunning}</td>
    </tr>
    <tr>
        <td>City:</td>
        <td>${userInfo.city}</td>
    </tr>
    <tr>
        <td>State:</td>
        <td>${userInfo.state}</td>
    </tr>
    <tr>
        <td>Country:</td>
        <td>${userInfo.country}</td>
    </tr>
    <tr>
        <td>Timezone:</td>
        <td>${userInfo.timezone}</td>
    </tr>
    <tr>
        <td>Offset From UTC (Millis):</td>
        <td>${userInfo.offsetFromUTCMillis}</td>
    </tr>
</table>
</body>
</html>