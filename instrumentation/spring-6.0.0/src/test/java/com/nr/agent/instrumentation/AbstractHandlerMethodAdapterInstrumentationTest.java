package com.nr.agent.instrumentation;

import com.newrelic.agent.bridge.Agent;
import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.agent.bridge.TracedMethod;
import com.newrelic.agent.bridge.Transaction;
import com.newrelic.agent.bridge.TransactionNamePriority;
import com.newrelic.api.agent.Logger;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;

import java.util.logging.Level;

import static org.mockito.Mockito.*;

public class AbstractHandlerMethodAdapterInstrumentationTest {
    Agent originalAgent = AgentBridge.getAgent();
    Agent mockAgent = mock(Agent.class);
    Logger mockLogger = mock(Logger.class);
    TracedMethod mockTracedMethod = mock(TracedMethod.class);


    @Before
    public void before() {
        AgentBridge.agent = mockAgent;
        when(mockAgent.getLogger()).thenReturn(mockLogger);
        when(mockLogger.isLoggable(Level.FINEST)).thenReturn(false);
    }

    @After
    public void after() {
        AgentBridge.agent = originalAgent;
    }

    @Test
    public void handleInternal_findsAnnotationsFromInterfaceAndMethod() throws Exception {
        AbstractHandlerMethodAdapter_Instrumentation cut = new AbstractHandlerMethodAdapter_Instrumentation();
        HandlerMethod handlerMethod = new HandlerMethod(new ControllerClassWithInterface(), ControllerClassWithInterface.class.getMethod("get"));

        HttpServletRequest mockReq = mock(HttpServletRequest.class);
        HttpServletResponse mockResp = mock(HttpServletResponse.class);
        Transaction mockTxn = mock(Transaction.class);

        when(mockAgent.getTransaction(false)).thenReturn(mockTxn);
        when(mockTxn.getTracedMethod()).thenReturn(mockTracedMethod);
        when(mockReq.getMethod()).thenReturn("GET");

        cut.handleInternal(mockReq, mockResp, handlerMethod);

        verify(mockTxn).getTracedMethod();
        verify(mockTxn).setTransactionName(TransactionNamePriority.FRAMEWORK_HIGH, false, "SpringController", "/root/get (GET)");
        verify(mockTracedMethod).setMetricName("Java", "com.nr.agent.instrumentation.AbstractHandlerMethodAdapterInstrumentationTest$ControllerClassWithInterface/get");
    }

    @Test
    public void handleInternal_findsAnnotationsWithUrlParamFromInterfaceAndMethod() throws Exception {
        AbstractHandlerMethodAdapter_Instrumentation cut = new AbstractHandlerMethodAdapter_Instrumentation();
        HandlerMethod handlerMethod = new HandlerMethod(new ControllerClassWithInterface(), ControllerClassWithInterface.class.getMethod("getParam"));

        HttpServletRequest mockReq = mock(HttpServletRequest.class);
        HttpServletResponse mockResp = mock(HttpServletResponse.class);
        Transaction mockTxn = mock(Transaction.class);

        when(mockAgent.getTransaction(false)).thenReturn(mockTxn);
        when(mockTxn.getTracedMethod()).thenReturn(mockTracedMethod);
        when(mockReq.getMethod()).thenReturn("GET");

        cut.handleInternal(mockReq, mockResp, handlerMethod);

        verify(mockTxn).getTracedMethod();
        verify(mockTxn).setTransactionName(TransactionNamePriority.FRAMEWORK_HIGH, false, "SpringController", "/root/get/{id} (GET)");
        verify(mockTracedMethod).setMetricName("Java", "com.nr.agent.instrumentation.AbstractHandlerMethodAdapterInstrumentationTest$ControllerClassWithInterface/getParam");

    }

    @Test
    public void handleInternal_findsAnnotationsWithoutInterface_withRequestMappings() throws Exception {
        AbstractHandlerMethodAdapter_Instrumentation cut = new AbstractHandlerMethodAdapter_Instrumentation();
        HandlerMethod handlerMethod = new HandlerMethod(new ControllerNoInterfaceWithRequestMappings(),
                ControllerNoInterfaceWithRequestMappings.class.getMethod("get"));

        HttpServletRequest mockReq = mock(HttpServletRequest.class);
        HttpServletResponse mockResp = mock(HttpServletResponse.class);
        Transaction mockTxn = mock(Transaction.class);

        when(mockAgent.getTransaction(false)).thenReturn(mockTxn);
        when(mockTxn.getTracedMethod()).thenReturn(mockTracedMethod);
        when(mockReq.getMethod()).thenReturn("GET");

        cut.handleInternal(mockReq, mockResp, handlerMethod);

        verify(mockTxn).getTracedMethod();
        verify(mockTxn).setTransactionName(TransactionNamePriority.FRAMEWORK_HIGH, false, "SpringController", "/root/get (GET)");
        verify(mockTracedMethod).setMetricName("Java", "com.nr.agent.instrumentation.AbstractHandlerMethodAdapterInstrumentationTest$ControllerNoInterfaceWithRequestMappings/get");
    }

    @Test
    public void handleInternal_findsAnnotationsWithoutInterface_withUrlParam_withRequestMappings() throws Exception {
        AbstractHandlerMethodAdapter_Instrumentation cut = new AbstractHandlerMethodAdapter_Instrumentation();
        HandlerMethod handlerMethod = new HandlerMethod(new ControllerNoInterfaceWithRequestMappings(),
                ControllerNoInterfaceWithRequestMappings.class.getMethod("getParam"));

        HttpServletRequest mockReq = mock(HttpServletRequest.class);
        HttpServletResponse mockResp = mock(HttpServletResponse.class);
        Transaction mockTxn = mock(Transaction.class);

        when(mockAgent.getTransaction(false)).thenReturn(mockTxn);
        when(mockTxn.getTracedMethod()).thenReturn(mockTracedMethod);
        when(mockReq.getMethod()).thenReturn("GET");

        cut.handleInternal(mockReq, mockResp, handlerMethod);

        verify(mockTxn).getTracedMethod();
        verify(mockTxn).setTransactionName(TransactionNamePriority.FRAMEWORK_HIGH, false, "SpringController", "/root/get/{id} (GET)");
        verify(mockTracedMethod).setMetricName("Java", "com.nr.agent.instrumentation.AbstractHandlerMethodAdapterInstrumentationTest$ControllerNoInterfaceWithRequestMappings/getParam");
    }

    @Test
    public void handleInternal_findsAnnotationsWithoutInterface_withPostMappings() throws Exception {
        AbstractHandlerMethodAdapter_Instrumentation cut = new AbstractHandlerMethodAdapter_Instrumentation();
        HandlerMethod handlerMethod = new HandlerMethod(new ControllerNoInterfaceGetPostMappings(),
                ControllerNoInterfaceGetPostMappings.class.getMethod("post"));

        HttpServletRequest mockReq = mock(HttpServletRequest.class);
        HttpServletResponse mockResp = mock(HttpServletResponse.class);
        Transaction mockTxn = mock(Transaction.class);

        when(mockAgent.getTransaction(false)).thenReturn(mockTxn);
        when(mockTxn.getTracedMethod()).thenReturn(mockTracedMethod);
        when(mockReq.getMethod()).thenReturn("POST");

        cut.handleInternal(mockReq, mockResp, handlerMethod);

        verify(mockTxn).getTracedMethod();
        verify(mockTxn).setTransactionName(TransactionNamePriority.FRAMEWORK_HIGH, false, "SpringController", "/post (POST)");
        verify(mockTracedMethod).setMetricName("Java", "com.nr.agent.instrumentation.AbstractHandlerMethodAdapterInstrumentationTest$ControllerNoInterfaceGetPostMappings/post");
    }

    @Test
    public void handleInternal_namesTxnBasedOnControllerClassAndMethod_whenNoAnnotationPresent() throws Exception {
        AbstractHandlerMethodAdapter_Instrumentation cut = new AbstractHandlerMethodAdapter_Instrumentation();
        HandlerMethod handlerMethod = new HandlerMethod(new NoAnnotationController(),
                NoAnnotationController.class.getMethod("get"));

        HttpServletRequest mockReq = mock(HttpServletRequest.class);
        HttpServletResponse mockResp = mock(HttpServletResponse.class);
        Transaction mockTxn = mock(Transaction.class);

        when(mockAgent.getTransaction(false)).thenReturn(mockTxn);
        when(mockTxn.getTracedMethod()).thenReturn(mockTracedMethod);
        when(mockReq.getMethod()).thenReturn("GET");

        cut.handleInternal(mockReq, mockResp, handlerMethod);

        verify(mockTxn).getTracedMethod();
        verify(mockTxn).setTransactionName(TransactionNamePriority.FRAMEWORK_HIGH, false, "SpringController",
                "/NoAnnotationController/get");
        verify(mockTracedMethod).setMetricName("Java", "com.nr.agent.instrumentation.AbstractHandlerMethodAdapterInstrumentationTest$NoAnnotationController/get");
    }

    @Test
    public void handleInternal_namesTxnBasedOnControllerClassAndMethod_whenExtendingAbstractController() throws Exception {
        AbstractHandlerMethodAdapter_Instrumentation cut = new AbstractHandlerMethodAdapter_Instrumentation();
        HandlerMethod handlerMethod = new HandlerMethod(new ControllerExtendingAbstractClass(),
                ControllerExtendingAbstractClass.class.getMethod("extend"));

        HttpServletRequest mockReq = mock(HttpServletRequest.class);
        HttpServletResponse mockResp = mock(HttpServletResponse.class);
        Transaction mockTxn = mock(Transaction.class);

        when(mockAgent.getTransaction(false)).thenReturn(mockTxn);
        when(mockTxn.getTracedMethod()).thenReturn(mockTracedMethod);
        when(mockReq.getMethod()).thenReturn("GET");

        cut.handleInternal(mockReq, mockResp, handlerMethod);

        verify(mockTxn).getTracedMethod();
        verify(mockTxn).setTransactionName(TransactionNamePriority.FRAMEWORK_HIGH, false, "SpringController",
                "/extend (GET)");
        verify(mockTracedMethod).setMetricName("Java", "com.nr.agent.instrumentation.AbstractHandlerMethodAdapterInstrumentationTest$ControllerExtendingAbstractClass/extend");
    }

    //Interfaces/classes used to test various mapping annotation scenarios
    @RequestMapping("/root")
    public interface ControllerInterface {
        @GetMapping("/get")
        void get();

        @PostMapping("/post")
        void post();

        @DeleteMapping("delete")
        void delete();

        @RequestMapping("/req")
        void req();

        @GetMapping("/get/{id}")
        void getParam();
    }

    static class ControllerClassWithInterface implements ControllerInterface {

        @Override
        public void get() {}

        @Override
        public void post() {}

        @Override
        public void delete() {}

        @Override
        public void req() {}

        @Override
        public void getParam() {}
    }

    @RequestMapping("/root")
    static class ControllerNoInterfaceWithRequestMappings {
        @RequestMapping("/get")
        public void get() {}

        @RequestMapping("/get/{id}")
        public void getParam() {}
    }

    static class ControllerNoInterfaceGetPostMappings {
        @GetMapping("/get")
        public void get() {}

        @GetMapping("/get/{id}")
        public void getParam() {}

        @PostMapping("/post")
        public void post() {}
    }

    static class NoAnnotationController {
        public void get() {}
    }

    static abstract class ControllerToExtend {
        @GetMapping("/extend")
        abstract public String extend();
    }

    static class ControllerExtendingAbstractClass extends ControllerToExtend {
        public String extend() {
            return "extend";
        }
    }
}
