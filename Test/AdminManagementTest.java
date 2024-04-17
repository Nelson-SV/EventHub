import be.Event;
import be.EventStatus;
import be.Status;
import bll.EventStatusCalculator;
import bll.admin.AdminManagementLogic;
import exceptions.EventException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdminManagementTest {

    Event testMule = new Event("Test", "test event", LocalDate.now(), LocalDate.now().plusDays(15), LocalTime.now(), LocalTime.now().plusHours(78), "Easv");
    Event testMule2 = new Event("Mule", "test eventMule", LocalDate.now().plusDays(1), LocalDate.now().plusDays(15), LocalTime.now(), LocalTime.now().plusHours(78), "Easv");
    EventStatus testMuleStatus = new EventStatus(testMule);

    EventStatus testMuleStatusSecond = new EventStatus(testMule2);
    List<EventStatus> eventMules = new ArrayList<>();

    {
        Collections.addAll(eventMules, testMuleStatus, testMuleStatusSecond);
        testMuleStatus.setStatus(EventStatusCalculator.calculateStatus(testMuleStatus.getEventDTO()));
        testMuleStatusSecond.setStatus(EventStatusCalculator.calculateStatus(testMuleStatusSecond.getEventDTO()));
    }

    @Test
    void testSearchOperationValid() throws EventException {
        AdminManagementLogic adminManagementLogic = new AdminManagementLogic();
        String search = "Te";
        List<EventStatus> searchResult = adminManagementLogic.getSearchedEvents(search, eventMules);
        EventStatus result = searchResult.get(0);
        EventStatus expected = testMuleStatus;
        Assertions.assertEquals(result, expected);
    }

    @Test
    void testSearchOperationInvalid() throws EventException {
        AdminManagementLogic adminManagementLogic = new AdminManagementLogic();
        String search = "Te";
        List<EventStatus> searchResult = adminManagementLogic.getSearchedEvents(search, eventMules);
        EventStatus result = searchResult.get(0);
        EventStatus expected = testMuleStatusSecond;
        Assertions.assertNotEquals(result, expected);
    }

    @Test
    void testSortOngoingEvents() throws EventException {
//Arrange
        AdminManagementLogic adminManagementLogic = new AdminManagementLogic();
        //Act
        List<EventStatus> ongoingEvents = adminManagementLogic.getSortedEventsByStatus(this.eventMules, Status.ONGOING);
        //Assert
        int expectedSize = 1;
        Assertions.assertEquals(expectedSize, ongoingEvents.size());
    }

    @Test
    void testSortUpcomingEvents() throws EventException {
//Arrange
        AdminManagementLogic adminManagementLogic = new AdminManagementLogic();
        //Act
        List<EventStatus> ongoingEvents = adminManagementLogic.getSortedEventsByStatus(this.eventMules, Status.UPCOMING);
        //Assert
        int expectedSize = 1;
        Assertions.assertEquals(expectedSize, ongoingEvents.size());
    }

    @Test
    void testSortFinalizedEvents() throws EventException {
//Arrange
        AdminManagementLogic adminManagementLogic = new AdminManagementLogic();
        //Act
        List<EventStatus> ongoingEvents = adminManagementLogic.getSortedEventsByStatus(this.eventMules, Status.FINALIZED);
        //Assert
        int expectedSize = 0;
        Assertions.assertEquals(expectedSize, ongoingEvents.size());
    }
    @Test
    void testSortOngoingValue() throws EventException {
        AdminManagementLogic adminManagementLogic = new AdminManagementLogic();
        //Act
        List<EventStatus> ongoingEvents = adminManagementLogic.getSortedEventsByStatus(this.eventMules, Status.ONGOING);
        EventStatus result= ongoingEvents.get(0);
        //Assert
        EventStatus expectedResult = testMuleStatus;
        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    void sortByStartingDateAndTime() throws EventException {
        AdminManagementLogic admL = new AdminManagementLogic();
        List<EventStatus> sortedEvents= admL.getAllSortedEventsByStatus(eventMules);
        EventStatus secondEvent = sortedEvents.get(1);
        EventStatus expetedSecondEvennt = testMuleStatusSecond;
        Assertions.assertEquals(secondEvent,expetedSecondEvennt);
    }

    @Test
    void sortByStartingDateAndTimeInvalid() throws EventException {
        AdminManagementLogic admL = new AdminManagementLogic();
        Event event = new Event("Inside","inside",LocalDate.now().plusDays(5),LocalDate.now().plusDays(10),LocalTime.now().plusHours(48),LocalTime.MAX,"easv");
        EventStatus eventStatus = new EventStatus(event);
        eventStatus.setStatus(EventStatusCalculator.calculateStatus(eventStatus.getEventDTO()));
        eventMules.add(eventStatus);
        List<EventStatus> sortedEvents= admL.getAllSortedEventsByStatus(eventMules);
        EventStatus secondEvent = sortedEvents.get(2);
        EventStatus expetedSecondEvennt = testMuleStatusSecond;
        Assertions.assertNotEquals(secondEvent,expetedSecondEvennt);
    }



}
