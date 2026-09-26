package com.lody.virtual.lsposed;

import android.os.Build;
import com.lody.virtual.helper.utils.VLog;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

import top.canyie.pine.Pine;
import top.canyie.pine.callback.MethodHook;

/**
 * Self-Test Suite to verify that the LSPosed / Pine hooking engine is properly working.
 */
public class LSPosedSelfTest {

    private static final String TAG = "LSPosedSelfTest";
    private static final AtomicInteger sTestCounter = new AtomicInteger(1);

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
     * Target dynamic method for test hooking.
     */
    public String dynamicEcho(String token) {
        return "ORIGINAL:" + token;
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
    public static synchronized TestReport runSelfTest() {
        long startTime = System.currentTimeMillis();
        TestReport report = new TestReport();
        report.androidSdk = Build.VERSION.SDK_INT;
        int runId = sTestCounter.getAndIncrement();

        VLog.i(TAG, "==================================================");
        VLog.i(TAG, ">>> Starting LSPosed Hooking Engine Self-Test (Run #" + runId + ") <<<");
        VLog.i(TAG, "==================================================");

        MethodHook.Unhook hookRecord1 = null;
        MethodHook.Unhook hookRecord2 = null;

        try {
            LSPosedSelfTest targetInstance = new LSPosedSelfTest();
            String testToken = "token_" + runId + "_" + System.currentTimeMillis();

            // Verification Test 1: Method Hook & Return Value Replacement via Pine
            Method targetMethod = LSPosedSelfTest.class.getDeclaredMethod("dynamicEcho", String.class);
            final String expectedHookedResult = "LSPosed_HOOKED_" + testToken;

            hookRecord1 = Pine.hook(targetMethod, new MethodHook() {
                @Override
                public void afterCall(Pine.CallFrame callFrame) throws Throwable {
                    // Modify return value dynamically
                    callFrame.setResult(expectedHookedResult);
                    VLog.i(TAG, "LSPosed Hook Intercepted dynamicEcho() successfully!");
                }
            });

            String afterHook = targetInstance.dynamicEcho(testToken);
            VLog.i(TAG, "Test 1 Result (Post-hook): " + afterHook);

            if (!expectedHookedResult.equals(afterHook)) {
                report.isSuccess = false;
                report.statusMessage = "Return Hook test failed: expected '" + expectedHookedResult + "' but got '" + afterHook + "'";
                VLog.e(TAG, report.statusMessage);
                return report;
            }

            // Verification Test 2: Math argument manipulation test
            Method mathMethod = LSPosedSelfTest.class.getDeclaredMethod("computeSum", int.class, int.class);
            final int customA = 100 * runId;
            final int customB = 200 * runId;
            final int expectedSum = customA + customB;

            hookRecord2 = Pine.hook(mathMethod, new MethodHook() {
                @Override
                public void beforeCall(Pine.CallFrame callFrame) throws Throwable {
                    // Modify arguments dynamically
                    callFrame.args[0] = customA;
                    callFrame.args[1] = customB;
                    VLog.i(TAG, "LSPosed Hook Intercepted computeSum(args)!");
                }
            });

            int mathResult = targetInstance.computeSum(1, 2);
            VLog.i(TAG, "Test 2 Math Result: computeSum(1, 2) => " + mathResult + " (Expected " + expectedSum + ")");

            if (mathResult != expectedSum) {
                report.isSuccess = false;
                report.statusMessage = "Argument hook failed: expected " + expectedSum + " but got " + mathResult;
                VLog.e(TAG, report.statusMessage);
                return report;
            }

            // All tests passed!
            report.isSuccess = true;
            report.statusMessage = "LSPosed Hook Engine is Active & Fully Functional! (Test #" + runId + " Passed)";
            report.hookDetails = "Method return hook & argument manipulation tests passed on ART runtime.";
            report.durationMs = System.currentTimeMillis() - startTime;

            VLog.i(TAG, "==================================================");
            VLog.i(TAG, ">>> LSPosed Self-Test RESULT: ALL TESTS PASSED! <<<");
            VLog.i(TAG, report.toString());
            VLog.i(TAG, "==================================================");

        } catch (Throwable t) {
            report.isSuccess = false;
            report.statusMessage = "Exception during LSPosed test: " + t.getMessage();
            report.durationMs = System.currentTimeMillis() - startTime;
            VLog.e(TAG, "LSPosed Self-Test Exception: " + t.getMessage(), t);
        } finally {
            if (hookRecord1 != null) {
                try {
                    hookRecord1.unhook();
                    VLog.i(TAG, "Test 1 hook unhooked successfully.");
                } catch (Throwable ignored) {
                }
            }
            if (hookRecord2 != null) {
                try {
                    hookRecord2.unhook();
                    VLog.i(TAG, "Test 2 hook unhooked successfully.");
                } catch (Throwable ignored) {
                }
            }
        }

        return report;
    }
}

