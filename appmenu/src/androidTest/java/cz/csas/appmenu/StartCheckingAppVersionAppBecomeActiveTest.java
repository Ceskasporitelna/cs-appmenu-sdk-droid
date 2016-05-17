package cz.csas.appmenu;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.judge.Constants;
import cz.csas.cscore.judge.JudgeUtils;

import static junit.framework.TestCase.assertEquals;

/**
 * The type Start checking app version app become active test.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 16.05.16.
 */
public class StartCheckingAppVersionAppBecomeActiveTest extends AppMenuTest {
    private final String X_JUDGE_CASE = "android.appmenu.checkAppVersion";
    private CountDownLatch mAppMenuSignal1;
    private AppVersion mAppVersion;
    private AppVersion mAppVersion2;
    private boolean flag = true;


    @Override
    public void setUp() {
        super.setUp();
        mAppMenuSignal1 = new CountDownLatch(2);
        JudgeUtils.setJudge(mJudgeClient, X_JUDGE_CASE, mXJudgeSessionHeader);
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
        mAppMenuClient.setGlobalHeaders(headers);
    }

    /**
     * Test start checking app version app become active.
     */
    @Test
    public void testStartCheckingAppVersionAppBecomeActive() {
        ((AppMenuManagerImpl) mAppMenuManager).setTestCurrentAppVersion(new AppVersion(0, 0));

        mAppMenuManager.setCheckForVersionInterval(-1);
        mAppMenuManager.startCheckingAppVersion(new AppIsOutdatedCallback() {
            @Override
            public void success(AppItem appItem) {
                if (flag) {
                    flag = false;
                    mAppVersion = appItem.appVersion();
                    mAppMenuSignal1.countDown();
                    mAppMenuManager.getAppForegroundListener().onApplicationOnForeground();
                } else {
                    mAppVersion2 = appItem.appVersion();
                    mAppMenuSignal1.countDown();
                }
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

        assertEquals(1, mAppVersion.getMajor());
        assertEquals(1, mAppVersion.getMinor());
        assertEquals(1, mAppVersion2.getMajor());
        assertEquals(1, mAppVersion2.getMinor());
    }


}
