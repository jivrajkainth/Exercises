package uk.co.rapidware.circles;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

/**
 * Created by Jivraj on 30/07/2017.
 */
public class Friends {

    static int friendCircles(String[] friends) {
        // The constraints specified imply a definition of friend circles
        // that means a student cannot be in two friend circles

        final int studentCount = friends.length;

        // Use a queue through which to process each student in turn
        final Queue<Integer> students = new ArrayDeque<>(studentCount);
        for (int studentIndex = 0; studentIndex < studentCount; studentIndex++) {
            students.add(studentIndex);
        }

        int friendCircles = 0;

        // Process until there are no more student remaining
        while (!students.isEmpty()) {
            // There is always at least one friend circle assuming non-zero students
            friendCircles++;

            // Use another queue for processing an established friend circle
            final Queue<Integer> friendCircle = new ArrayDeque<>(studentCount);

            // Poll the student queue for next student whom will always be in a friend circle
            // as this student "begins" a circle - see comment at the top of the loop
            final Integer studentIndex = students.poll();
            friendCircle.offer(studentIndex);

            // Now process the friend circle to find further friends
            while (!friendCircle.isEmpty()) {
                // Remove the student at the head of the queue to indicate has been processed
                final Integer studentInFriendCircle = friendCircle.poll();

                // Now actually find the friends of removed student
                for (int index = 0; index < students.size(); index++) {
                    // Only test friendship against student not already in the friendship circle
                    final Integer studentToTest = students.poll();
                    if ('Y' == friends[studentInFriendCircle].charAt(studentToTest)) {
                        // Add to friend circle so this student can in turn also be processed
                        friendCircle.offer(studentToTest);
                    } else {
                        // Add back to student queue if no friendship found
                        students.offer(studentToTest);
                    }
                }
            }

        }

        return friendCircles;
    }
}
