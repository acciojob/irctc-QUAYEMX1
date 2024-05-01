package com.driver.services;


import com.driver.EntryDto.BookTicketEntryDto;
import com.driver.model.Passenger;
import com.driver.model.Ticket;
import com.driver.model.Train;
import com.driver.repository.PassengerRepository;
import com.driver.repository.TicketRepository;
import com.driver.repository.TrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TicketService {

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    TrainRepository trainRepository;

    @Autowired
    PassengerRepository passengerRepository;


    public Integer bookTicket(BookTicketEntryDto bookTicketEntryDto)throws Exception{

        //Check for validity
        //Use bookedTickets List from the TrainRepository to get bookings done against that train
        // Incase the there are insufficient tickets
        // throw new Exception("Less tickets are available");
        //otherwise book the ticket, calculate the price and other details
        //Save the information in corresponding DB Tables
        //Fare System : Check problem statement
        //Incase the train doesn't pass through the requested stations
        //throw new Exception("Invalid stations");
        //Save the bookedTickets in the train Object
        //Also in the passenger Entity change the attribute bookedTickets by using the attribute bookingPersonId.
       //And the end return the ticketId that has come from db

       Train train=trainRepository.findById(bookTicketEntryDto.getTrainId()).get();
        List<Integer>listPassenger=bookTicketEntryDto.getPassengerIds();

       List<Ticket>bookedlist=train.getBookedTickets();
       int tot= train.getNoOfSeats()-bookedlist.size();

       if(tot<bookTicketEntryDto.getNoOfSeats()){
           throw new Exception("Less tickets are available");
       }

      Passenger passenger=passengerRepository.findById(bookTicketEntryDto.getBookingPersonId()).get();

       boolean st=false;
       boolean dst=false;

       String[] arr=train.getRoute().split(",");

       for(String str:arr){
           if(str.equals(bookTicketEntryDto.getFromStation())){
               st=true;
           }else if(str.equals(bookTicketEntryDto.getToStation())){
               dst=true;
           }

           if(st==true && dst==true){
               break;
           }
       }

       if(st==false || dst==false){
           throw new Exception("Invalid stations");
       }

       int totFare=0;

       for(int i=0;i<arr.length;i++){
           if(arr[i].equals(bookTicketEntryDto.getFromStation())){
               for(int j=i+1;j<arr.length;j++){
                   if(arr[j].equals(bookTicketEntryDto.getToStation())){
                       totFare=(j-i)*bookTicketEntryDto.getNoOfSeats();
                       break;
                   }
               }
               break;
           }
       }


       Ticket ticket=new Ticket();

       ticket.setFromStation(bookTicketEntryDto.getFromStation());
       ticket.setToStation(bookTicketEntryDto.getToStation());
       ticket.setTotalFare(totFare);

      List<Passenger>allTicket=new ArrayList<>();

      for(Integer ids:listPassenger){
          Passenger passenger1=passengerRepository.findById(ids).get();
          allTicket.add(passenger1);
      }

      ticket.setPassengersList(allTicket);

      List<Ticket>ticketList=passenger.getBookedTickets();
      ticketList.add(ticket);

      List<Ticket>ticketList1=train.getBookedTickets();
      ticketList1.add(ticket);

      ticket=ticketRepository.save(ticket);
      trainRepository.save(train);

     return ticket.getTicketId();
    }
}
