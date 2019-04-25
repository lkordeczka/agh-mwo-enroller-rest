package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/meetings")
public class meetingRestController {

	@Autowired
	MeetingService meetingService;
	@Autowired
	ParticipantService participantService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getMeetings() {
		Collection<Meeting> meetings = meetingService.getAll();
		return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getMeeting(@PathVariable("id") Long id){
		Meeting meeting = meetingService.getByID(id);
		if (meeting == null){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	}
	
	 @RequestMapping(value = "", method = RequestMethod.POST)
	 public ResponseEntity<?> registerMeetings(@RequestBody Meeting meeting){
		meetingService.add(meeting);
		return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);
	 }
	 
//	 curl -X DELETE {} http://localhost:8080/meetings/6
	 @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	 public ResponseEntity<?> removeMeeting(@PathVariable("id") Long id){
		 Meeting meeting = meetingService.getByID(id);
			if (meeting == null){
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			meetingService.remove(meeting);
			return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	 }
	
//	curl -H "Content-Type: application/json" -d '{"login":"JestemPrzypisanyDoSpotkania7", "password": "password"}' localhost:8080/meetings/12/participant
	@RequestMapping(value = "/{meetingID}/participant", method = RequestMethod.POST)
	public ResponseEntity<?> addParticipant(
			@PathVariable("meetingID") Long meetingID,
			@RequestBody Participant participant){
		
		Meeting meeting = meetingService.getByID(meetingID);
	
		if (meeting == null) {
			return new ResponseEntity("Unable to add. A meeting with id " + meetingID + " not exist.", HttpStatus.NOT_FOUND);
		}
		Participant foundParticipant = participantService.getUserByID(participant.getLogin());
		if (foundParticipant != null){
			return new ResponseEntity("Unable to create. A participant with login " + participant.getLogin() + " already exist.", HttpStatus.CONFLICT);
		}
		
		participantService.add(participant);
		meetingService.addParticipant(participant, meeting);
		
//		meeting.addParticipant(participant);
		return new ResponseEntity(HttpStatus.OK);
	}
	 
	@RequestMapping(value = "/{meetingID}/participants", method = RequestMethod.GET)
	public ResponseEntity<?> showParticipants(@PathVariable("meetingID") Long meetingID){
						
		Meeting meeting = meetingService.getByID(meetingID);		
		
		if (meeting == null) {			
			return new ResponseEntity("Unable to add. A meeting with id " + meetingID + " not exist.", HttpStatus.NOT_FOUND);
		}		
		Collection<Participant> participants = meetingService.participantsList(meeting);		
		for (Participant participant : participants) {						
		}		
		if (participants.size() > 0) {			
			for (Participant participant : participants) {				
			}
			return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
		} else {			
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		
	}
	 
//	 curl -H "Content-Type: application/json" -d '{"title": "someTitle2"}' -X PUT http://localhost:8080/meetings/9
	 @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	 public ResponseEntity<?> editMeetring(
			 @PathVariable("id") Long id,
			 @RequestBody Meeting incomingMeeting){
		 
		Meeting meeting = meetingService.getByID(id);
		
		if (meeting == null){
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		
		if (incomingMeeting.getTitle() != null) {
			meeting.setTitle(incomingMeeting.getTitle());
		}
		if (incomingMeeting.getDate() != null) {
			meeting.setDate(incomingMeeting.getDate());
		}
		if (incomingMeeting.getDescription() != null) {
			meeting.setDescription(incomingMeeting.getDescription());
		}		
		meetingService.updateMeeting(meeting);
		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	 }
	 
//	curl -H "Content-Type: application/json" -d '{}' -X DELETE localhost:8080/meetings/12/JestemPrzypisanyDoSpotkania4
	@RequestMapping(value = "/{meetingID}/{participantLogin}", method = RequestMethod.DELETE)
	public ResponseEntity<?> removeParticipant(
			@PathVariable("meetingID") Long meetingID,
			@PathVariable String participantLogin){
		
		Meeting meeting = meetingService.getByID(meetingID);
		if (meeting == null) {
			return new ResponseEntity("Unable to add. A meeting with id " + meetingID + " not exist.", HttpStatus.NOT_FOUND);
		}
		
		Participant participant = meetingService.getParticipantByLogin(meeting, participantLogin);
		if (participant == null) {
			return new ResponseEntity("Unable to remove. A participant with login " + participantLogin + " not association with meeting.", HttpStatus.NOT_FOUND);
		}
		
		meeting.removeParticipant(participant);
		return new ResponseEntity(HttpStatus.OK);
	}
	 
	 

}
