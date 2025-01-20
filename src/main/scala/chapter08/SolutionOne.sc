import cats.effect.IO
import chapter08.MeetingTime
import chapter08.ch08_SchedulingMeetings.{calendarEntriesApiCall, createMeetingApiCall}

// Practicing creating and combining IO values (Page 284)

def calendarEntries(name: String): IO[List[MeetingTime]] = {
  IO.delay(calendarEntriesApiCall(name))
}

def createMeeting(names: List[String], meeting: MeetingTime): IO[Unit] = {
  IO.delay(createMeetingApiCall(names, meeting))
}

def scheduledMeetings(person1: String, person2: String): IO[List[MeetingTime]] = {
  for {
    person1Entries <- calendarEntries(person1)
    person2Entries <- calendarEntries(person2)
  } yield person1Entries.appendedAll(person2Entries)
}
