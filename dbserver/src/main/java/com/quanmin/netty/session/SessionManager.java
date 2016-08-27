package com.quanmin.netty.session;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.quanmin.servlet.ServletConfig;


public final class SessionManager {	
	/** log */
	private static final Log log = LogFactory.getLog(SessionManager.class);
	
    /** SessionManager */
    private static final SessionManager instance = new SessionManager();

	protected int duplicates = 0;

	// sessionListeners
	protected final List<SessionListener> sessionListeners = new ArrayList<SessionListener>();;
	
	// sessionListeners
	protected final List<SessionAttributeListener> sessionAttributeListeners = new ArrayList<SessionAttributeListener>();
	

	protected final ConcurrentMap<String, Session> sessions = new ConcurrentHashMap<String, Session>();
	
	// digest
	protected volatile MessageDigest digest;
	
	// Random
	protected Random random;
	
	// entropy
	protected String entropy;
	
	// ScheduledThreadPoolExecutor
	private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
	
	private boolean checkThreadStarted = false;
	
	private ServletConfig sc;
	
	private SessionManager() {
	}
	

	public static SessionManager getInstance() {
	    return instance;
	}
	

	public void startSessionCheckThread() {
		if (checkThreadStarted) {
			return;
		}
		
		synchronized (this) {
			if (!checkThreadStarted) {
				checkThreadStarted = true;

				scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
				scheduledThreadPoolExecutor.scheduleWithFixedDelay(new Runnable() {
					@Override
					public void run() {
						if (log.isDebugEnabled()) {
							log.debug("start check session");
						}
						try {
							Set<Entry<String, Session>> entrySet = sessions.entrySet();
							for (Entry<String, Session> entry : entrySet) {
								Session session = entry.getValue();
								if (!session.isValid()) {
									if (log.isDebugEnabled()) {
										log.debug("clear session id:" + session.getId());
									}
									session.invalidate();
								} 
							}
						} catch (Exception e) {
							log.error("clear session", e);
						}
						if (log.isDebugEnabled()) {
							log.debug("end check session");
						}
					}
				}, (Integer)sc.getInitParam(ServletConfig.SESSION_TICK_INTERVAL), (Integer)sc.getInitParam(ServletConfig.SESSION_TICK_INTERVAL), TimeUnit.SECONDS);
			}
		}
	}

	public void access(String sessionId) {
		if (null == sessionId) {
			return;
		}
		
		Session session = getSession(sessionId);
		if (null != session) {
			session.access();
		}
	}
	
	public Session getSession(String sessionId) {
		return getSession(sessionId, false);
	}
	

	public Session getSession(String sessionId, boolean allowCreate) {
		if (allowCreate) {
			Session session = null == sessionId ? null : _getSession(sessionId);
			if (null == session) {
				session = new StandardSession(generateSessionId(), sessionListeners, sessionAttributeListeners, sc);
				sessions.put(session.getId(), session);
				sessionId = session.getId();
			}
		}
		return null == sessionId ? null : _getSession(sessionId);
	}
	

	private Session _getSession(String sessionId) {
		Session session = sessions.get(sessionId);
		return session;
	}


	public void addSessionListener(SessionListener sessionListener) {
		sessionListeners.add(sessionListener);
	}
	

	public void addSessionAttributeListener(SessionAttributeListener sessionAttributeListener) {
		sessionAttributeListeners.add(sessionAttributeListener);
	}

	public void setServletConfig(ServletConfig sc) {
		this.sc = sc;
	}


    protected synchronized String generateSessionId() {
        byte[] random = new byte[16];
        String jvmRoute = "JVM_ROOT";
        String result = null;
        
        StringBuilder builder = new StringBuilder();
        do {
            int resultLenBytes = 0;
            if (result != null) {
                builder = new StringBuilder();
                duplicates++;
            }
            
            while (resultLenBytes < 16) {
                getRandomBytes(random);
                random = getMessageDigest().digest(random);
                for (int i = 0; i < random.length && resultLenBytes < 16; i++) {
                    byte b1 = (byte) ((random[i] & 0xf0) >> 4);
                    byte b2 = (byte) (random[i] & 0x0f);
                    
                    if (b1 < 10) {
                        builder.append((char)('0' + b1));
                    } else {
                        builder.append((char)('A' + (b1 - 10)));
                    }
                    
                    if (b2 < 10) {
                        builder.append((char)('0' + b2));
                    } else {
                        builder.append((char)('A' + (b2 - 10)));
                    }
                    
                    resultLenBytes++;
                }
            }
            
            if (jvmRoute != null) {
                builder.append('.').append(jvmRoute);
            }
            result = builder.toString();
            
        } while (sessions.containsKey(result));
        
        return result;
    }
    

    private MessageDigest getMessageDigest() {
        if (this.digest == null) {
            try {
                this.digest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                
            }
        }
        
        return this.digest;
    }


    private void getRandomBytes(byte[] bytes) {
        if (this.random == null) {
            long seed = System.currentTimeMillis();
            char[] entropy = getEntropy().toCharArray();
            for (int i = 0; i < entropy.length; i++) {
                long update = ((byte)entropy[i]) << ((i % 8) * 8);
                seed ^= update;
            }
            
            this.random = new Random();
            this.random.setSeed(seed);
        }
        
        this.random.nextBytes(bytes);
    }

    private String getEntropy() {
        if (this.entropy == null) {
            this.entropy = this.toString();
        }
        return this.entropy;
    }
}
