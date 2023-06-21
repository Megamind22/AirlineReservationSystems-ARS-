package javaapplication3;

import java.time.LocalTime;
import java.util.*;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.swing.JTextArea;

class Server implements FlightBuffer{
	private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
	
	private final HashMap<Integer,Long> seats;
        private final Object[] mixedArray = new String[2];
	private JTextArea log=null;
     
	private int bookedCount = 0;
	
	public Server( )
	{
		
		
		this.seats = new HashMap<>();
		for (int i=1;i<=24;i++) {
			this.seats.put(i,0L);
		}
		
	}
        
        
        public void ClientsLog(JTextArea log){
            this.log=log;
        }

	@Override
	public List<Object> seeAvailableSeats(String name)
	{
		lock.readLock().lock();
		try {
			String time = "Time: " + LocalTime.now() +"\n";
			StringBuffer newLog = new StringBuffer();
			newLog.append('\n');
			newLog.append(time + name);
                        newLog.append('\n');
			newLog.append("Server update Available Seats..! \n");
                        name= newLog.toString();
        } finally {
            lock.readLock().unlock();
        }
                return Arrays.asList(name,this.seats);		
	}


	@Override
	public Object[] makeReservation(String name, int seatNo, long customerId)  {
		
		lock.writeLock().lock();
	
		try {
			String time = "Time: " + LocalTime.now() +"\n";
			StringBuffer newLog = new StringBuffer();
			newLog.append('\n');
			newLog.append("Revieve Recervation"+time + name);
			if (bookedCount == this.seatCount()) {
				newLog.append(" no more seats available on the flight currently!");
			} else {
				newLog.append(" tries to book the seat " + seatNo + "\n");
				if (this.seats.containsKey(seatNo)) {
					if(this.seats.get(seatNo) == 0L) {
						newLog.append(time + name + " booked seat number " + seatNo + " successfully\n");
						this.seats.put(seatNo, customerId);
					
						bookedCount++;
                                                mixedArray[0]="true";
					} 
					else {
                                                mixedArray[0]="flase";
						newLog.append(time + name + " could not book seat number " + seatNo + " since it has been already booked\n");
					}
				}
			}
                        mixedArray[1]=newLog.toString();    
        } 
		finally {
                    
            lock.writeLock().unlock();
          
        }		
            return mixedArray;
        }	
        @Override
	public int seatCount() {
		return this.seats.size();
	}

   

	

}
