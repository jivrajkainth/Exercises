package uk.co.rapidware.sendence;

import junit.framework.TestCase;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.slf4j.MDC;

import java.util.concurrent.ScheduledExecutorService;

/**
 */
public class SendenceCacheManagerImplTest {

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
    public void testCreateCache() throws Exception {
        final ScheduledExecutorService executorService =
            getEasyMockSupport().createMock(ScheduledExecutorService.class);
        final SendenceCacheManagerImpl cacheManager = new SendenceCacheManagerImpl(executorService);

        getEasyMockSupport().replayAll();

        final SendenceCache<String, Object> cache = cacheManager.createCache(
            getTestName().getMethodName(),
            SendenceConfiguration.createFor(String.class, Object.class)
        );

        TestCase.assertNotNull(cache);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCacheAlreadyBeenCreated() throws Exception {
        final ScheduledExecutorService executorService =
            getEasyMockSupport().createMock(ScheduledExecutorService.class);
        final SendenceCacheManagerImpl cacheManager = new SendenceCacheManagerImpl(executorService);

        getEasyMockSupport().replayAll();

        cacheManager.createCache(
            getTestName().getMethodName(),
            SendenceConfiguration.createFor(String.class, Object.class)
        );

        cacheManager.createCache(
            getTestName().getMethodName(),
            SendenceConfiguration.createFor(String.class, Object.class)
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCacheNullName() throws Exception {
        final ScheduledExecutorService executorService =
            getEasyMockSupport().createMock(ScheduledExecutorService.class);
        final SendenceCacheManagerImpl cacheManager = new SendenceCacheManagerImpl(executorService);

        getEasyMockSupport().replayAll();

        cacheManager.createCache(
            null,
            SendenceConfiguration.createFor(String.class, Object.class)
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCacheEmptyName() throws Exception {
        final ScheduledExecutorService executorService =
            getEasyMockSupport().createMock(ScheduledExecutorService.class);
        final SendenceCacheManagerImpl cacheManager = new SendenceCacheManagerImpl(executorService);

        getEasyMockSupport().replayAll();

        cacheManager.createCache(
            "",
            SendenceConfiguration.createFor(String.class, Object.class)
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCacheNullKeyClass() throws Exception {
        final ScheduledExecutorService executorService =
            getEasyMockSupport().createMock(ScheduledExecutorService.class);
        final SendenceCacheManagerImpl cacheManager = new SendenceCacheManagerImpl(executorService);

        getEasyMockSupport().replayAll();

        final String cacheName = getTestName().getMethodName();
        cacheManager.createCache(
            cacheName,
            SendenceConfiguration.createFor(String.class, Object.class)
        );

        cacheManager.getCache(cacheName, null, Object.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCacheWrongKeyClass() throws Exception {
        final ScheduledExecutorService executorService =
            getEasyMockSupport().createMock(ScheduledExecutorService.class);
        final SendenceCacheManagerImpl cacheManager = new SendenceCacheManagerImpl(executorService);

        getEasyMockSupport().replayAll();

        final String cacheName = getTestName().getMethodName();
        cacheManager.createCache(
            cacheName,
            SendenceConfiguration.createFor(String.class, Object.class)
        );

        cacheManager.getCache(cacheName, Integer.class, Object.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCacheNullValueClass() throws Exception {
        final ScheduledExecutorService executorService =
            getEasyMockSupport().createMock(ScheduledExecutorService.class);
        final SendenceCacheManagerImpl cacheManager = new SendenceCacheManagerImpl(executorService);

        getEasyMockSupport().replayAll();

        final String cacheName = getTestName().getMethodName();
        cacheManager.createCache(
            cacheName,
            SendenceConfiguration.createFor(String.class, Object.class)
        );

        cacheManager.getCache(cacheName, String.class, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCacheWrongValueClass() throws Exception {
        final ScheduledExecutorService executorService =
            getEasyMockSupport().createMock(ScheduledExecutorService.class);
        final SendenceCacheManagerImpl cacheManager = new SendenceCacheManagerImpl(executorService);

        getEasyMockSupport().replayAll();

        final String cacheName = getTestName().getMethodName();
        cacheManager.createCache(
            cacheName,
            SendenceConfiguration.createFor(String.class, Object.class)
        );

        cacheManager.getCache(cacheName, String.class, String.class);
    }

    @Test
    public void testGetCache() throws Exception {
        final ScheduledExecutorService executorService =
            getEasyMockSupport().createMock(ScheduledExecutorService.class);
        final SendenceCacheManagerImpl cacheManager = new SendenceCacheManagerImpl(executorService);

        getEasyMockSupport().replayAll();

        final String cacheName = getTestName().getMethodName();

        final SendenceCache<String, Object> createdCache = cacheManager.createCache(
            cacheName,
            SendenceConfiguration.createFor(String.class, Object.class)
        );

        final SendenceCache<Class<String>, Class<Object>> retrievedCache =
            cacheManager.getCache(cacheName, String.class, Object.class);

        TestCase.assertSame(createdCache, retrievedCache);
    }
}