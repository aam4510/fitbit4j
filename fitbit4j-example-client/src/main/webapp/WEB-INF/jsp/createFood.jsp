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
<%--@elvariable id="totalFat" type="java.lang.Float"--%>
<%--@elvariable id="transFat" type="java.lang.Float"--%>
<%--@elvariable id="saturatedFat" type="java.lang.Float"--%>
<%--@elvariable id="cholesterol" type="java.lang.Float"--%>
<%--@elvariable id="sodium" type="java.lang.Float"--%>
<%--@elvariable id="potassium" type="java.lang.Float"--%>
<%--@elvariable id="totalCarbohydrate" type="java.lang.Float"--%>
<%--@elvariable id="dietaryFiber" type="java.lang.Float"--%>
<%--@elvariable id="sugars" type="java.lang.Float"--%>
<%--@elvariable id="protein" type="java.lang.Float"--%>
<%--@elvariable id="vitaminA" type="java.lang.Float"--%>
<%--@elvariable id="vitaminC" type="java.lang.Float"--%>
<%--@elvariable id="iron" type="java.lang.Float"--%>
<%--@elvariable id="calcium" type="java.lang.Float"--%>
<%--@elvariable id="thiamin" type="java.lang.Float"--%>
<%--@elvariable id="riboflavin" type="java.lang.Float"--%>
<%--@elvariable id="vitaminB6" type="java.lang.Float"--%>
<%--@elvariable id="vitaminB12" type="java.lang.Float"--%>
<%--@elvariable id="vitaminE" type="java.lang.Float"--%>
<%--@elvariable id="folicAcid" type="java.lang.Float"--%>
<%--@elvariable id="niacin" type="java.lang.Float"--%>
<%--@elvariable id="magnesium" type="java.lang.Float"--%>
<%--@elvariable id="phosphorus" type="java.lang.Float"--%>
<%--@elvariable id="iodine" type="java.lang.Float"--%>
<%--@elvariable id="zinc" type="java.lang.Float"--%>
<%--@elvariable id="copper" type="java.lang.Float"--%>
<%--@elvariable id="biotin" type="java.lang.Float"--%>
<%--@elvariable id="pantothenicAcid" type="java.lang.Float"--%>
<%--@elvariable id="vitaminD" type="java.lang.Float"--%>

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
                    <div class="line" style="padding:10px;">Calories from Fat(optional):</div>
                    <div class="line" style="padding:10px;">Total Fat(optional):</div>
                    <div class="line" style="padding:10px;">Trans Fat(optional):</div>
                    <div class="line" style="padding:10px;">Saturated Fat (optional):</div>
                    <div class="line" style="padding:10px;">Cholesterol (optional):</div>
                    <div class="line" style="padding:10px;">Sodium (optional):</div>
                    <div class="line" style="padding:10px;">Potassium (optional):</div>
                    <div class="line" style="padding:10px;">Total Carbohydrate (optional):</div>
                    <div class="line" style="padding:10px;">Dietary Fiber (optional):</div>
                    <div class="line" style="padding:10px;">Sugars (optional):</div>
                    <div class="line" style="padding:10px;">Protein (optional):</div>
                    <div class="line" style="padding:10px;">Vitamin A (optional):</div>
                    <div class="line" style="padding:10px;">Vitamin C (optional):</div>
                    <div class="line" style="padding:10px;">Iron (optional):</div>
                    <div class="line" style="padding:10px;">Calcium (optional):</div>
                    <div class="line" style="padding:10px;">Thiamin (optional):</div>
                    <div class="line" style="padding:10px;">Riboflavin (optional):</div>
                    <div class="line" style="padding:10px;">Vitamin B6 (optional):</div>
                    <div class="line" style="padding:10px;">Vitamin B12 (optional):</div>
                    <div class="line" style="padding:10px;">Vitamin E (optional):</div>
                    <div class="line" style="padding:10px;">Folic Acid (optional):</div>
                    <div class="line" style="padding:10px;">Niacin (optional):</div>
                    <div class="line" style="padding:10px;">Magnesium (optional):</div>
                    <div class="line" style="padding:10px;">Phosphorus (optional):</div>
                    <div class="line" style="padding:10px;">Iodine (optional):</div>
                    <div class="line" style="padding:10px;">Zinc (optional):</div>
                    <div class="line" style="padding:10px;">Copper (optional):</div>
                    <div class="line" style="padding:10px;">Biotin (optional):</div>
                    <div class="line" style="padding:10px;">Pantothenic Acid (optional):</div>
                    <div class="line" style="padding:10px;">Vitamin D (optional):</div>
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
                    <div class="line"><input name="caloriesFromFat" type="text" value="${caloriesFromFat}"/></div>
                    <div class="line"><input name="totalFat" type="text" value="${totalFat}"/></div>
                    <div class="line"><input name="transFat" type="text" value="${transFat}"/></div>
                    <div class="line"><input name="saturatedFat" type="text" value="${saturatedFat}"/></div>
                    <div class="line"><input name="cholesterol" type="text" value="${cholesterol}"/></div>
                    <div class="line"><input name="sodium" type="text" value="${sodium}"/></div>
                    <div class="line"><input name="potassium" type="text" value="${potassium}"/></div>
                    <div class="line"><input name="totalCarbohydrate" type="text" value="${totalCarbohydrate}"/></div>
                    <div class="line"><input name="dietaryFiber" type="text" value="${dietaryFiber}"/></div>
                    <div class="line"><input name="sugars" type="text" value="${sugars}"/></div>
                    <div class="line"><input name="protein" type="text" value="${protein}"/></div>
                    <div class="line"><input name="vitaminA" type="text" value="${vitaminA}"/></div>
                    <div class="line"><input name="vitaminC" type="text" value="${vitaminC}"/></div>
                    <div class="line"><input name="iron" type="text" value="${iron}"/></div>
                    <div class="line"><input name="calcium" type="text" value="${calcium}"/></div>
                    <div class="line"><input name="thiamin" type="text" value="${thiamin}"/></div>
                    <div class="line"><input name="riboflavin" type="text" value="${riboflavin}"/></div>
                    <div class="line"><input name="vitaminB6" type="text" value="${vitaminB6}"/></div>
                    <div class="line"><input name="vitaminB12" type="text" value="${vitaminB12}"/></div>
                    <div class="line"><input name="vitaminE" type="text" value="${vitaminE}"/></div>
                    <div class="line"><input name="folicAcid" type="text" value="${folicAcid}"/></div>
                    <div class="line"><input name="niacin" type="text" value="${niacin}"/></div>
                    <div class="line"><input name="magnesium" type="text" value="${magnesium}"/></div>
                    <div class="line"><input name="phosphorus" type="text" value="${phosphorus}"/></div>
                    <div class="line"><input name="iodine" type="text" value="${iodine}"/></div>
                    <div class="line"><input name="zinc" type="text" value="${zinc}"/></div>
                    <div class="line"><input name="copper" type="text" value="${copper}"/></div>
                    <div class="line"><input name="biotin" type="text" value="${biotin}"/></div>
                    <div class="line"><input name="pantothenicAcid" type="text" value="${pantothenicAcid}"/></div>
                    <div class="line"><input name="vitaminD" type="text" value="${vitaminD}"/></div>
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