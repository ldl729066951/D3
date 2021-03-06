package org.d3.zk.app.conf;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;

public class ConfigWatcher implements Watcher {

	ActiveKeyValueStore store;
	
	public ConfigWatcher(String host) throws IOException, InterruptedException{
		store = new ActiveKeyValueStore();
		store.connect(host);
	}

	public void displayConfig() throws InterruptedException, KeeperException {
		String value = store.read(ConfigUpdater.PATH, this);
		System.out.printf("Read %s as %s\n", ConfigUpdater.PATH, value);
	}

	@Override
	public void process(WatchedEvent event) {
		System.out.println(event);
		if(event.getType() == EventType.NodeCreated){
			System.out.println(event.getPath());
		}
		if (event.getType() == EventType.NodeDataChanged) {
			System.out.println(event.getPath());
			try {
				displayConfig();
			} catch (InterruptedException e) {
				System.err.println("Interrupted. Exiting.");
				Thread.currentThread().interrupt();
			} catch (KeeperException e) {
				System.err.printf("KeeperException: %s. Exiting.\n", e);
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		ConfigWatcher configWatcher = new ConfigWatcher("127.0.0.1");
		configWatcher.displayConfig();
		// stay alive until process is killed or thread is interrupted
		Thread.sleep(Long.MAX_VALUE);
	}

}
