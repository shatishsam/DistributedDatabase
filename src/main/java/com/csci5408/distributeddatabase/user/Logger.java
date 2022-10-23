package com.csci5408.distributeddatabase.user;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private static String lastdate = "";
    private static boolean generalD = false;
    private static boolean eventD = false;
    private static boolean queryD = false;


    public static String generalLogfile = "GeneralLogs.txt";
    public static String eventLogfile = "EventLogs.txt";
    public static String queryLogfile = "QueryLogs.txt";
    static FileWriter eventWriter;
    static FileWriter generalWriter;
    static FileWriter queryWriter;


    private boolean alllogFile () {
      try {

        File generalLog = new File(generalLogfile);

        if (generalLog.createNewFile()) {
          System.out.println("General log generator");
        }
          generalWriter = new FileWriter(generalLog, true);

        File queryLog = new File(queryLogfile);
        if (queryLog.createNewFile()) {
          System.out.println("Query log generator");
        }

          queryWriter = new FileWriter(queryLog, true);


        File eventLog = new File(eventLogfile);
        if (eventLog.createNewFile()) {
          System.out.println("Event log generator");
        }
        eventWriter = new FileWriter(eventLog, true);
      } catch (Exception e) {
        return false;
      }
      return false;
    }

    private static String getDate() {

      Date date = new Date();
      SimpleDateFormat logDate = new SimpleDateFormat("dd/MMM/YYYY - EEE");
      String lDate = logDate.format(date);
      lDate = "\n********************** [ " + lDate + " ] ******************** \n";

      return lDate;
    }

    
    public static void eventLogger(String input) throws IOException {
      File eventLog = new File(eventLogfile);

        String ldate;
        ldate = getDate();

        if (ldate.equals(lastdate) && eventD) {
          ldate = "";
        } else {
          lastdate = ldate;
        }
        eventWriter = new FileWriter(eventLog, true);
        lastdate = ldate;
        eventD = true;
        eventWriter.append(ldate);
        eventWriter.append(input);
        eventWriter.flush();

    }

  public static void generalLogger(String input) throws IOException {
    File generalLog = new File(generalLogfile);

      String ldate;
      ldate = getDate();

      if (ldate.equals(lastdate) && generalD) {
        ldate = "";
      } else {
        lastdate = ldate;
      }
      generalWriter = new FileWriter(generalLog, true);
//        generalD = true;
      generalWriter.append(input);
      generalWriter.append(ldate);
      generalWriter.flush();
      }


  public static void queryLogger(String input) throws IOException {
        File queryLog = new File(queryLogfile);
        String ldate;
        ldate = getDate();

        if (ldate.equals(lastdate) && queryD) {
          ldate = "";
        } else {
          lastdate = ldate;
        }
        lastdate = ldate;
        queryD = true;
        queryWriter = new FileWriter(queryLog, true);
        queryWriter.append(ldate);
        queryWriter.append(input);
        queryWriter.flush();

    }
  }
