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
 * The type Parameters list test.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 16.05.16.
 */
public class StartCheckingAppVersionTest extends AppMenuTest {
    private final String X_JUDGE_CASE = "android.appmenu.parameters.list";
    private CountDownLatch mAppMenuSignal1;
    private AppVersion mAppVersion;
    private CsAppMenuError mError;


    @Override
    public void setUp() {
        super.setUp();
        mAppMenuSignal1 = new CountDownLatch(1);
        JudgeUtils.setJudge(mJudgeClient, X_JUDGE_CASE, mXJudgeSessionHeader);
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.XJUDGE_SESSION_HEADER_NAME, mXJudgeSessionHeader);
        mAppMenuClient.setGlobalHeaders(headers);
    }

    /**
     * Test parameters list.
     */
    @Test
    public void testResigterForCallback() {
        ((AppMenuManagerImpl) mAppMenuManager).setTestCurrentAppVersion(new AppVersion(0, 0));
        mAppMenuManager.startCheckingAppVersion(new AppIsOutdatedCallback() {
            @Override
            public void success(AppItem appItem) {
                mAppVersion = appItem.appVersion();
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

        try {
            mAppMenuManager.startCheckingAppVersion(new AppIsOutdatedCallback() {
                @Override
                public void success(AppItem appItem) {
                }

                @Override
                public void failure(CsSDKError error) {
                }
            });
        } catch (CsAppMenuError error) {
            mError = error;
        }

        assertEquals("You called checkingAppVersion for the second time! You can call it just once!", mError.getMessage());
        assertEquals(1, mAppVersion.getMajor());
        assertEquals(1, mAppVersion.getMinor());
    }


}
