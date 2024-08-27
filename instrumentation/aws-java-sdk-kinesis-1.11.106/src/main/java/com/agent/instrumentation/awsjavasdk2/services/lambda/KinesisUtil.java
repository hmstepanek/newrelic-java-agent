package com.agent.instrumentation.awsjavasdk2.services.lambda;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.handlers.AsyncHandler_Instrumentation;
import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.api.agent.CloudParameters;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Segment;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.TracedMethod;

public class KinesisUtil {

    public static final String PLATFORM = "aws_kinesis_data_streams";
    public static final String TRACE_CATEGORY = "Kinesis";
    private KinesisUtil() {}

    public static void setupToken(AsyncHandler_Instrumentation asyncHandler, AmazonWebServiceRequest request) {
        if (AgentBridge.getAgent().getTransaction(false) != null) {
            if (asyncHandler != null) {
                asyncHandler.token = NewRelic.getAgent().getTransaction().getToken();
            }
            if (request != null) {
                request.token = NewRelic.getAgent().getTransaction().getToken();
            }
        }
    }

    public static void linkAndExpireToken(AmazonWebServiceRequest request) {
        if (request != null && request.token != null) {
            request.token.linkAndExpire();
            request.token = null;
        }
    }

    public static void setTraceDetails(String kinesisOperation) {
        TracedMethod tracedMethod = NewRelic.getAgent().getTracedMethod();
        tracedMethod.setMetricName(TRACE_CATEGORY, kinesisOperation);
        tracedMethod.reportAsExternal(createCloudParams());
    }

    public static CloudParameters createCloudParams() {
        // Todo: add arn to cloud parameters
        return CloudParameters.provider(PLATFORM).build();
    }

}
