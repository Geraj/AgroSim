package calculations;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JComboBox;

import appmap.ApplicationMap;

import core.Parcel;
import core.Path;
import core.PathPoints;
import core.Plants;
import core.Points;
import dao.DAOFactory;
import dao.ParcelDAO;
import dao.PathDAO;
import dao.PathPointsDAO;
import dao.PlantsDAO;
import dao.PointsDAO;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.util.measure.MeasureTool;

/**
 *  Class responsible for getting previous roads and parcels and adding them
 * to world window if necessary
 * @author Gergely Meszaros
 *
 */
public class PreviousParcels {
	
	/**
	 * Converts points to positions 
	 * @param points
	 * @return
	 */
	public ArrayList<Position> PointsToPosition(ArrayList<Points> points){
		ArrayList<Position> posAL=new ArrayList<Position>();
		Iterator<Points> itr=points.iterator();
		while (itr.hasNext()){
			Points p=itr.next();
			posAL.add(Position.fromDegrees(p.getLatitude(),p.getLongitude(),p.getElevation()));
		}
		return posAL;
	}
	/**
	 * Converts pathpoints to positions
	 * @param points
	 * @return
	 */
	public ArrayList<Position> PathPointsToPosition(ArrayList<PathPoints> points){
		ArrayList<Position> posAL=new ArrayList<Position>();
		Iterator<PathPoints> itr=points.iterator();
		while (itr.hasNext()){
			PathPoints p=itr.next();
			posAL.add(Position.fromDegrees(p.getLatitude(),p.getLongitude(),p.getElevation()));
		}
		return posAL;
	}
	/**
	 * Creates a combobox with path id's
	 * @param baseID
	 * @return combobox
	 */
	public JComboBox comboboxWithPathIDs(int baseID){
		JComboBox j=new JComboBox();
		DAOFactory df=DAOFactory.getInstance();
    	PathDAO pathDAO=df.getPathDAO();
    	try {
    		ArrayList<Path> pathAL=(ArrayList<Path>) pathDAO.getAllPathofBase(baseID);
    		Iterator<Path> it=pathAL.iterator();
    		String S[]=new String[pathDAO.getCountFromPathsWithBase(baseID)+1];
    		S[0]="Select";
    		int i=1;
    		while (it.hasNext()){
    			Path path=it.next();
    			S[i]=new String(Integer.toString(path.getID()));
    			i=i+1;
    		}
    		j=new JComboBox(S);
    	}
    		catch (Exception e)
    		{
    			e.printStackTrace();
    		}
    		
		return j;	
	}
	
	/**
	 * Creates a combobox with plant types
	 * @return combobox
	 */
	public JComboBox comboBoxWithPlantTypes(){
		JComboBox j=new JComboBox();
		DAOFactory df=DAOFactory.getInstance();
		PlantsDAO plantDao=df.getPlantsDAO();
		try {
    		ArrayList<Plants> planAL=(ArrayList<Plants>) plantDao.getAllPlants();
    		Iterator<Plants> it=planAL.iterator();
    		String S[]=new String[planAL.size()+1];
    		S[0]="Select";
    		int i=1;
    		while (it.hasNext()){
    			Plants plant=it.next();
    			S[i]=new String(plant.getType());
    			i=i+1;
    		}
    		j=new JComboBox(S);
    	}
    		catch (Exception e)
    		{
    			e.printStackTrace();
    		}
		return j;
	}
	/**
	 * Creates a combobox with parcel names.
	 * @param baseID
	 * @param type
	 * @return
	 */
	public JComboBox comboBoxWithParcelNames(int baseID,int type){
		JComboBox j=new JComboBox();
		DAOFactory df=DAOFactory.getInstance();
    	ParcelDAO parceldao=df.getParcelDao();
    	try {
    		ArrayList<Parcel> parcelAL=(ArrayList<Parcel>) parceldao.getAllParcelFromBase(baseID);
    		Iterator<Parcel> it=parcelAL.iterator();
    		String S[]=new String[parceldao.getCountFromparcelsWithBase(baseID)+1];
    		if (type==1){
    			S[0]="Base";
    		}else{
    			S[0]="Select";
    		}
    		int i=1;
    		while (it.hasNext()){
    			Parcel par=it.next();
    			S[i]=new String(par.getName());
    			i=i+1;
    		}
    		j=new JComboBox(S);
    	}
    		catch (Exception e)
    		{
    			e.printStackTrace();
    		}
    		
		return j;	
	}
	
	/**Adds measuretool layers based on path and parcel points to the WWCanvas.Adds the google earth layer.
	 * 
	 * @param worldwindow
	 * @param baseID
	 * @return
	 * @throws Exception
	 */
	public ArrayList<MeasureTool> getAllpreviousParcel(WorldWindowGLCanvas worldwindow,int baseID) throws Exception {
		//Clear all layers from WW canvas
		
		LayerList ll=worldwindow.getModel().getLayers();
		//remove some layers to avoid duplicates
		for (Layer l:ll){
			if ((l.getName().equals("Measure Tool"))||(l.getName().equals("Google Earth Aerial"))){
				worldwindow.getModel().getLayers().remove(l);
			}
		}
		//activate political boundries.  commented out becouse corrupted files were reported
		/*for (Layer layer:ll){
			if (layer.getName().equals("Political Boundaries")){
				layer.setEnabled(true);
			}
		}*/
		ArrayList<MeasureTool> measureAL = new ArrayList<MeasureTool>();
		DAOFactory daoF = DAOFactory.getInstance();
		ParcelDAO parcelD = daoF.getParcelDao();
		ArrayList<Parcel> parcels = (ArrayList<Parcel>) parcelD.getAllParcelFromBase(baseID);
		Iterator<Parcel> itrParcels = parcels.iterator();
		// Adding parcels form Db
		while (itrParcels.hasNext()) {
			Parcel parcel = itrParcels.next();
			ArrayList<Points> pointsAL = new ArrayList<Points>();
			PointsDAO pointsD = daoF.getPointsDao();
			pointsAL = (ArrayList<Points>) pointsD.getPointsByParcelID(parcel.getID());
			MeasureTool measure = new MeasureTool(worldwindow);
			measure.setPositions(PointsToPosition(pointsAL));
			measure.setFollowTerrain(true);
			measure.setFillColor(Color.red);
			measureAL.add(measure);
		}
		//addin paths 
		PathDAO pathdao=daoF.getPathDAO();
		ArrayList<Path> paths=(ArrayList<Path>) pathdao.getAllPathofBase(baseID);
		Iterator<Path> itrPaths = paths.iterator();
		while (itrPaths.hasNext()){
			try{
			Path path=itrPaths.next();
			ArrayList<PathPoints> pathpointsAL=new ArrayList<PathPoints>();
			PathPointsDAO pathPointsDAO=daoF.getPathPointsDAO();
			pathpointsAL=(ArrayList<PathPoints>) pathPointsDAO.getPathPointsByPathID(path.getID());
			MeasureTool measure=new MeasureTool(worldwindow);
			measure.setMeasureShapeType(MeasureTool.SHAPE_PATH);
			measure.setPositions(PathPointsToPosition(pathpointsAL));
			measure.setFollowTerrain(true);
			measure.setFillColor(Color.red);
			measure.setLineColor(Color.white);
			measure.setLineWidth(3);
			measureAL.add(measure);
			}catch(Exception e){e.printStackTrace();}
		}
//		//Adding google maps layers with opacity
//		GoogleEarthLayer gel=new GoogleEarthLayer();
// 		gel.setOpacity(0.75);
// 		//add layer to WorldWind
// 		worldwindow.getModel().getLayers().add(gel);
		worldwindow.redraw();
		worldwindow.getModel().getLayers().add(ApplicationMap.markerlayer);
		return measureAL;
	}
}
