package cz.csas.appmenu.applications;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cz.csas.appmenu.AppMenuTest;
import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.judge.Constants;
import cz.csas.cscore.judge.JudgeUtils;

import static junit.framework.Assert.assertEquals;

/**
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 16.05.16.
 */
public class ApplicationListTest extends AppMenuTest {
    private final String X_JUDGE_CASE = "appmenu.checkAppVersion";
    private CountDownLatch mAppMenuSignal;
    private ApplicationListResponse mApplicationListResponse;

    @Override
    public void setUp() {
        super.setUp();
        mAppMenuSignal = new CountDownLatch(1);
        JudgeUtils.setJudge(mJudgeClient, X_JUDGE_CASE, mXJudgeSessionHeader);
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
        mAppMenuClient.setGlobalHeaders(headers);
    }

    @Test
    public void testApplicationList() {
        ApplicationListParameters parameters = new ApplicationListParameters(null);

        mAppMenuClient.getAppMenuResource().withId("queueing").list(parameters, new CallbackWebApi<ApplicationListResponse>() {
            @Override
            public void success(ApplicationListResponse applicationListResponse) {
                mApplicationListResponse = applicationListResponse;
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

        List<cz.csas.appmenu.Application> applications = mApplicationListResponse.getApplications();
        assertEquals(Long.valueOf(0), mApplicationListResponse.getPageNumber());
        assertEquals(Long.valueOf(1436), mApplicationListResponse.getPageCount());
        assertEquals(Long.valueOf(1), mApplicationListResponse.getNextPage());
        assertEquals(Long.valueOf(3), mApplicationListResponse.getPageSize());



    }
}
