package edu.umn.robotcontrol.domain;

public class RobotCommand implements Comparable<RobotCommand>{
	private int component;
	private int value;
	private long issueTime;

	public int getComponent() {
		return component;
	}

	public void setComponent(int component) {
		this.component = component;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * @return the issueTime
	 */
	public long getIssueTime() {
		return issueTime;
	}

	/**
	 * @param issueTime the issueTime to set
	 */
	public void setIssueTime(long issueTime) {
		this.issueTime = issueTime;
	}

	@Override
	public String toString() {
		return "RobotCommand [component=" + component + ", value=" + value + "]";
	}

	@Override
	public int compareTo(RobotCommand arg0) {
		return Long.valueOf(issueTime).compareTo(Long.valueOf(arg0.getIssueTime()));
	}
}
