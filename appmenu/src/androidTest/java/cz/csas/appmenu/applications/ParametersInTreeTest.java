package cz.csas.appmenu.applications;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cz.csas.appmenu.AppItem;
import cz.csas.appmenu.AppMenuTest;
import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.judge.Constants;
import cz.csas.cscore.judge.JudgeUtils;

import static junit.framework.Assert.assertEquals;

/**
 * The type Parameters in tree test.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 16.05.16.
 */
public class ParametersInTreeTest extends AppMenuTest {
    private final String X_JUDGE_CASE = "android.appmenu.parametersInTree.list";
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
     * Test parameters in tree list.
     */
    @Test
    public void testParametersInTreeList() {

        mAppMenuClient.getApplicationsResource().withId("queueing")
                .getNodesResource().withId("root")
                .list(new CallbackWebApi<ApplicationListResponse>() {
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
                case "SERVIS_24":
                    assertEquals("https://www.csas.cz/static_internet/cs/Redakce/Prezentace/Automaticky_rozbalit/Prilohy/mobileapps/mobileApps/queing/servis24iconAndroid.png", appItem.getAppIconUrl());
                    assertEquals("SERVIS 24", appItem.getAppName());
                    assertEquals("com.cleverlance.csas.servis24", appItem.getPackageName());
                    assertEquals("https://play.google.com/store/apps/details?id=com.cleverlance.csas.servis24", appItem.getGooglePlayLink());
                    assertEquals("service24://", appItem.getUrlScheme());

                    assertEquals("https://www.csas.cz/static_internet/cs/Redakce/Prezentace/Automaticky_rozbalit/Prilohy/mobileapps/mobileApps/queing/servis24iconAndroid.png", appItem.getRawData().get("app_icon"));
                    assertEquals("SERVIS 24", appItem.getRawData().get("app_name"));
                    assertEquals("com.cleverlance.csas.servis24", appItem.getRawData().get("package_name"));
                    assertEquals("https://play.google.com/store/apps/details?id=com.cleverlance.csas.servis24", appItem.getRawData().get("google_play_link"));
                    assertEquals("service24://", appItem.getRawData().get("url_scheme"));
                    break;

                case "QUICKCHECK":
                    assertEquals("https://www.csas.cz/static_internet/cs/Redakce/Prezentace/Automaticky_rozbalit/Prilohy/mobileapps/mobileApps/queing/quickcheck_iconAndroid.png", appItem.getAppIconUrl());
                    assertEquals("Můj stav", appItem.getAppName());
                    assertEquals("cz.csas.app.mujstav", appItem.getPackageName());
                    assertEquals("https://play.google.com/store/apps/details?id=cz.csas.app.mujstav", appItem.getGooglePlayLink());
                    assertEquals("cz.csas.app.mujstav://", appItem.getUrlScheme());
                    assertEquals("Mate jiz nepodporovanou verzi aplikace, aktualizujte prosim.", appItem.getIncompatibleTextCS());
                    assertEquals("0", appItem.getMinimalVersionMinor());
                    assertEquals("0", appItem.getMinimalVersionMajor());
                    assertEquals("This is an unsupported version of application, please update.", appItem.getIncompatibleTextEN());
                    assertEquals("Rychlý náhled na Váš účet", appItem.getDescriptionTextCS());
                    assertEquals("EN Rychly nahled na Vas ucet", appItem.getDescriptionTextEN());

                    assertEquals("https://www.csas.cz/static_internet/cs/Redakce/Prezentace/Automaticky_rozbalit/Prilohy/mobileapps/mobileApps/queing/quickcheck_iconAndroid.png", appItem.getRawData().get("app_icon"));
                    assertEquals("Můj stav", appItem.getRawData().get("app_name"));
                    assertEquals("cz.csas.app.mujstav", appItem.getRawData().get("package_name"));
                    assertEquals("https://play.google.com/store/apps/details?id=cz.csas.app.mujstav", appItem.getRawData().get("google_play_link"));
                    assertEquals("cz.csas.app.mujstav://", appItem.getRawData().get("url_scheme"));
                    assertEquals("Mate jiz nepodporovanou verzi aplikace, aktualizujte prosim.", appItem.getRawData().get("incompatibleTextCS"));
                    assertEquals("0", appItem.getRawData().get("minimalVersionMajor"));
                    assertEquals("0", appItem.getRawData().get("minimalVersionMinor"));
                    assertEquals("This is an unsupported version of application, please update.", appItem.getRawData().get("incompatibleTextEN"));
                    assertEquals("Rychlý náhled na Váš účet", appItem.getRawData().get("descriptionTextCS"));
                    assertEquals("EN Rychly nahled na Vas ucet", appItem.getRawData().get("descriptionTextEN"));
                    break;
            }
        }
    }

}
