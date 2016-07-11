package uk.co.rapidware.sendence;

import junit.framework.TestCase;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.slf4j.MDC;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;

/**
 */
public class SendenceCacheImplTest {

    @Rule
    public final TestName testName = new TestName();

    private EasyMockSupport easyMockSupport_;

    public TestName getTestName() {
        return testName;
    }

    public EasyMockSupport getEasyMockSupport() {
        return easyMockSupport_;
    }

    public void setEasyMockSupport(final EasyMockSupport easyMockSupport) {
        easyMockSupport_ = easyMockSupport;
    }

    @Before
    public void setUp() throws Exception {
        setEasyMockSupport(new EasyMockSupport());
        MDC.put("testName", getTestName().getMethodName());

    }

    @After
    public void tearDown() throws Exception {
        getEasyMockSupport().resetAll();
    }

    @Test
    public void testPut() throws Exception {
        final SendenceCacheImpl<String, Object> sendenceCache = createSendenceCache();

        final String key = "key";
        final Object value = new Object();

        getEasyMockSupport().replayAll();

        sendenceCache.put(key, value);

        getEasyMockSupport().verifyAll();

        TestCase.assertEquals(value, sendenceCache.getKeyValueStore().get(key));

    }

    private SendenceCacheImpl<String, Object> createSendenceCache() {
        final ScheduledExecutorService executorService =
            getEasyMockSupport().createMock(ScheduledExecutorService.class);

        return createSendenceCacheWith(executorService);
    }

    private SendenceCacheImpl<String, Object> createSendenceCacheWith(final ScheduledExecutorService executorService) {
        final String cacheName = getTestName().getMethodName();
        return new SendenceCacheImpl<>(cacheName, executorService);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullKey() throws Exception {
        final SendenceCacheImpl<String, Object> sendenceCache = createSendenceCache();

        final String key = null;
        final Object value = new Object();

        getEasyMockSupport().replayAll();

        sendenceCache.put(key, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullValue() throws Exception {
        final SendenceCacheImpl<String, Object> sendenceCache = createSendenceCache();

        final String key = "key";
        final Object value = null;

        getEasyMockSupport().replayAll();

        sendenceCache.put(key, value);
    }

    @Test
    public void testPutTwice() throws Exception {
        final SendenceCacheImpl<String, Object> sendenceCache = createSendenceCache();

        final String key = "key";
        final Object value = new Object();
        final Object aNewValue = new Object();

        getEasyMockSupport().replayAll();

        sendenceCache.put(key, value);
        sendenceCache.put(key, aNewValue);

        getEasyMockSupport().verifyAll();

        TestCase.assertEquals(aNewValue, sendenceCache.getKeyValueStore().get(key));
    }

    @Test
    public void testPutWithExpire() throws Exception {
        final ScheduledExecutorService executorService =
            getEasyMockSupport().createMock(ScheduledExecutorService.class);

        final Capture<? extends Callable<Object>> callableCapture = EasyMock.newCapture();

        EasyMock.expect(
            executorService.schedule(capture(callableCapture), eq(5L), eq(TimeUnit.MILLISECONDS))
        )
                .andReturn(null)
                .once()
        ;

        final SendenceCacheImpl<String, Object> sendenceCache = createSendenceCacheWith(executorService);

        final String key = "key";
        final Object value = new Object();

        getEasyMockSupport().replayAll();

        sendenceCache.put(key, value, Duration.forMs(5));
        final Object storedValue = sendenceCache.getKeyValueStore().get(key);
        final Object expiredValue = callableCapture.getValue().call();
        final Object shouldBeNull = sendenceCache.getKeyValueStore().get(key);

        getEasyMockSupport().verifyAll();

        TestCase.assertEquals(value, storedValue);
        TestCase.assertEquals(storedValue, expiredValue);
        TestCase.assertNull(shouldBeNull);
    }

    @Test
    public void testPutWithExpireThenPutAgainAfterExpiry() throws Exception {
        final ScheduledExecutorService executorService =
            getEasyMockSupport().createMock(ScheduledExecutorService.class);

        final Capture<? extends Callable<Object>> callableCapture = EasyMock.newCapture();

        EasyMock.expect(
            executorService.schedule(capture(callableCapture), eq(5L), eq(TimeUnit.MILLISECONDS))
        )
                .andReturn(null)
                .once()
        ;

        final SendenceCacheImpl<String, Object> sendenceCache = createSendenceCacheWith(executorService);

        final String key = "key";
        final Object value = new Object();
        final Object aNewValue = new Object();

        getEasyMockSupport().replayAll();

        sendenceCache.put(key, value, Duration.forMs(5));
        final Object storedValue = sendenceCache.getKeyValueStore().get(key);
        final Object expiredValue = callableCapture.getValue().call();
        final Object shouldBeNull = sendenceCache.getKeyValueStore().get(key);
        sendenceCache.put(key, aNewValue);

        getEasyMockSupport().verifyAll();

        TestCase.assertEquals(value, storedValue);
        TestCase.assertEquals(storedValue, expiredValue);
        TestCase.assertNull(shouldBeNull);

    }

    @Test
    public void testPutWithExpireThenPutAgainBeforeExpiry() throws Exception {
        final ScheduledExecutorService executorService =
            getEasyMockSupport().createMock(ScheduledExecutorService.class);

        final Capture<? extends Callable<Object>> callableCapture = EasyMock.newCapture();

        final long expireDurationAmount = 5L;
        EasyMock.expect(
            executorService.schedule(capture(callableCapture), eq(expireDurationAmount), eq(TimeUnit.MILLISECONDS))
        )
                .andReturn(null)
                .once()
        ;

        final SendenceCacheImpl<String, Object> sendenceCache = createSendenceCacheWith(executorService);

        final String key = "key";
        final Object value = new Object();
        final Object aNewValue = new Object();

        getEasyMockSupport().replayAll();

        sendenceCache.put(key, value, Duration.forMs(expireDurationAmount));
        final Object storedValue = sendenceCache.getKeyValueStore().get(key);
        sendenceCache.put(key, aNewValue);

        final Object expiredValue = callableCapture.getValue().call();
        final Object shouldNewValue = sendenceCache.getKeyValueStore().get(key);
        getEasyMockSupport().verifyAll();

        TestCase.assertEquals(value, storedValue);
        TestCase.assertNull(expiredValue);
        TestCase.assertEquals(aNewValue, shouldNewValue);
    }

    @Test
    public void testGet() throws Exception {
        final SendenceCache<String, Object> sendenceCache = createSendenceCache();

        final String key = "key";
        final Object value = new Object();

        getEasyMockSupport().replayAll();

        sendenceCache.put(key, value);

        getEasyMockSupport().verifyAll();

        for (int count = 0; count < 100; count++) {
            TestCase.assertEquals(value, sendenceCache.get(key));
        }
    }

    @Test
    public void testGetAfterExpire() throws Exception {
        final ScheduledExecutorService executorService =
            getEasyMockSupport().createMock(ScheduledExecutorService.class);

        final Capture<? extends Callable<Object>> callableCapture = EasyMock.newCapture();

        EasyMock.expect(
            executorService.schedule(capture(callableCapture), eq(5L), eq(TimeUnit.MILLISECONDS))
        )
                .andReturn(null)
                .once()
        ;

        final SendenceCacheImpl<String, Object> sendenceCache = createSendenceCacheWith(executorService);

        final String key = "key";
        final Object value = new Object();

        getEasyMockSupport().replayAll();

        sendenceCache.put(key, value, Duration.forMs(5));
        final Object storedValue = sendenceCache.get(key);
        final Object expiredValue = callableCapture.getValue().call();
        final Object shouldBeNull = sendenceCache.get(key);

        getEasyMockSupport().verifyAll();

        TestCase.assertEquals(value, storedValue);
        TestCase.assertEquals(storedValue, expiredValue);
        TestCase.assertNull(shouldBeNull);
    }

    @Test
    public void testRemove() throws Exception {
        final SendenceCache<String, Object> sendenceCache = createSendenceCache();

        final String key = "key";
        final Object value = new Object();

        getEasyMockSupport().replayAll();

        sendenceCache.put(key, value);
        final Object storedValue = sendenceCache.get(key);
        sendenceCache.remove("aDifferentKey");
        final Object valueStillThere = sendenceCache.get(key);
        final boolean removed = sendenceCache.remove(key);
        final Object shouldBeNull = sendenceCache.get(key);

        getEasyMockSupport().verifyAll();
        TestCase.assertEquals(value, storedValue);
        TestCase.assertSame(storedValue, valueStillThere);
        TestCase.assertTrue(removed);
        TestCase.assertNull(shouldBeNull);
    }
}