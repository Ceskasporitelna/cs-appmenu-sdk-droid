package cz.csas.appmenu.applications;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cz.csas.appmenu.AppInformation;
import cz.csas.appmenu.AppInformationSource;
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
public class GetAppInfoServerCacheTest extends AppMenuTest {
    private final String X_JUDGE_CASE = "android.appmenu.parameters.list";
    private CountDownLatch mAppMenuSignal1;
    private CountDownLatch mAppMenuSignal2;
    private AppInformation appInformation1;
    private AppInformation appInformation2;


    @Override
    public void setUp() {
        super.setUp();
        mAppMenuSignal1 = new CountDownLatch(1);
        mAppMenuSignal2 = new CountDownLatch(1);
        JudgeUtils.setJudge(mJudgeClient, X_JUDGE_CASE, mXJudgeSessionHeader);
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
        mAppMenuClient.setGlobalHeaders(headers);
    }

    /**
     * Test parameters list.
     */
    @Test
    public void testGetAppInfoServerCache() {

        mAppMenuManager.getAppInformation(0L, new CallbackWebApi<AppInformation>() {
            @Override
            public void success(AppInformation appInformation) {
                appInformation1 = appInformation;
                mAppMenuSignal1.countDown();
            }

            @Override
            public void failure(CsSDKError error) {
                mAppMenuSignal1.countDown();
            }
        });

        try {
            mAppMenuSignal1.await(20, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        JudgeUtils.setJudge(mJudgeClient, X_JUDGE_CASE, mXJudgeSessionHeader);
        mAppMenuManager.getAppInformation(1000L, new CallbackWebApi<AppInformation>() {
            @Override
            public void success(AppInformation appInformation) {
                appInformation2 = appInformation;
                mAppMenuSignal2.countDown();
            }

            @Override
            public void failure(CsSDKError error) {
                mAppMenuSignal2.countDown();
            }
        });

        try {
            mAppMenuSignal2.await(20, TimeUnit.SECONDS); // wait for callback
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(AppInformationSource.SERVER, appInformation1.getSource());
        assertEquals("Mate jiz nepodporovanou verzi aplikace, aktualizujte prosim.", appInformation1.getThisApp().getIncompatibleTextCS());
        assertEquals("https://www.csas.cz/static_internet/cs/Redakce/Prezentace/Automaticky_rozbalit/Prilohy/mobileapps/mobileApps/queing/quickcheck_iconIPhone.png", appInformation1.getThisApp().getAppIconUrl());
        assertEquals("Queueing", appInformation1.getThisApp().getAppName());
        assertEquals("1", appInformation1.getThisApp().getMinimalVersionMinor());
        assertEquals("This is an unsupported version of application, please update.", appInformation1.getThisApp().getIncompatibleTextEN());
        assertEquals("1", appInformation1.getThisApp().getMinimalVersionMajor());
        assertEquals("https://play.google.com/store/apps/details?id=com.cleverlance.csas.servis24", appInformation1.getThisApp().getGooglePlayLink());
        assertEquals("cz.csas.queueing", appInformation1.getThisApp().getPackageName());
        assertEquals("cz.csas.queueing://", appInformation1.getThisApp().getUrlScheme());

        assertEquals(AppInformationSource.CACHE, appInformation2.getSource());
        assertEquals("Mate jiz nepodporovanou verzi aplikace, aktualizujte prosim.", appInformation2.getThisApp().getIncompatibleTextCS());
        assertEquals("https://www.csas.cz/static_internet/cs/Redakce/Prezentace/Automaticky_rozbalit/Prilohy/mobileapps/mobileApps/queing/quickcheck_iconIPhone.png", appInformation2.getThisApp().getAppIconUrl());
        assertEquals("Queueing", appInformation2.getThisApp().getAppName());
        assertEquals("1", appInformation2.getThisApp().getMinimalVersionMinor());
        assertEquals("This is an unsupported version of application, please update.", appInformation2.getThisApp().getIncompatibleTextEN());
        assertEquals("1", appInformation2.getThisApp().getMinimalVersionMajor());
        assertEquals("https://play.google.com/store/apps/details?id=com.cleverlance.csas.servis24", appInformation2.getThisApp().getGooglePlayLink());
        assertEquals("cz.csas.queueing", appInformation2.getThisApp().getPackageName());
        assertEquals("cz.csas.queueing://", appInformation2.getThisApp().getUrlScheme());

    }


}
