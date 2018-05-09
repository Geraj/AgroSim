package calculations.animation.log;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.JLabel;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.AnnotationLayer;
import gov.nasa.worldwind.render.AnnotationAttributes;
import gov.nasa.worldwind.render.GlobeAnnotation;

/**
 * Animation class. This class is responsible for drawing the animation based on the log files
 * 
 * @author Gergely Meszaros
 * 
 */
public class Animation implements Runnable {
  public final WorldWindow wwd;

  private int baseID;

  public static int speed = 250;

  public static boolean running = false;

  private ArrayList<AnimationDataStorage> annimationdataStorageAL;// arraylist

  private JLabel timelable;

  private int precision;

  /**
   * Constructs the data for the annimation
   * 
   * @param wwd - WorldWindow
   * @param precision - Frames / simulation minutes
   * @param drawParcel - Consider time spent on parcel
   * @param annimationtimelable - lable on parceltoolpanel
   * @throws Exception
   */
  public Animation(WorldWindow wwd, int precision, boolean drawParcel, JLabel annimationtimelable)
      throws Exception {
    this.wwd = wwd;
    this.timelable = annimationtimelable;
    this.precision = precision;
    Animation.running = true;
    // data in arraylists;
    annimationdataStorageAL = new ArrayList<AnimationDataStorage>();
    ArrayList<String> masterList = getFiles("log/Master.txt");
    String operation = "";// actual operation;
    AnimationDataStorage ads = new AnimationDataStorage();
    for (int i = 0; i < masterList.size(); i++) {
      String line = masterList.get(i);
      StringTokenizer st = new StringTokenizer(line);

      // first line
      if (i == 0) {
        operation = st.nextToken();
        String machine = st.nextToken();
        String file = "log/" + st.nextToken();
        System.out.println(file);
        AnimationData ad = new AnimationData(file, precision, drawParcel);
        ads.annimationData.add(ad);
        ads.machine.add(machine);
        ads.operation = operation;
      } else {
        // the same operation in next file
        String curentOP = st.nextToken();
        if (curentOP.equals(ads.operation)) {
          ads.machine.add(st.nextToken());
          String file = "log/" + st.nextToken();
          AnimationData ad = new AnimationData(file, precision, drawParcel);
          ads.annimationData.add(ad);
        } else {
          annimationdataStorageAL.add(ads);
          ads = new AnimationDataStorage();
          operation = curentOP;
          ads.operation = operation;
          ads.machine.add(st.nextToken());
          String file = "log/" + st.nextToken();
          AnimationData ad = new AnimationData(file, precision, drawParcel);
          ads.annimationData.add(ad);
        }
      }
    }
    if (!ads.operation.equals("-1"))
      annimationdataStorageAL.add(ads);
  }

  @Override
  public void run() {
    try {

      AnnotationAttributes annotationAttribute = new AnnotationAttributes();
      AnnotationLayer layer = new AnnotationLayer();
      annotationAttribute.setFont(Font.decode("Arial-BOLD-12"));
      annotationAttribute.setDistanceMaxScale(1);
      annotationAttribute.setDistanceMinScale(1);

      // for every operation data
      for (int i = 0; i < annimationdataStorageAL.size(); i++) {
        layer = new AnnotationLayer();
        AnimationDataStorage ads = annimationdataStorageAL.get(i);
        String operation = ads.operation;
        Integer t = 0;
        boolean arethereanyelementsinlist = true;
        while (arethereanyelementsinlist) {
          arethereanyelementsinlist = false;
          Map<Position, String> mappos = new HashMap<Position, String>();
          t++;
          for (int j = 0; j < ads.annimationData.size(); j++) {
            timelable.setText("Time: " + convertMinutes(t / precision));
            // if that machines has cordinates to draw (its list is
            // not empty) make an annotation

            if (!ads.annimationData.get(j).annimationLatitudeList.isEmpty()) {

              arethereanyelementsinlist = true;
              Position p = Position.fromDegrees(ads.annimationData.get(
                  j).annimationLatitudeList.pop(), ads.annimationData.get(
                      j).annimationLongitudeList.pop(), 0);

              if (mappos.containsKey(p)) {
                String s = mappos.get(p);
                s = s + " " + ads.machine.get(j);
                mappos.put(p, s);
              } else {
                mappos.put(p, ads.machine.get(j));
              }
            }
          }
          Iterator it = mappos.entrySet().iterator();
          while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            GlobeAnnotation gg = new GlobeAnnotation((String) pairs.getValue() + " " + operation,
                (Position) pairs.getKey(), annotationAttribute);
            gg.getAttributes().setVisible(true);
            gg.setAlwaysOnTop(false);
            layer.addAnnotation(gg);
            // System.out.println(pairs.getKey() + " = " + );
          }
          wwd.getModel().getLayers().add(layer);
          Thread.sleep(Animation.speed);
          wwd.getModel().getLayers().remove(layer);
          layer = new AnnotationLayer();
        }

      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    Animation.running = false;
  }

  /**
   * @param filename - Masterfile
   * @return List of the animation files
   * @throws Exception
   */
  private ArrayList<String> getFiles(String filename) throws Exception {
    ArrayList<String> arrayListFiles = new ArrayList<String>();
    {
      FileInputStream fstream = new FileInputStream(filename);
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String strLine;
      // Read File Line By Line
      while ((strLine = br.readLine()) != null) {
        try {
          this.baseID = Integer.parseInt(strLine);
        } catch (NumberFormatException e) {

        }
        arrayListFiles.add(strLine + ".ani");
      }
      // Close the input stream
      in.close();
    }
    return arrayListFiles;
  }

  /**
   * Converts minutes into Day Time format
   * 
   * @param minute
   * @return String with the time
   */
  public String convertMinutes(int minute) {
    String converted = "";
    int days = minute / 600;// 10 hour workdays
    minute = minute - days * 600;
    int hours = (minute / 60) + 8;// work starts at 8
    minute = minute % 60;
    if (minute < 10)
      converted = "Day:" + days + " Time:" + hours + ":0" + minute + " ";
    else
      converted = "Day:" + days + " Time:" + hours + ":" + minute + " ";
    return converted;
  }

}
