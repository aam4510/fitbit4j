package com.fitbit.api.common.model.units;

import java.util.Locale;

public enum UnitSystem {
    METRIC(DurationUnits.MS, DistanceUnits.METRIC, HeightUnits.CM, WeightUnits.KG, MeasurementUnits.CM, VolumeUnits.ML),
    UK(DurationUnits.MS, DistanceUnits.METRIC, HeightUnits.CM, WeightUnits.STONE, MeasurementUnits.CM, VolumeUnits.ML),
    US(DurationUnits.MS, DistanceUnits.US, HeightUnits.INCHES, WeightUnits.POUNDS, MeasurementUnits.INCHES, VolumeUnits.FL_OZ);

    DurationUnits durationUnits;
    DistanceUnits distanceUnits;
    HeightUnits heightUnits;
    WeightUnits weightUnits;
    MeasurementUnits measurementUnits;
    VolumeUnits volumeUnits;

    UnitSystem(DurationUnits durationUnits, DistanceUnits distanceUnits, HeightUnits heightUnits, WeightUnits weightUnits, MeasurementUnits measurementUnits, VolumeUnits volumeUnits) {
        this.durationUnits = durationUnits;
        this.distanceUnits = distanceUnits;
        this.heightUnits = heightUnits;
        this.weightUnits = weightUnits;
        this.measurementUnits = measurementUnits;
        this.volumeUnits = volumeUnits;
    }

    public DurationUnits getDurationUnits() {
        return durationUnits;
    }

    public DistanceUnits getDistanceUnits() {
        return distanceUnits;
    }

    public HeightUnits getHeightUnits() {
        return heightUnits;
    }

    public WeightUnits getWeightUnits() {
        return weightUnits;
    }

    public MeasurementUnits getMeasurementUnits() {
        return measurementUnits;
    }

    public VolumeUnits getVolumeUnits() {
        return volumeUnits;
    }

    public static UnitSystem getUnitSystem(Locale locale) {
        if (Locale.US.equals(locale)) {
            return US;
        } else if (Locale.UK.equals(locale)) {
            return UK;
        } else {
            return METRIC;
        }
    }
}
