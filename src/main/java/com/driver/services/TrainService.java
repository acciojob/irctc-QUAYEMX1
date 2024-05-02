package com.driver.services;

import com.driver.EntryDto.AddTrainEntryDto;
import com.driver.EntryDto.SeatAvailabilityEntryDto;
import com.driver.model.Passenger;
import com.driver.model.Station;
import com.driver.model.Ticket;
import com.driver.model.Train;
import com.driver.repository.TrainRepository;
import org.aspectj.weaver.ast.HasAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TrainService {

    @Autowired
    TrainRepository trainRepository;

    public Integer addTrain(AddTrainEntryDto trainEntryDto){

        //Add the train to the trainRepository
        //and route String logic to be taken from the Problem statement.
        //Save the train and return the trainId that is generated from the database.
        //Avoid using the lombok library

        List<Station>stationList=trainEntryDto.getStationRoute();

        String rout="";
        for(int i=0;i<stationList.size();i++){
            if(i==stationList.size()-1){
                rout=rout+stationList.get(i);
            }else{
                rout=rout+",";
            }
        }

        Train train=new Train();
        train.setRoute(rout);
        train.setDepartureTime(trainEntryDto.getDepartureTime());
        train.setNoOfSeats(trainEntryDto.getNoOfSeats());

        train=trainRepository.save(train);

        return train.getTrainId();
    }

    public Integer calculateAvailableSeats(SeatAvailabilityEntryDto seatAvailabilityEntryDto){

        //Calculate the total seats available
        //Suppose the route is A B C D
        //And there are 2 seats avaialble in total in the train
        //and 2 tickets are booked from A to C and B to D.
        //The seat is available only between A to C and A to B. If a seat is empty between 2 station it will be counted to our final ans
        //even if that seat is booked post the destStation or before the boardingStation
        //Inshort : a train has totalNo of seats and there are tickets from and to different locations
        //We need to find out the available seats between the given 2 stations.

        Train train=trainRepository.findById(seatAvailabilityEntryDto.getTrainId()).get();

        int totSeats=train.getNoOfSeats();
        List<Ticket>trainList=train.getBookedTickets();

        Map<String,Integer> mp=new HashMap<>();
        String[] arr=train.getRoute().split(",");
        for(int i=0;i<arr.length;i++){
            mp.put(arr[i],i);
        }

        String st=seatAvailabilityEntryDto.getFromStation()+"";
        String en=seatAvailabilityEntryDto.getToStation()+"";
        int booking=0;

//        for(Ticket ticket:trainList){
//            if(mp.get(ticket.))
//        }
        return 0;
    }

    public Integer calculatePeopleBoardingAtAStation(Integer trainId,Station station) throws Exception{

        //We need to find out the number of people who will be boarding a train from a particular station
        //if the trainId is not passing through that station
        //throw new Exception("Train is not passing from this station");
        //  in a happy case we need to find out the number of such people.

        Train train=trainRepository.findById(trainId).get();
        String[] arr= train.getRoute().split(",");
        String stsn=station+"";

        boolean flg=false;
        for(String str:arr){
            if(str.equals(stsn)){
                flg=true;
            }
        }

        if (flg == false) {
            throw new Exception("Train is not passing from this station");
        }

        List<Ticket>ticketList=train.getBookedTickets();
        int tot=0;

        for(Ticket ticket:ticketList){
           tot+=ticket.getPassengersList().size();
        }

        return tot;
    }

    public Integer calculateOldestPersonTravelling(Integer trainId){

        //Throughout the journey of the train between any 2 stations
        //We need to find out the age of the oldest person that is travelling the train
        //If there are no people travelling in that train you can return 0

       Train train=trainRepository.findById(trainId).get();

       List<Ticket>ticketList=train.getBookedTickets();
       int age=0;

       for(Ticket ticket:ticketList){
           List<Passenger>passengerList=ticket.getPassengersList();
           for(Passenger passenger:passengerList){
               int ages=passenger.getAge();
               if(age<ages){
                   age=ages;
               }
           }
       }

     return age;
    }

    public List<Integer> trainsBetweenAGivenTime(Station station, LocalTime startTime, LocalTime endTime){

        //When you are at a particular station you need to find out the number of trains that will pass through a given station
        //between a particular time frame both start time and end time included.
        //You can assume that the date change doesn't need to be done ie the travel will certainly happen with the same date (More details
        //in problem statement)
        //You can also assume the seconds and milli seconds value will be 0 in a LocalTime format.

        return new ArrayList<>();
    }

}
