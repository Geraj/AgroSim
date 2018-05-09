package control;

import java.awt.Font;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import appmap.ApplicationMap;

import core.Parcel;
import core.Path;
import core.Plants;
import dao.DAOFactory;
import dao.ParcelDAO;
import dao.PathDAO;
import dao.PathPointsDAO;
import dao.PlantsDAO;
import dao.PointsDAO;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.event.SelectEvent;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.AnnotationLayer;
import gov.nasa.worldwind.render.AnnotationAttributes;
import gov.nasa.worldwind.render.GlobeAnnotation;
import gov.nasa.worldwind.render.markers.BasicMarker;

/**
 * Implementation of selection listener to be used on WWC selection events
 * 
 * @author Gergely Meszaros
 *
 */
public class GlobeListener implements SelectListener {
  private WorldWindowGLCanvas wwc;

  private AnnotationLayer layer;

  private Object lastDisplayed = null;

  private static GlobeAnnotation gg;

  private static Logger logger = Logger.getLogger(GlobeListener.class);

  public GlobeListener(WorldWindowGLCanvas wwc) {
    this.wwc = wwc;
    this.layer = new AnnotationLayer();
  }

  public void selected(SelectEvent event) {
    // annotiation generation
    wwc.getModel().getLayers().remove(layer);
    layer.removeAllAnnotations();
    if (event.getEventAction().equals(SelectEvent.ROLLOVER)) {

      if (event.getTopObject() instanceof GlobeAnnotation) {
        this.lastDisplayed = event.getObjects().get(0);
        AnnotationAttributes annotationAttribute = new AnnotationAttributes();

        annotationAttribute.setFont(Font.decode("Arial-BOLD-12"));
        annotationAttribute.setDistanceMaxScale(1);
        annotationAttribute.setDistanceMinScale(1);
        GlobeAnnotation e = (GlobeAnnotation) event.getTopObject();
        // System.out.println(e.getPosition().latitude.degrees);
        DAOFactory daoF = DAOFactory.getInstance();
        PointsDAO pointDao = daoF.getPointsDao();
        ParcelDAO parcelDao = daoF.getParcelDao();
        Integer parcelid = 0;
        String annotationMesage = "";
        try {
          parcelid = pointDao.getPositionsParcelID(e.getPosition());
          Parcel currentParcel = parcelDao.getParcelByID(parcelid);
          PathDAO pathDAO = daoF.getPathDAO();
          PathPointsDAO pathpointDAO = daoF.getPathPointsDAO();
          Set<Integer> pathIDSet = new TreeSet<Integer>();
          pathIDSet = pathpointDAO.getPositionsPathID(e.getPosition());

          Iterator<Integer> it = pathIDSet.iterator();
          while (it.hasNext()) {
            Path currentPath = pathDAO.getPathByID(it.next());
            // currently on path point
            if (currentPath.getID() != -1) {
              String length = Double.toString(currentPath.getLength() / 1000).substring(0, 6);
              String fromto = null;
              if ((currentPath.getTo() != 0) && (currentPath.getFrom() != 0)) {
                Parcel parcelto = parcelDao.getParcelByID(currentPath.getTo());
                Parcel parcelfrom = parcelDao.getParcelByID(currentPath.getFrom());
                fromto = parcelfrom.getName() + ":" + parcelto.getName();
              } else {
                if (currentPath.getTo() == 0) {
                  Parcel parcelfrom = parcelDao.getParcelByID(currentPath.getFrom());
                  fromto = parcelfrom.getName() + " : Base";
                } else {
                  Parcel parcelto = parcelDao.getParcelByID(currentPath.getTo());
                  fromto = "Base : " + parcelto.getName();
                }
              }

              annotationMesage += "PathID: " + currentPath.getID() + "\n Length: " + length + "km "
                  + " \n Between: " + fromto + "\n";
            }
          }
          // currently on parcel point
          if (currentParcel.getArea() >= 0) {
            PlantsDAO plantDao = daoF.getPlantsDAO();
            Plants p = plantDao.getPlantByID(currentParcel.getPlantID());
            if (Double.toString(currentParcel.getArea()).length() > 6) {
              annotationMesage += "Code:" + currentParcel.getName() + "\n Area: " + Double.toString(
                  currentParcel.getArea()).substring(0, 6) + " ha \n Plant:" + p.getType() + "\n";
              int seed = (int) (currentParcel.getArea() * p.getSeed());

              int inc = (int) (currentParcel.getArea() * p.getIncome());
              annotationMesage += "Seed price:" + seed + "\n Value:" + inc + "\n";
            } else {
              int seed = (int) (currentParcel.getArea() * p.getSeed());
              int inc = (int) (currentParcel.getArea() * p.getIncome());
              annotationMesage += "Seed price:" + seed + "\n Value:" + inc + "\n";
            }

          }
          if (annotationMesage != "") {

            gg = new GlobeAnnotation(annotationMesage, e.getPosition(), annotationAttribute);
            gg.getAttributes().setVisible(true);
            gg.setAlwaysOnTop(true);

            layer.addAnnotation(gg);
            wwc.getModel().getLayers().add(layer);
            // logger.info("[F2.3] distplaying information about object under pointer");

          }
        } catch (Exception ex) {
          // JOptionPane.showMessageDialog(this,ex.getMessage());
          System.out.println("error on hower");
          System.out.println(ex.getMessage());
        }

      }
      if (event.getTopObject() instanceof BasicMarker) {
        AnnotationAttributes townAttr = new AnnotationAttributes();
        layer = new AnnotationLayer();
        townAttr.setFont(Font.decode("Arial-BOLD-12"));
        townAttr.setDistanceMaxScale(1);
        townAttr.setDistanceMinScale(1);

        gg = new GlobeAnnotation(" BASE ", Position.fromDegrees(ApplicationMap.base.getLatitude(),
            ApplicationMap.base.getLongitude(), 0), townAttr);
        gg.getAttributes().setVisible(true);
        gg.setAlwaysOnTop(true);
        layer.addAnnotation(gg);
        wwc.getModel().getLayers().add(layer);
      }
    }
  }
}
