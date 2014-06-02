package game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import util.Updatable;

public class ThreadHandler {
	private ExecutorService threadPool;
	
	public ThreadHandler() {
		threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	}
	public void update(Set<Updatable> updates) {
		Set<Callable<Object>> callables = new HashSet<Callable<Object>>();
		for (Updatable u : updates) {
			callables.add(new Updates(u));
		}
		try {
			threadPool.invokeAll(callables);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class Updates implements Callable<Object> {
	private final Updatable update;
	
	public Updates(Updatable update) {
		this.update = update;
	}
	@Override
	public Object call() throws Exception {
		update.update();
		return null;
	}
}