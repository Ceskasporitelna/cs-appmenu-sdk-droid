package cz.csas.appmenu;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.runner.RunWith;

import java.util.UUID;

import cz.csas.cscore.CoreSDK;
import cz.csas.cscore.Environment;
import cz.csas.cscore.client.WebApiConfiguration;
import cz.csas.cscore.client.WebApiConfigurationImpl;
import cz.csas.cscore.judge.JudgeClient;
import cz.csas.cscore.logger.LogLevel;
import cz.csas.cscore.logger.LogManagerImpl;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
@RunWith(AndroidJUnit4.class)
public abstract class AppMenuTest {
    private final String TEST_BASE_URL_OAUTH = "http://csas-judge.herokuapp.com/widp/oauth2";
    private final String TEST_BASE_URL_JUDGE = "http://csas-judge.herokuapp.com";
    private final String TEST_BASE_URL = "http://csas-judge.herokuapp.com/webapi";
    private final String WEB_API_KEY_TEST = "TEST_API_KEY";
    /**
     * The M x judge session header.
     */
    protected String mXJudgeSessionHeader;
    /**
     * The M cs configuration.
     */
    protected WebApiConfiguration mWebApiConfiguration;
    /**
     * The M judge client.
     */
    protected JudgeClient mJudgeClient;
    /**
     * The M places client.
     */
    protected AppMenuClient mAppMenuClient;

    /**
     * The M app menu manager.
     */
    protected AppMenuManager mAppMenuManager;


    /**
     * Set up.
     */
    @Before
    public void setUp() {
        CoreSDK.getInstance().useLogger(new LogManagerImpl("FOOK_TAG", LogLevel.DETAILED_DEBUG));
        mWebApiConfiguration = new WebApiConfigurationImpl(WEB_API_KEY_TEST, new Environment(TEST_BASE_URL, TEST_BASE_URL_OAUTH, false), "cs-CZ", null);
        mXJudgeSessionHeader = UUID.randomUUID().toString();
        mJudgeClient = new JudgeClient(TEST_BASE_URL_JUDGE, new LogManagerImpl("TEST", LogLevel.DETAILED_DEBUG));
        mAppMenuClient = AppMenu.getInstance().init(mWebApiConfiguration).getAppMenuClient();
        mAppMenuManager = new AppMenuManagerImpl("queueing", "QUEUEING", InstrumentationRegistry.getTargetContext());
        ((AppMenuManagerImpl) mAppMenuManager).setAppMenuClient(mAppMenuClient);
        ((AppMenuManagerImpl) mAppMenuManager).clearCache();
    }
}