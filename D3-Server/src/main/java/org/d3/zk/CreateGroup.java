package org.d3.zk;

import java.io.IOException;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;

public class CreateGroup extends ConnectionWatcher{

	public void create(String groupName) throws KeeperException, InterruptedException{
		String path = "/" + groupName;
		String createdPath = zk.create(path, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		System.out.println("Created " + createdPath);
	}
	
	public static void main(String...strings) throws IOException, InterruptedException, KeeperException{
		CreateGroup c = new CreateGroup();
		c.connect("127.0.0.1");
		c.create("registry");
		c.close();
	}
	
}
