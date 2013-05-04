package edu.umn.robotcontrol.servlet;

import java.util.NoSuchElementException;
import java.util.SortedSet;
import java.util.TreeSet;

import edu.umn.robotcontrol.domain.RobotPosition;

public class PositionHolder {

	private static PositionHolder INSTANCE;
	private SortedSet<RobotPosition> list;
	
	private PositionHolder(){
		list = new TreeSet<RobotPosition>();
	};
	
	public static PositionHolder getInstance(){
		if(INSTANCE == null){
			INSTANCE = new PositionHolder();
		}
		return INSTANCE;
	}
	
	public void pushCommand(RobotPosition pos){
		list.add(pos);
	}
	
	// TODO make idempotent
	public RobotPosition popCommand(){
		try{
			RobotPosition pos = list.first();
			// If there is more than one command in the queue, remove this one.
			// Never remove the last command.
			if (list.size() > 1) {
				list.remove(pos);
			}
			return pos;
		} catch (NoSuchElementException e){
			return null;
		}
	}
	
}
