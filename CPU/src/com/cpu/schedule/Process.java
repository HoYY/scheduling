package com.cpu.schedule;

public class Process {
	private int arrival_time = 0;
	private int cpu_time = 0; //cpu 총 점유시간
	private int cpu_burst_time = 0;
	private int io_burst_time = 0;
	private int finishing_time = 0;
	private int io_time = 0; //blocked state에 있었던 총 합
	private int waiting_time = 0;
	private int rcpu_burst_time = 0;
	private Integer rio_burst_time;
	private int running_state = 0;//cpu_time
	private int turnaround_time = 0;//finishing - arrival
	private int pid = 0;
	private int q = 0;
	
	
	public Process(int pid, int arrival_time, int cpu_time, int cpu_burst_time, int io_burst_time, int rcpu_burst_time, int rio_burst_time) {
		this.arrival_time = arrival_time;
		this.cpu_time = cpu_time;
		this.cpu_burst_time = cpu_burst_time;
		this.io_burst_time = io_burst_time;
		this.rcpu_burst_time = rcpu_burst_time;
		this.rio_burst_time = rio_burst_time;
		this.pid = pid;
	}
	
	public Process(int pid, int arrival_time, int cpu_time, int cpu_burst_time, int io_burst_time, int rcpu_burst_time) {
		this.arrival_time = arrival_time;
		this.cpu_time = cpu_time;
		this.cpu_burst_time = cpu_burst_time;
		this.io_burst_time = io_burst_time;
		this.pid = pid;
		this.rcpu_burst_time = rcpu_burst_time;
	}
	
	public Process() {}
	
	public Boolean isNull() {
		return (this.rio_burst_time == null);
	}
	
	public int getArrival_time() {
		return arrival_time;
	}
	public void setArrival_time(int arrival_time) {
		this.arrival_time = arrival_time;
	}
	public int getCpu_time() {
		return cpu_time;
	}
	public void setCpu_time(int cpu_time) {
		this.cpu_time = cpu_time;
	}
	public int getCpu_burst_time() {
		return cpu_burst_time;
	}
	public void setCpu_burst_time(int cpu_burst_time) {
		this.cpu_burst_time = cpu_burst_time;
	}
	public int getIo_burst_time() {
		return io_burst_time;
	}
	public void setIo_burst_time(int io_burst_time) {
		this.io_burst_time = io_burst_time;
	}

	public int getIo_time() {
		return io_time;
	}
	public void setIo_time(int io_time) {
		this.io_time = io_time;
	}
	public int getWaiting_time() {
		return waiting_time;
	}
	public void setWaiting_time(int waiting_time) {
		this.waiting_time = waiting_time;
	}

	public int getRcpu_burst_time() {
		return rcpu_burst_time;
	}

	public void setRcpu_burst_time(int rcpu_burst_time) {
		this.rcpu_burst_time = rcpu_burst_time;
	}

	public int getRio_burst_time() {
		return rio_burst_time;
	}

	public void setRio_burst_time(int rio_burst_time) {
		this.rio_burst_time = rio_burst_time;
	}

	public int getFinishing_time() {
		return finishing_time;
	}

	public void setFinishing_time(int finishing_time) {
		this.finishing_time = finishing_time;
	}

	public int getRunning_state() {
		return running_state;
	}

	public void setRunning_state(int running_state) {
		this.running_state = running_state;
	}

	public int getTurnaround_time() {
		return turnaround_time;
	}

	public void setTurnaround_time(int turnaround_time) {
		this.turnaround_time = turnaround_time;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getQ() {
		return q;
	}

	public void setQ(int q) {
		this.q = q;
	}
	
}
