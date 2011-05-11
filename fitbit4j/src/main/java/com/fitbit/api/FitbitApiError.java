package com.fitbit.api;

import com.fitbit.api.client.http.Response;
import com.google.common.collect.Lists;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: James Park
 * Date: Jun 5, 2007
 * Time: 10:16:23 AM
 * To change this template use File | Settings | File Templates.
 */
public class FitbitApiError {
	public enum ErrorType {
		System("system"),
		Validation("validation"),
		Request("request");

		private String code;

		ErrorType(String code) {
			this.code = code;
		}

		public String toString() {
			return code;
		}
	}

	private ErrorType errorType;
    private String fieldName;
	private String message;

	public FitbitApiError(ErrorType errorType, String fieldName, String message) {
		this.errorType = errorType;
		this.message = message;
		this.fieldName = fieldName;
	}

	public FitbitApiError(ErrorType errorType, String message) {
		this.errorType = errorType;
		this.message = message;
	}

    public FitbitApiError(JSONObject res) throws FitbitAPIException {
        try {
            errorType = ErrorType.valueOf(APIUtil.capitalize(res.getString("errorType")));
            fieldName = res.getString("fieldName");
            message = res.getString("message");
        } catch (JSONException e) {
            throw new FitbitAPIException(e.getMessage() + ": " + res.toString(), e);
        }
    }

    public static List<FitbitApiError> constructFitbitApiErrorList(Response res) throws FitbitAPIException {
        if (res.isError()) {
            try {
                JSONObject jsonObject;
                //check if it's not json object but bad request with custom message
                try {
                    jsonObject = res.asJSONObject();
                } catch (FitbitAPIException e) {
                    return Lists.newArrayList(new FitbitApiError(FitbitApiError.ErrorType.Request, res.asString()));
                }
                if( jsonObject.has("errors") ) {
                    JSONArray errorArray = jsonObject.getJSONArray("errors");
                    List<FitbitApiError> activityList = new ArrayList<FitbitApiError>(errorArray.length());
                    for (int i = 0; i < errorArray.length(); i++) {
                        JSONObject activity = errorArray.getJSONObject(i);
                        activityList.add(new FitbitApiError(activity));
                    }
                    return activityList;
                }
            } catch (JSONException e) {
                throw new FitbitAPIException(e.getMessage() + ':' + res.asString(), e);
            }
        }
        return new ArrayList<FitbitApiError>(0);
    }

	public ErrorType getErrorType() {
		return errorType;
	}

	public void setErrorType(ErrorType errorType) {
		this.errorType = errorType;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
}