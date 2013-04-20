package edu.umn.robotcontrol.servlet;

import java.util.NoSuchElementException;
import java.util.SortedSet;
import java.util.TreeSet;

import edu.umn.robotcontrol.domain.RobotCommand;

public class CommandHolder {

	private static CommandHolder INSTANCE;
	private SortedSet<RobotCommand> list;
	
	private CommandHolder(){
		list = new TreeSet<RobotCommand>();
	};
	
	public static CommandHolder getInstance(){
		if(INSTANCE == null){
			INSTANCE = new CommandHolder();
		}
		return INSTANCE;
	}
	
	public void pushCommand(RobotCommand cmd){
		list.add(cmd);
	}
	
	// TODO make idempotent
	public RobotCommand popCommand(){
		try{
		RobotCommand cmd = list.first();
		return cmd;
		} catch (NoSuchElementException e){
			return null;
		}
	}
	
}
