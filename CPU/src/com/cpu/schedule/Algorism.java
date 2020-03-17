package com.cpu.schedule;

import java.util.LinkedList;
import java.util.Map;

public interface Algorism {
	public Map<String, LinkedList<Process>> Arrival(Map<String, LinkedList<Process>> State, int time);
	public Map<String, LinkedList<Process>> insertBlocked(Map<String, LinkedList<Process>> State);
	public Map<String, LinkedList<Process>> insertReady(Map<String, LinkedList<Process>> State);
	public Map<String, LinkedList<Process>> insertRunning(Map<String, LinkedList<Process>> State, int time);
	public Map<String, LinkedList<Process>> useCPU(Map<String, LinkedList<Process>> State);
	public Map<String, LinkedList<Process>> useIO(Map<String, LinkedList<Process>> State);
	public Map<String, LinkedList<Process>> stayReady(Map<String, LinkedList<Process>> State);
	public Map<String, LinkedList<Process>> TReadyinsert(Map<String, LinkedList<Process>> State);
}
