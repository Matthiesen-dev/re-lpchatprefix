package dev.matthiesen.common.relpchatprefix.util;

import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibApiMetricsManager;
import dev.matthiesen.common.matthiesen_lib_api.core.metric.UniversalMetricContext;
import dev.matthiesen.common.relpchatprefix.Constants;
import dev.matthiesen.libs.faststats.ErrorTracker;

public class MetricManager {
    public static final ErrorTracker ERROR_TRACKER = MatthiesenLibApiMetricsManager.getErrorTracker();
    @SuppressWarnings("unused")
    private static final UniversalMetricContext metricContext = MatthiesenLibApi.makeErrorMetricsContext(
            Constants.MOD_ID,
            Constants.METRICS_TOKEN,
            ERROR_TRACKER
    );

    public static void init() {
        MatthiesenLibApi.registerModToApiMetrics(Constants.MOD_ID);
    }
}
