package com.cpu.schedule;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

public class Schedule {
	static Random random = new Random();
	public static void main(String[] args) throws Exception {
		Schedule schedule = new Schedule();
		int n = 0;
		int time = 0;
		int terminated=0;
		StringBuffer stringBuffer = new StringBuffer();
		
		BufferedReader bufferedReader = new BufferedReader(new FileReader(args[0]));
		while(true) {
			String line = bufferedReader.readLine();
			stringBuffer.append(line);
			if(line == null) break;
		}
		bufferedReader.close();
		LinkedList<Process> processList = new LinkedList<Process>();
		String file = stringBuffer.toString();
		String context[] = file.split("\\(");//context = 1 1 1 1)
		for(int i=1; i<context.length; i++) {
			processList.add(i-1, schedule.makeprocess(context[i], i));
		}
		n = processList.size();
		schedule.printhead();
		
		LinkedList<Process> Ready = new LinkedList<Process>();
		LinkedList<Process> Blocked = new LinkedList<Process>();
		LinkedList<Process> Running = new LinkedList<Process>();
		Map<String, LinkedList<Process>> State = new HashMap<String, LinkedList<Process>>();
		State.put("processlist", processList);
		State.put("ready", Ready);
		State.put("blocked", Blocked);
		State.put("running", Running);
		
		int c=0;
		int io=0;
		int tu=0;
		int wait=0;
		double c_u;
		double io_u;
		double th_p;
		double Av_tu;
		double Av_wait;
		
		switch(args[1]) {
			case "FCFS" :
				Algorism fcfs = new FCFS();
				//����
				for(time=0; true; time++) {
					//����ť�� �����ð������������� �ֱ�
					State = fcfs.Arrival(State, time);
					
					//����ƮŸ�� 0  �Ǹ� �ٽ� ������ �־��ֱ�, ��ϻ��·� �̵�, id null�̸� T����� �̵�
					State = fcfs.insertBlocked(State);
					
					//io �� �Ȱ� T����� �̵� �� io����Ʈ �ٽ� ����
					State = fcfs.insertReady(State);
					
					//pid�� T���� �����ؼ� ����� �̵�
					State = fcfs.TReadyinsert(State);
					
					//���� �ִ� ���μ��� �߿� ���� �������� ���׿� ����
					State = fcfs.insertRunning(State, time);
					
					//cpu���
					State = fcfs.useCPU(State);
					
					//io���
					State = fcfs.useIO(State);
					
					//������� +1
					State = fcfs.stayReady(State);
			
					//�������μ��� ī��Ʈ �� ���
					if(!State.get("running").isEmpty()) {
						if(State.get("running").get(0).getCpu_time() == State.get("running").get(0).getRunning_state()) {
							terminated++;
							System.out.printf("%3d  |", State.get("running").get(0).getPid());
							System.out.printf("(%3d,%3d,%3d,%3d)|", State.get("processlist").get(State.get("running").get(0).getPid()-1).getArrival_time(), 
									State.get("processlist").get(State.get("running").get(0).getPid()-1).getCpu_time(), State.get("processlist").get(State.get("running").get(0).getPid()-1).getCpu_burst_time(),
									State.get("processlist").get(State.get("running").get(0).getPid()-1).getIo_burst_time());
							System.out.printf("%15d  |", time+1);
							System.out.printf("%15d  |", State.get("running").get(0).getTurnaround_time());
							System.out.printf("%18d  |", State.get("running").get(0).getRunning_state());
							System.out.printf("%7d  |", State.get("running").get(0).getIo_time());
							System.out.printf("%10d  |", State.get("running").get(0).getWaiting_time());
							System.out.printf("\n");
							State.get("running").remove(0);
							if(terminated == n) {
								break;
							}
						}
					}
				}
				
				for(int i=0; i<processList.size(); i++) {
					c = c + processList.get(i).getRunning_state();
					io = io + processList.get(i).getIo_time();
					tu = tu + processList.get(i).getTurnaround_time();
					wait = wait +processList.get(i).getWaiting_time();
				}
				c_u = (double)(c*100)/(time+1);
				io_u = (double)(io*100)/(time+1);
				th_p = (double)(n*100)/(time+1);
				Av_tu = (double)tu/n;
				Av_wait = (double)wait/n;
				System.out.printf("CPU utilization : %.1f%%, I/O utilization : %.1f%%, Throughput in processes : %.1f%%,\n"
							+ "Average turnaround time : %.1f, Average waiting time : %.1f\n", c_u, io_u, th_p, Av_tu, Av_wait);
				
			case "RR_1" :
				schedule.RR(State, 1, n);
				
			case "RR_10" :
				schedule.RR(State, 10, n);
				
			case "RR_100" :
				schedule.RR(State, 100, n);
				
			case "SJF" :
				SJF sjf = new SJF();
				//����
				for(time=0; true; time++) {
					//����ť�� �����ð������������� �ֱ�
					State = sjf.Arrival(State, time);
					
					//����ƮŸ�� 0  �Ǹ� �ٽ� ������ �־��ֱ�, ��ϻ��·� �̵�, id null�̸� T����� �̵�
					State = sjf.insertBlocked(State);
					
					//io �� �Ȱ� T����� �̵� �� io����Ʈ �ٽ� ����
					State = sjf.insertReady(State);
					
					//pid�� T���� �����ؼ� ����� �̵�
					State = sjf.TReadyinsert(State);
					
					//���� �ִ°� random cpu burst�� ����
					State = sjf.Readysort(State);
			
					//���� �ִ� ���μ��� �߿� ���� �������� ���׿� ����
					State = sjf.insertRunning(State, time);
					
					//cpu���
					State = sjf.useCPU(State);
					
					//io���
					State = sjf.useIO(State);
					
					//������� +1
					State = sjf.stayReady(State);
			
					//�������μ��� ī��Ʈ �� ���
					if(!State.get("running").isEmpty()) {
						if(State.get("running").get(0).getCpu_time() == State.get("running").get(0).getRunning_state()) {
							terminated++;
							System.out.printf("%3d  |", State.get("running").get(0).getPid());
							System.out.printf("(%3d,%3d,%3d,%3d)|", State.get("processlist").get(State.get("running").get(0).getPid()-1).getArrival_time(), 
									State.get("processlist").get(State.get("running").get(0).getPid()-1).getCpu_time(), State.get("processlist").get(State.get("running").get(0).getPid()-1).getCpu_burst_time(),
									State.get("processlist").get(State.get("running").get(0).getPid()-1).getIo_burst_time());
							System.out.printf("%15d  |", time+1);
							System.out.printf("%15d  |", State.get("running").get(0).getTurnaround_time());
							System.out.printf("%18d  |", State.get("running").get(0).getRunning_state());
							System.out.printf("%7d  |", State.get("running").get(0).getIo_time());
							System.out.printf("%10d  |", State.get("running").get(0).getWaiting_time());
							System.out.printf("\n");
							State.get("running").remove(0);
							if(terminated == n) {
								break;
							}
						}
					}
				}
	
				for(int i=0; i<processList.size(); i++) {
					c = c + processList.get(i).getRunning_state();
					io = io + processList.get(i).getIo_time();
					tu = tu + processList.get(i).getTurnaround_time();
					wait = wait +processList.get(i).getWaiting_time();
				}
				c_u = (double)(c*100)/(time+1);
				io_u = (double)(io*100)/(time+1);
				th_p = (double)(n*100)/(time+1);
				Av_tu = (double)tu/n;
				Av_wait = (double)wait/n;
				System.out.printf("CPU utilization : %.1f%%, I/O utilization : %.1f%%, Throughput in processes : %.1f%%,\n"
							+ "Average turnaround time : %.1f, Average waiting time : %.1f\n", c_u, io_u, th_p, Av_tu, Av_wait);
				
			case "SRTF" :
				SRTF srtf = new SRTF();
				//����
				for(time=0; true; time++) {
					//����ť�� �����ð������������� �ֱ�
					State = srtf.Arrival(State, time);
					
					//����ƮŸ�� 0  �Ǹ� �ٽ� ������ �־��ֱ�, ��ϻ��·� �̵�, id null�̸� T����� �̵�
					State = srtf.insertBlocked(State);
					
					//io �� �Ȱ� T����� �̵� �� io����Ʈ �ٽ� ����
					State = srtf.insertReady(State);
					
					//pid�� T���� �����ؼ� ����� �̵�
					State = srtf.TReadyinsert(State);
					
					//���� �ִ°� random cpu burst�� ����
					State = srtf.Readysort(State);
					
					//���� �ִ� ���μ��� �߿� ���� �������� ���׿� ����
					State = srtf.insertRunning(State, time);
					
					//cpu���
					State = srtf.useCPU(State);
					
					//io���
					State = srtf.useIO(State);
					
					//������� +1
					State = srtf.stayReady(State);
			
					//�������μ��� ī��Ʈ �� ���
					if(!State.get("running").isEmpty()) {
						if(State.get("running").get(0).getCpu_time() == State.get("running").get(0).getRunning_state()) {
							terminated++;
							System.out.printf("%3d  |", State.get("running").get(0).getPid());
							System.out.printf("(%3d,%3d,%3d,%3d)|", State.get("processlist").get(State.get("running").get(0).getPid()-1).getArrival_time(), 
									State.get("processlist").get(State.get("running").get(0).getPid()-1).getCpu_time(), State.get("processlist").get(State.get("running").get(0).getPid()-1).getCpu_burst_time(),
									State.get("processlist").get(State.get("running").get(0).getPid()-1).getIo_burst_time());
							System.out.printf("%15d  |", time+1);
							System.out.printf("%15d  |", State.get("running").get(0).getTurnaround_time());
							System.out.printf("%18d  |", State.get("running").get(0).getRunning_state());
							System.out.printf("%7d  |", State.get("running").get(0).getIo_time());
							System.out.printf("%10d  |", State.get("running").get(0).getWaiting_time());
							System.out.printf("\n");
							State.get("running").remove(0);
							if(terminated == n) {
								break;
							}
						}
					}
				}
				for(int i=0; i<processList.size(); i++) {
					c = c + processList.get(i).getRunning_state();
					io = io + processList.get(i).getIo_time();
					tu = tu + processList.get(i).getTurnaround_time();
					wait = wait +processList.get(i).getWaiting_time();
				}
				c_u = (double)(c*100)/(time+1);
				io_u = (double)(io*100)/(time+1);
				th_p = (double)(n*100)/(time+1);
				Av_tu = (double)tu/n;
				Av_wait = (double)wait/n;
				System.out.printf("CPU utilization : %.1f%%, I/O utilization : %.1f%%, Throughput in processes : %.1f%%,\n"
							+ "Average turnaround time : %.1f, Average waiting time : %.1f\n", c_u, io_u, th_p, Av_tu, Av_wait);
		}
	}
	
	private Process makeprocess(String context, int i) {
		context = context.replace(")", "");
		context = context.replace("null", "");
		String contextlist[] = context.split("\\s");
		if(Integer.parseInt(contextlist[3]) == 0) {
			Process process = new Process(i, Integer.parseInt(contextlist[0]),Integer.parseInt(contextlist[1]),
					Integer.parseInt(contextlist[2]),0,random.nextInt(Integer.parseInt(contextlist[2]))+1);
			return process;
		}
		else {
		Process process = new Process(i, Integer.parseInt(contextlist[0]),Integer.parseInt(contextlist[1]),
				Integer.parseInt(contextlist[2]),Integer.parseInt(contextlist[3]),random.nextInt(Integer.parseInt(contextlist[2]))+1,
				random.nextInt(Integer.parseInt(contextlist[3]))+1);
			return process;
		}
	}
	
	private void printhead() {
		System.out.println(" pid |  (A, C, B, IO)  | Fininshing Time | Turnaround Time | Running State Time | IO Time | Waiting Time ");
	}
	
	private void RR(Map<String, LinkedList<Process>> State, int q, int n) {
		int c=0;
		int io=0;
		int tu=0;
		int wait=0;
		double c_u;
		double io_u;
		double th_p;
		double Av_tu;
		double Av_wait;
		int time=0;
		int terminated=0;
		RR rr = new RR();
		//����
		for(time=0; true; time++) {
			//����ť�� �����ð������������� �ֱ�
			State = rr.Arrival(State, time);
			
			//����ƮŸ�� 0  �Ǹ� �ٽ� ������ �־��ֱ�, ��ϻ��·� �̵�, io�� null�̸� T����� �̵�
			State = rr.insertBlocked(State);
			
			//io �� �Ȱ� T����� �̵� �� io����Ʈ �ٽ� ����
			State = rr.insertReady(State);
			
			//pid�� T���� �����ؼ� ����� �̵�
			State = rr.TReadyinsert(State);
			
			//quantum�˻� �� ������ ready�� �̵�(�ǵ�)
			State = rr.QuantumCheck(State, q);
			
			//���� �ִ� ���μ��� �߿� ���� �������� ���׿� ����
			State = rr.insertRunning(State, time);
			
			//cpu���
			State = rr.useCPU(State);
			
			//io���
			State = rr.useIO(State);
			
			//������� +1
			State = rr.stayReady(State);
	
			//�������μ��� ī��Ʈ �� ���
			if(!State.get("running").isEmpty()) {
				if(State.get("running").get(0).getCpu_time() == State.get("running").get(0).getRunning_state()) {
					terminated++;
					System.out.printf("%3d  |", State.get("running").get(0).getPid());
					System.out.printf("(%3d,%3d,%3d,%3d)|", State.get("processlist").get(State.get("running").get(0).getPid()-1).getArrival_time(), 
							State.get("processlist").get(State.get("running").get(0).getPid()-1).getCpu_time(), State.get("processlist").get(State.get("running").get(0).getPid()-1).getCpu_burst_time(),
							State.get("processlist").get(State.get("running").get(0).getPid()-1).getIo_burst_time());
					System.out.printf("%15d  |", time+1);
					System.out.printf("%15d  |", State.get("running").get(0).getTurnaround_time());
					System.out.printf("%18d  |", State.get("running").get(0).getRunning_state());
					System.out.printf("%7d  |", State.get("running").get(0).getIo_time());
					System.out.printf("%10d  |", State.get("running").get(0).getWaiting_time());
					System.out.printf("\n");
					State.get("running").remove(0);
					if(terminated == n) {
						break;
					}
				}
			}
		}
		for(int i=0; i<State.get("processlist").size(); i++) {
			c = c + State.get("processlist").get(i).getRunning_state();
			io = io + State.get("processlist").get(i).getIo_time();
			tu = tu + State.get("processlist").get(i).getTurnaround_time();
			wait = wait +State.get("processlist").get(i).getWaiting_time();
		}
		c_u = (double)(c*100)/(time+1);
		io_u = (double)(io*100)/(time+1);
		th_p = (double)(n*100)/(time+1);
		Av_tu = (double)tu/n;
		Av_wait = (double)wait/n;
		System.out.printf("CPU utilization : %.1f%%, I/O utilization : %.1f%%, Throughput in processes : %.1f%%,\n"
					+ "Average turnaround time : %.1f, Average waiting time : %.1f\n", c_u, io_u, th_p, Av_tu, Av_wait);
	}
	
}
