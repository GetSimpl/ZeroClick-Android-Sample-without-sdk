package com.simpl.pay.sample.zc_s2s;

import android.Manifest;
import android.os.Build;
import android.util.Log;

import com.simpl.android.fingerprint.SimplFingerprint;
import com.simpl.pay.sample.zc_s2s.utils.Decompression;
import com.simpl.pay.sample.zc_s2s.utils.TestUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;

import java.io.IOException;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class FingerprintTestSuite {

    private String email = "integration_test@simpl.com";
    private String phone = "2121000111";
    private String[] permissions;
    private JSONObject object;

    @Rule
    public ActivityTestRule<UserActivity> mUserActivityRule = new ActivityTestRule<>(UserActivity.class);

    @Rule
    public TestName testName = new TestName();


    @Before
    public void setup() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            switch (testName.getMethodName()) {
                case "testWithPermissions":
                    permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE};
                    break;
                default:
                    permissions = new String[]{};
            }

            for (String permission : permissions)
                InstrumentationRegistry.getInstrumentation().getUiAutomation()
                        .grantRuntimePermission(InstrumentationRegistry.getInstrumentation().getTargetContext().getPackageName(), permission);
        }
    }

    @Test
    public void testWithoutPermissions() {
        //generate fingerprint
        SimplFingerprint.init(mUserActivityRule.getActivity(), phone, email);
        SimplFingerprint.getInstance().generateFingerprint(data -> {
            try {
                object = new JSONObject(Decompression.run(data));
                Log.v("Data1", object.toString());

                assertDefaults();
                assertNotNull(object.getString("SIMPL-Ltln"));
                assertFalse(object.getString("SIMPL-Ltln").equalsIgnoreCase(""));

                assertNotNull(object.getString("SIMPL-DevId"));
                assertFalse(object.getString("SIMPL-DevId").equalsIgnoreCase(""));

                assertNotNull(object.getString("SIMPL-SSN"));
                assertFalse(object.getString("SIMPL-SSN").equalsIgnoreCase(""));
            } catch (JSONException e) {
                e.printStackTrace();
                fail();
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
        });
        TestUtils.wait(10000);
    }


    @Test
    public void testWithPermissions() {
        //generate fingerprint
        SimplFingerprint.init(mUserActivityRule.getActivity(), phone, email);
        SimplFingerprint.getInstance().generateFingerprint(data -> {
            try {
                object = new JSONObject(Decompression.run(data));
                Log.v("Data2", object.toString());

                assertDefaults();

                assertTrue(object.getString("SIMPL-Ltln").matches(TestUtils.REGEX_LOCATION));
                assertTrue(object.getString("SIMPL-DevId").length() > 0);
                assertTrue(object.getString("SIMPL-SSN").length() > 0);
            } catch (IOException ex) {
                ex.printStackTrace();
                fail();
            } catch (JSONException e) {
                e.printStackTrace();
                fail();
            }
        });
        TestUtils.wait(10000);
    }

    private void assertDefaults() {
        try {
            assertEquals(object.getString("phone_number"), phone);
            assertEquals(object.getString("email"), email);

            assertEquals(object.getString("SIMPL-PAN"), BuildConfig.APPLICATION_ID);
            assertEquals(object.getString("SIMPL-PAV"), BuildConfig.VERSION_NAME);
            assertEquals(object.getString("sdk-version"), com.simpl.android.fingerprint.BuildConfig.VERSION_NAME);
            assertEquals(object.getString("sdk"), "zcs2s");
            assertEquals(object.getString("platform"), "android");

            assertNotNull(object.getString("SIMPL-IPA"));// regex: ip address
            assertTrue(object.getString("SIMPL-IPA").matches(TestUtils.REGEX_IPADDRESS));

            assertNotNull(object.getString("SIMPL-Rt"));
            assertFalse(object.getString("SIMPL-Rt").equalsIgnoreCase(""));

            assertNotNull(object.getString("SIMPL-isR"));//boolean
            assertTrue(object.getString("SIMPL-isR").matches(TestUtils.REGEX_BOOLEAN));

            assertNotNull(object.getString("SIMPL-SeN"));
            assertFalse(object.getString("SIMPL-SeN").equalsIgnoreCase(""));

            assertNotNull(object.getString("SIMPL-Up"));//regex d+ms
            assertTrue(object.getString("SIMPL-Up").matches(TestUtils.REGEX_UP_TIME));

            assertNotNull(object.getString("SIMPL-AndId"));
            assertFalse(object.getString("SIMPL-AndId").equalsIgnoreCase(""));

            assertNotNull(object.getString("SIMPL-AdID"));//valid uuid
            assertTrue(object.getString("SIMPL-AdID").matches(TestUtils.REGEX_UUID));

            assertNotNull(object.getString("SIMPL-Amem"));//valid integer
            assertTrue(object.getString("SIMPL-Amem").matches(TestUtils.REGEX_MEMORY));

            assertNotNull(object.getString("SIMPL-BAT"));// valid integer
            assertTrue(object.getString("SIMPL-BAT").matches(TestUtils.REGEX_BATTERY));

            assertNotNull(object.getString("SIMPL-DRes"));//regex: dxdxd
            assertTrue(object.getString("SIMPL-DRes").matches(TestUtils.REGEX_DISPLAY_RES));

            assertNotNull(object.getString("SIMPL-ScrOff"));
            assertFalse(object.getString("SIMPL-ScrOff").equalsIgnoreCase(""));

            assertNotNull(object.getString("SIMPL-FontSize"));// valid integer
            assertTrue(object.getString("SIMPL-FontSize").matches(TestUtils.REGEX_FONTSIZE));

            assertNotNull(object.getString("SIMPL-ScrBrtMode"));
            assertFalse(object.getString("SIMPL-ScrBrtMode").equalsIgnoreCase(""));

            assertNotNull(object.getString("SIMPL-CaR"));
            assertFalse(object.getString("SIMPL-CaR").equalsIgnoreCase(""));

            assertNotNull(object.getString("SIMPL-DEVICE-MANUFACTURER"));
            assertFalse(object.getString("SIMPL-DEVICE-MANUFACTURER").equalsIgnoreCase(""));

            assertNotNull(object.getString("SIMPL-DEVICE-MODEL"));
            assertFalse(object.getString("SIMPL-DEVICE-MODEL").equalsIgnoreCase(""));

            assertNotNull(object.getString("SIMPL-ADisk"));// valid integer
            assertTrue(object.getString("SIMPL-ADisk").matches(TestUtils.REGEX_INTEGER));

            assertNotNull(object.getString("SIMPL-InApp"));//assert for comma seperated values
            assertTrue(object.getString("SIMPL-InApp").matches(TestUtils.REGEX_CSV));

            assertNotNull(object.getString("SIMPL-WIFI-SSID"));
            assertFalse(object.getString("SIMPL-WIFI-SSID").equalsIgnoreCase(""));

            assertNotNull(object.getString("SIMPL-CaN"));
            assertFalse(object.getString("SIMPL-CaN").equalsIgnoreCase(""));

            assertNotNull(object.getString("SIMPL-AccEm"));
            assertFalse(object.getString("SIMPL-AccEm").equalsIgnoreCase(""));

            assertNotNull(object.getString("SIMPL-WpId"));
            assertFalse(object.getString("SIMPL-WpId").equalsIgnoreCase(""));

            assertNotNull(object.getString("SIMPL-Blu"));
            assertFalse(object.getString("SIMPL-Blu").equalsIgnoreCase(""));
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        }
    }

    @After
    public void teardown() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //TODO revoking permissions crashing the app. Handle it appropriately.
//            for (String permission : permissions) {
//                try {
//                    InstrumentationRegistry.getInstrumentation().getUiAutomation()
//                            .revokeRuntimePermission(InstrumentationRegistry.getInstrumentation().getTargetContext().getPackageName(), permission);
//                } catch (SecurityException e) {
//                    e.printStackTrace();
//                }
//            }
        }
    }
}
