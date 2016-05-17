package cz.csas.appmenu.applications;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cz.csas.appmenu.AppInformation;
import cz.csas.appmenu.AppMenuTest;
import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.judge.Constants;
import cz.csas.cscore.judge.JudgeUtils;

import static junit.framework.TestCase.assertEquals;

/**
 * The type Parameters list test.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 16.05.16.
 */
public class RegisterForCallbackTest extends AppMenuTest {
    private final String X_JUDGE_CASE = "android.appmenu.parameters.list";
    private CountDownLatch mAppMenuSignal;
    private AppInformation appInformation1;
    private AppInformation appInformation2;
    private AppInformation appInformation3;


    @Override
    public void setUp() {
        super.setUp();
        mAppMenuSignal = new CountDownLatch(3);
        JudgeUtils.setJudge(mJudgeClient, X_JUDGE_CASE, mXJudgeSessionHeader);
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
        mAppMenuClient.setGlobalHeaders(headers);
    }

    /**
     * Test parameters list.
     */
    @Test
    public void testRegisterForCallback() {

        mAppMenuManager.registerAppInformationObtainedCallback("test1", new CallbackWebApi<AppInformation>() {
            @Override
            public void success(AppInformation appInformation) {
                appInformation1 = appInformation;
                mAppMenuSignal.countDown();
            }

            @Override
            public void failure(CsSDKError error) {
                mAppMenuSignal.countDown();
            }
        });

        mAppMenuManager.registerAppInformationObtainedCallback("test2", new CallbackWebApi<AppInformation>() {
            @Override
            public void success(AppInformation appInformation) {
                appInformation2 = appInformation;
                mAppMenuSignal.countDown();
            }

            @Override
            public void failure(CsSDKError error) {
                mAppMenuSignal.countDown();
            }
        });

        mAppMenuManager.getAppInformation(0L, new CallbackWebApi<AppInformation>() {
            @Override
            public void success(AppInformation appInformation) {
                appInformation3 = appInformation;
                mAppMenuSignal.countDown();
            }

            @Override
            public void failure(CsSDKError error) {
                mAppMenuSignal.countDown();
            }
        });

        try {
            mAppMenuSignal.await(20, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(appInformation1, appInformation2);
        assertEquals(appInformation2, appInformation3);
        assertEquals(appInformation1, appInformation3);
    }


}
