package com.msse.robot_cowboy;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;


public class PhotoQueue {
	
	private static volatile PhotoQueue instance = null;
	private Queue<byte[]> queue;
	
	private PhotoQueue() {
		queue = new LinkedBlockingQueue<byte[]>();
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
	
	public boolean add(byte[] data) {
		return queue.add(data);
	}
	
	public byte[] peek() {
		return queue.peek();
	}
	
	public byte[] poll() {
		return queue.poll();
	}
}
