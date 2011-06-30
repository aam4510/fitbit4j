package com.fitbit.api.common.model.timeseries;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class IntradaySummary {

    private Data summary;
    private IntradayDataset intradayDataset;

    public IntradaySummary(Data summary, IntradayDataset intradayDataset) {
        this.summary = summary;
        this.intradayDataset = intradayDataset;
    }

    public Data getSummary() {
        return summary;
    }

    public IntradayDataset getIntradayDataset() {
        return intradayDataset;
    }
}
