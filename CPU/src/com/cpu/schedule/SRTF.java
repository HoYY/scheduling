package com.cpu.schedule;

import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

public class SRTF implements Algorism {
	Random random = new Random();
	LinkedList<Process> TReady = new LinkedList<Process>();
	
	@Override
	public Map<String, LinkedList<Process>> Arrival(Map<String, LinkedList<Process>> State, int time) {
		for(int i=0; i<State.get("processlist").size(); i++) {
			if(State.get("processlist").get(i).getArrival_time() == time) {
				State.get("ready").add(State.get("processlist").get(i));
				State = SRTFcheck(State);
			}
		}
		return State;
	}
	
	@Override
	public Map<String, LinkedList<Process>> insertBlocked(Map<String, LinkedList<Process>> State) {
		if(!State.get("running").isEmpty()) {
			if(State.get("running").get(0).getRcpu_burst_time() == 0) {
				State.get("running").get(0).setRcpu_burst_time(random.nextInt(State.get("running").get(0).getCpu_burst_time())+1);
				if(!State.get("running").get(0).isNull()) {
					State.get("blocked").add(State.get("running").get(0));
				}
				else {
					TReady.add(State.get("running").get(0));
				}
				State.get("running").remove(0);
			}
		}
		return State;
	}
	
	@Override
	public Map<String, LinkedList<Process>> insertReady(Map<String, LinkedList<Process>> State) {
		if(!State.get("blocked").isEmpty()) {
			for(int i=0; i<State.get("blocked").size(); i++) {
				if(State.get("blocked").get(i).getRio_burst_time() == 0) {
					State.get("blocked").get(i).setRio_burst_time(random.nextInt(State.get("blocked").get(i).getIo_burst_time())+1);
					TReady.add(State.get("blocked").get(i));
					State.get("blocked").remove(i);
					i = i-1;
				}
			}
		}
		return State;
	}
	
	@Override
	public Map<String, LinkedList<Process>> insertRunning(Map<String, LinkedList<Process>> State, int time) {
		for(int ready=0; ready < State.get("ready").size(); ready++) {
			if(State.get("ready").get(ready).getArrival_time() <= time && State.get("running").isEmpty()) {
				State.get("running").add(0, State.get("ready").get(ready));
				State.get("ready").remove(ready);
			}
		}
		return State;
	}
	
	@Override
	public Map<String, LinkedList<Process>> TReadyinsert(Map<String, LinkedList<Process>> State){
		if(!TReady.isEmpty()) {
			while(true) {	
				int low=0;
				for(int i=1; i < TReady.size(); i++) {
					if(TReady.get(low).getPid() > TReady.get(i).getPid()) {
						low = i;
					}
				}
				State.get("ready").add(TReady.get(low));
				TReady.remove(low);
				if(TReady.isEmpty())
					break;
			}
			State = SRTFcheck(State);
		}
		return State;
	}
	
	public Map<String, LinkedList<Process>> Readysort(Map<String, LinkedList<Process>> State){
		for(int j=0; j < State.get("ready").size(); j++) {	
			int low=j;
			for(int i=j; i < State.get("ready").size(); i++) {
				if(State.get("ready").get(low).getRcpu_burst_time() > State.get("ready").get(i).getRcpu_burst_time()) {
					low = i;
				}
			}
			State.get("ready").add(j, State.get("ready").get(low));
			State.get("ready").remove(low+1);
		}
		return State;
	}
	
	@Override
	public Map<String, LinkedList<Process>> useCPU(Map<String, LinkedList<Process>> State) {
		if(!State.get("running").isEmpty()) {
			State.get("running").get(0).setRcpu_burst_time(State.get("running").get(0).getRcpu_burst_time()-1);
			State.get("running").get(0).setRunning_state(State.get("running").get(0).getRunning_state()+1);
			State.get("running").get(0).setTurnaround_time(State.get("running").get(0).getTurnaround_time()+1);
		}
		return State;
	}
	
	@Override
	public Map<String, LinkedList<Process>> useIO(Map<String, LinkedList<Process>> State) {
		if(!State.get("blocked").isEmpty()) {
			for(int i=0; i<State.get("blocked").size(); i++) {
				State.get("blocked").get(i).setRio_burst_time(State.get("blocked").get(i).getRio_burst_time()-1);
				State.get("blocked").get(i).setTurnaround_time(State.get("blocked").get(i).getTurnaround_time()+1);
				State.get("blocked").get(i).setIo_time(State.get("blocked").get(i).getIo_time()+1);
			}
		}
		return State;
	}
	
	@Override
	public Map<String, LinkedList<Process>> stayReady(Map<String, LinkedList<Process>> State) {
		for(int i=0; i<State.get("ready").size(); i++) {
			State.get("ready").get(i).setWaiting_time(State.get("ready").get(i).getWaiting_time()+1);
			State.get("ready").get(i).setTurnaround_time(State.get("ready").get(i).getTurnaround_time()+1);
		}
		return State;
	}
	
	//레디에 새로운게 들어왔을때 비교
	public Map<String, LinkedList<Process>> SRTFcheck(Map<String, LinkedList<Process>> State) {
		if(!State.get("running").isEmpty() && !State.get("ready").isEmpty()) {
			if(State.get("running").get(0).getRcpu_burst_time() > State.get("ready").get(0).getRcpu_burst_time()) {
				State.get("ready").add(State.get("running").get(0));
				State.get("running").remove(0);
				State.get("running").add(State.get("ready").get(0));
				State.get("ready").remove(0);
			}
		}
		return State;
	}

}
