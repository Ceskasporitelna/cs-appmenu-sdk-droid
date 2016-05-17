package cz.csas.appmenu.applications;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cz.csas.appmenu.AppMenuTest;
import cz.csas.appmenu.AppItem;
import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.judge.Constants;
import cz.csas.cscore.judge.JudgeUtils;

import static junit.framework.Assert.assertEquals;

/**
 * The type Check app version test.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 16.05.16.
 */
public class CheckAppVersionTest extends AppMenuTest {
    private final String X_JUDGE_CASE = "android.appmenu.parameters.list";
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

    /**
     * Test check app version.
     */
    @Test
    public void testCheckAppVersion() {


        mAppMenuClient.getApplicationsResource().withId("queueing").list(new CallbackWebApi<ApplicationListResponse>() {
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

        List<AppItem> appItems = mApplicationListResponse.getAppItems();

        for (int i = 0; i < appItems.size(); i++) {
            AppItem appItem = appItems.get(i);
            String categoryKey = appItem.getCategoryKey();
            switch (categoryKey) {
                case "QUEUEING":
                    assertEquals("Mate jiz nepodporovanou verzi aplikace, aktualizujte prosim.", appItem.getIncompatibleTextCS());
                    assertEquals("https://www.csas.cz/static_internet/cs/Redakce/Prezentace/Automaticky_rozbalit/Prilohy/mobileapps/mobileApps/queing/quickcheck_iconIPhone.png", appItem.getAppIconUrl());
                    assertEquals("Queueing", appItem.getAppName());
                    assertEquals("1", appItem.getMinimalVersionMinor());
                    assertEquals("This is an unsupported version of application, please update.", appItem.getIncompatibleTextEN());
                    assertEquals("1", appItem.getMinimalVersionMajor());
                    assertEquals("https://play.google.com/store/apps/details?id=com.cleverlance.csas.servis24", appItem.getGooglePlayLink());
                    assertEquals("cz.csas.queueing", appItem.getPackageName());
                    assertEquals("cz.csas.queueing://", appItem.getUrlScheme());
                    break;

                case "SERVIS_24":
                    assertEquals("https://www.csas.cz/static_internet/cs/Redakce/Prezentace/Automaticky_rozbalit/Prilohy/mobileapps/mobileApps/queing/servis24iconAndroid.png", appItem.getAppIconUrl());
                    assertEquals("SERVIS 24", appItem.getAppName());
                    assertEquals("https://play.google.com/store/apps/details?id=com.cleverlance.csas.servis24", appItem.getGooglePlayLink());
                    assertEquals("service24://", appItem.getUrlScheme());
                    assertEquals("com.cleverlance.csas.servis24", appItem.getPackageName());
                    break;
            }
        }


    }
}
