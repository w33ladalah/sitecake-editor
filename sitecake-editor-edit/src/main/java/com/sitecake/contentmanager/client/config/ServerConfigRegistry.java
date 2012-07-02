package com.sitecake.contentmanager.client.config;

import java.util.HashMap;

import com.google.gwt.user.client.Timer;
import com.google.inject.Inject;
import com.sitecake.contentmanager.client.SynchronizationBarrier;

public class ServerConfigRegistry extends AbstractConfigRegistry {

	private SynchronizationBarrier synchronizationBarrier;
	
	@Inject
	public ServerConfigRegistry(SynchronizationBarrier synchronizationBarrier) {
		super();
		registry = new HashMap<String, String>();
		this.synchronizationBarrier = synchronizationBarrier;
		
		synchronizationBarrier.lock();
		Timer timeout = new Timer() {
			public void run() {
				ServerConfigRegistry.this.synchronizationBarrier.release();
			}
		};
		timeout.schedule(5 * 1000);
	}

}
