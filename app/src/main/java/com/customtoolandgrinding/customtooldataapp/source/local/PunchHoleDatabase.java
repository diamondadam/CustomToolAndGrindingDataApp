package com.customtoolandgrinding.customtooldataapp.source.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.customtoolandgrinding.customtooldataapp.models.PunchHole;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {PunchHole.class}, version = 1, exportSchema = false)
public abstract class PunchHoleDatabase extends RoomDatabase {

    public abstract PunchHoleDao clockDao();

    private static volatile PunchHoleDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static PunchHoleDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (PunchHoleDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            PunchHoleDatabase.class, "punch_hole_database").build();
                }
            }
        }
        return INSTANCE;
    }
     public class ListNode {
        int val;
        ListNode next;
        String s;

        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
      }


    class Solution {
        public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
            int multiplyer = 0;
            int sum = 0;

            while(l1 != null || l2 != null){
                if(l1.next != null && l2.next != null){
                    System.out.printf("l1: %d, l2: %d ", l1.val, l2.val);
                    sum = (int) (l1.val + l2.val + (sum * (Math.pow(multiplyer, 10))));
                    l1 = l1.next;
                    l2 = l2.next;
                }
                else if(l1.next != null){
                    sum = l1.val + (sum * (multiplyer * 10));
                    l1 = l1.next;
                }
                else if(l2.next != null){
                    sum = l2.val + (sum * (multiplyer * 10));
                    l2 = l2.next;
                }
                multiplyer++;
            }

            sum = l1.val + l2.val + (sum * (multiplyer * 10));
            multiplyer++;

            return createNodes(multiplyer, sum);
        }


        private ListNode createNodes(int multiplyer, int sum){
            System.out.println("Creating Nodes");
            System.out.println("Multiplyer: " + multiplyer);
            System.out.println("Sum: "+ sum);
            //Create head node
            ListNode head = new ListNode(sum % (multiplyer * 10));
            sum /= (multiplyer * 10);
            multiplyer--;
            ListNode iter = head;

            //Create the rest of the nodes
            for(; multiplyer >= 0; multiplyer--){
                System.out.println("For Loop");
                System.out.println("Multiplyer: "+ multiplyer);
                System.out.println("Sum: "+ sum);
                iter.next = new ListNode(sum % (multiplyer * 10));
                sum /= (multiplyer * 10);
                multiplyer--;
                iter = iter.next;
            }
            return head;
        }


    }
}
