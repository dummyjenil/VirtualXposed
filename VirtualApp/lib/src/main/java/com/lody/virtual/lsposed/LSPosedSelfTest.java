package com.lody.virtual.lsposed;

import android.os.Build;
import com.lody.virtual.helper.utils.VLog;

import java.lang.reflect.Method;

import top.canyie.pine.Pine;
import top.canyie.pine.callback.MethodHook;

/**
 * Self-Test Suite to verify that the LSPosed / Pine hooking engine is properly working.
 */
public class LSPosedSelfTest {

    private static final String TAG = "LSPosedSelfTest";

    public static class TestReport {
        public boolean isSuccess;
        public String statusMessage;
        public String hookDetails;
        public int androidSdk;
        public long durationMs;

        @Override
        public String toString() {
            return "LSPosedSelfTest Report:\n" +
                    "- Status: " + (isSuccess ? "PASSED (LSPosed Working)" : "FAILED") + "\n" +
                    "- Message: " + statusMessage + "\n" +
                    "- Details: " + hookDetails + "\n" +
                    "- Android SDK: " + androidSdk + " (" + Build.VERSION.RELEASE + ")\n" +
                    "- Duration: " + durationMs + " ms";
        }
    }

    /**
     * Target dummy method for test hooking.
     */
    public String getTestString() {
        return "UNHOOKED_ORIGINAL_STRING";
    }

    /**
     * Target dummy math method for test argument hooking.
     */
    public int computeSum(int a, int b) {
        return a + b;
    }

    /**
     * Executes the sample tests to confirm LSPosed Pine Hook Engine is functional.
     */
    public static TestReport runSelfTest() {
        long startTime = System.currentTimeMillis();
        TestReport report = new TestReport();
        report.androidSdk = Build.VERSION.SDK_INT;

        VLog.i(TAG, "==================================================");
        VLog.i(TAG, ">>> Starting LSPosed Hooking Engine Self-Test <<<");
        VLog.i(TAG, "==================================================");

        try {
            LSPosedSelfTest targetInstance = new LSPosedSelfTest();

            // Verification Test 1: Baseline Check
            String beforeHook = targetInstance.getTestString();
            VLog.i(TAG, "Test 1 Baseline String (Pre-hook): " + beforeHook);

            if (!"UNHOOKED_ORIGINAL_STRING".equals(beforeHook)) {
                report.isSuccess = false;
                report.statusMessage = "Baseline test failed: unexpected pre-hook string: " + beforeHook;
                return report;
            }

            // Verification Test 2: ART Method Hook via Pine
            Method targetMethod = LSPosedSelfTest.class.getDeclaredMethod("getTestString");
            final String HOOKED_RESULT = "LSPosed Hook Verified: SUCCESS!";

            Pine.hook(targetMethod, new MethodHook() {
                @Override
                public void afterCall(Pine.CallFrame callFrame) throws Throwable {
                    // Intercept and modify the return value
                    callFrame.setResult(HOOKED_RESULT);
                    VLog.i(TAG, "LSPosed Hook Intercepted getTestString() call!");
                }
            });

            String afterHook = targetInstance.getTestString();
            VLog.i(TAG, "Test 2 Result String (Post-hook): " + afterHook);

            if (!HOOKED_RESULT.equals(afterHook)) {
                report.isSuccess = false;
                report.statusMessage = "Hooking failed: expected '" + HOOKED_RESULT + "' but got '" + afterHook + "'";
                VLog.e(TAG, report.statusMessage);
                return report;
            }

            // Verification Test 3: Math argument manipulation test
            Method mathMethod = LSPosedSelfTest.class.getDeclaredMethod("computeSum", int.class, int.class);
            Pine.hook(mathMethod, new MethodHook() {
                @Override
                public void beforeCall(Pine.CallFrame callFrame) throws Throwable {
                    // Modify arguments (e.g. 10 + 20 => 50 + 50 = 100)
                    callFrame.args[0] = 50;
                    callFrame.args[1] = 50;
                    VLog.i(TAG, "LSPosed Hook Intercepted computeSum(args)!");
                }
            });

            int mathResult = targetInstance.computeSum(10, 20);
            VLog.i(TAG, "Test 3 Math Result: computeSum(10, 20) = " + mathResult + " (Expected 100)");

            if (mathResult != 100) {
                report.isSuccess = false;
                report.statusMessage = "Argument hook failed: expected 100 but got " + mathResult;
                VLog.e(TAG, report.statusMessage);
                return report;
            }

            // All tests passed!
            report.isSuccess = true;
            report.statusMessage = "LSPosed Hook Engine is Fully Functional!";
            report.hookDetails = "Method return hook & argument manipulation tests passed on ART runtime.";
            report.durationMs = System.currentTimeMillis() - startTime;

            VLog.i(TAG, "==================================================");
            VLog.i(TAG, ">>> LSPosed Self-Test RESULT: ALL TESTS PASSED! <<<");
            VLog.i(TAG, report.toString());
            VLog.i(TAG, "==================================================");

        } catch (Throwable t) {
            report.isSuccess = false;
            report.statusMessage = "Exception occurred during LSPosed self-test: " + t.getMessage();
            report.durationMs = System.currentTimeMillis() - startTime;
            VLog.e(TAG, "LSPosed Self-Test Exception: " + t.getMessage(), t);
        }

        return report;
    }
}
