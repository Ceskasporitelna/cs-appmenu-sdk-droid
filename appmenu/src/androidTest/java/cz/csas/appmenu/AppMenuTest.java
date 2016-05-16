package cz.csas.appmenu;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.runner.RunWith;

import java.util.UUID;

import cz.csas.cscore.Environment;
import cz.csas.cscore.client.WebApiConfiguration;
import cz.csas.cscore.client.WebApiConfigurationImpl;
import cz.csas.cscore.judge.JudgeClient;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
@RunWith(AndroidJUnit4.class)
public abstract class AppMenuTest {
    private final String TEST_BASE_URL_OAUTH = "http://csas-judge.herokuapp.com/widp/oauth2";
    private final String TEST_BASE_URL = "http://csas-judge.herokuapp.com";
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
     * Set up.
     */
    @Before
    public void setUp(){
        mWebApiConfiguration = new WebApiConfigurationImpl(WEB_API_KEY_TEST,new Environment(TEST_BASE_URL,TEST_BASE_URL_OAUTH,false),"cs-CZ",null);
        mXJudgeSessionHeader = UUID.randomUUID().toString();
        mJudgeClient = new JudgeClient(TEST_BASE_URL);
        mAppMenuClient = AppMenu.getInstance().init(mWebApiConfiguration).getAppMenuClient();
    }
}