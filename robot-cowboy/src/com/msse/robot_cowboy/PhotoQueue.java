package com.msse.robot_cowboy;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;


public class PhotoQueue {
	
	private static volatile PhotoQueue instance = null;
	private Queue<String> queue;
	
	private PhotoQueue() {
		queue = new LinkedBlockingQueue<String>();
	}

	public static PhotoQueue getInstance() {
		if (instance == null) {
			synchronized (PhotoQueue.class) {
				if (instance == null) {
					instance = new PhotoQueue();
				}
			}
		}
		return instance;
	}
	
	public boolean add(String filename) {
		return queue.add(filename);
	}
	
	public String peek() {
		return queue.peek();
	}
	
	public String poll() {
		return queue.poll();
	}
}
