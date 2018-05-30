package appmap;

import gov.nasa.worldwind.*;
import gov.nasa.worldwind.avlist.*;
import gov.nasa.worldwind.awt.*;
import gov.nasa.worldwind.event.*;
import gov.nasa.worldwind.exception.*;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.*;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.markers.BasicMarker;
import gov.nasa.worldwind.render.markers.BasicMarkerAttributes;
import gov.nasa.worldwind.render.markers.BasicMarkerShape;
import gov.nasa.worldwind.render.markers.Marker;
import gov.nasa.worldwind.util.*;
import gov.nasa.worldwind.util.measure.MeasureTool;
import gov.nasa.worldwind.util.measure.MeasureToolController;

import javax.swing.*;
import calculations.PreviousParcels;
import calculations.animation.AnimationStateHandler;
import calculations.animation.log.Animation;
import control.GlobeListener;
import control.observer.EventDispatcher;
import control.observer.StateListener;
import control.observer.StateMachineEvents;
import core.Base;
import dao.BaseDAO;
import dao.DAOFactory;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

/**
 * Provides a application Map uses singleton pattern to ensure that only one map
 * runs at a time.
 * 
 * @author Gergely Meszaros
 * 
 */
public class ApplicationMap {
	private static ApplicationMap map;
	
	public static MarkerLayer markerlayer;
	public static int baseID;
	public static Base base;
	
	static {
		if (Configuration.isMacOS()) {
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "World Wind Application");
			System.setProperty("com.apple.mrj.application.growbox.intrudes", "false");
			System.setProperty("apple.awt.brushMetalLook", "true");
		} else if (Configuration.isWindowsOS()) {
			System.setProperty("sun.awt.noerasebackground", "true"); // prevents
																		// flashing
																		// during
																		// window
																		// resizing
		}
	}
	/**
	 * Class creating a jPanel which contains the virtual globe
	 * 
	 * @author Geraj
	 * 
	 */
	public static class AppPanel extends JPanel {
		private static final long serialVersionUID = -8023143298746865842L;
		protected WorldWindowGLCanvas wwd;
		protected StatusBar statusBar;

		public AppPanel(Dimension canvasSize) {
			super(new BorderLayout());

			this.wwd = this.createWorldWindow();
			Configuration.setValue(AVKey.OFFLINE_MODE, this.wwd);
			// TODO offline mode!!!

			this.wwd.setPreferredSize(canvasSize);
			// include status bar
			this.statusBar = new StatusBar();
			this.add(statusBar, BorderLayout.PAGE_END);
			this.statusBar.setEventSource(wwd);
			// Create the default model as described in the current worldwind
			// properties.
			Model m = (Model) WorldWind.createConfigurationComponent(AVKey.MODEL_CLASS_NAME);
			this.wwd.setModel(m);

			Marker marker = new BasicMarker(Position.fromDegrees(base.getLatitude(), base.getLongitude(), 0),
					new BasicMarkerAttributes(Material.GREEN, BasicMarkerShape.SPHERE, 1d, 10, 5));

			markerlayer = new MarkerLayer();
			markerlayer.setOverrideMarkerElevation(true);
			markerlayer.setKeepSeparated(false);
			markerlayer.setElevation(0);

			ArrayList<Marker> markers = new ArrayList<Marker>();
			markers.add(marker);
			markerlayer.setMarkers(markers);

			this.add(this.wwd, BorderLayout.CENTER);
		}

		protected WorldWindowGLCanvas createWorldWindow() {
			return new WorldWindowGLCanvas();
		}

		public WorldWindowGLCanvas getWwd() {
			return wwd;
		}

		public StatusBar getStatusBar() {
			return statusBar;
		}
	}

	/**
	 * Class for building the frame of the map
	 * 
	 * @author Geraj
	 * 
	 */
	protected static class AppFrame extends JFrame {
		private static final long serialVersionUID = -2819826227057098182L;
		private Dimension canvasSize = new Dimension(1024, 700);
		private JPanel panel;
		protected AppPanel wwjPanel;

		static {
			if (Configuration.isMacOS()) {
				System.setProperty("apple.laf.useScreenMenuBar", "true");
				System.setProperty("com.apple.mrj.application.apple.menu.about.name", "World Wind Application");
				System.setProperty("com.apple.mrj.application.growbox.intrudes", "false");
				System.setProperty("apple.awt.brushMetalLook", "true");
			} else if (Configuration.isWindowsOS()) {
				System.out.println("Windows");
				System.setProperty("sun.awt.noerasebackground", "true"); // prevents
																			// flashing
																			// during
																			// window
																			// resizing
			}
		}

		private AppFrame() {
			this.initialize();
			panel = new JPanel();
			MeasureTool measureTool = new MeasureTool(this.getWwd());
			measureTool.setController(new MeasureToolController());
			// add the toolpanel
			panel.add(new ParcelToolPanel(this.getWwd(), measureTool, this));
			this.getContentPane().add(panel, BorderLayout.WEST);
		}

		/**
		 * Initialize the map
		 */
		private void initialize() {
			// Create the WorldWindow.
			this.wwjPanel = this.createAppPanel(this.canvasSize);
			this.wwjPanel.setPreferredSize(canvasSize);
			// Adding previous parcells
			try {
				// System.out.println(baseID);
				PreviousParcels preparcels = new PreviousParcels();
				// add previous parcels to map
				preparcels.getAllpreviousParcel(this.wwjPanel.getWwd(), baseID);
				this.wwjPanel.getWwd().addSelectListener(new GlobeListener(this.getWwd()));
				// add listener
			} catch (Exception e) {

			}

			this.getContentPane().add(wwjPanel, BorderLayout.CENTER);
			this.wwjPanel.getWwd().addRenderingExceptionListener(new RenderingExceptionListener() {
				public void exceptionThrown(Throwable t) {
					if (t instanceof WWAbsentRequirementException) {
						String message = "Computer does not meet minimum graphics requirements.\n";
						message += "Please install up-to-date graphics driver and try again.\n";
						message += "Reason: " + t.getMessage() + "\n";
						message += "This program will end when you press OK.";

						JOptionPane.showMessageDialog(AppFrame.this, message, "Unable to Start Program",
								JOptionPane.ERROR_MESSAGE);
						System.exit(-1);
					}
				}
			});

			this.pack();
			AnimationStateHandler animationStateHandler =  new AnimationStateHandler(this.wwjPanel.getWwd(), baseID);
			EventDispatcher.getInstance().registerListener(animationStateHandler,
					StateMachineEvents.VEHICLE_STARTED);
			EventDispatcher.getInstance().registerListener(animationStateHandler,
					StateMachineEvents.VEHICLE_MOVING_TO_PARCEL);
			EventDispatcher.getInstance().registerListener(animationStateHandler,
					StateMachineEvents.VEHICLE_STOPPED);
			EventDispatcher.getInstance().registerListener(animationStateHandler,
					StateMachineEvents.VEHICLE_WORKING);
			EventDispatcher.getInstance().registerListener(animationStateHandler,
					StateMachineEvents.SIMULATION_EVENT);

			// Center the application on the screen.
			WWUtil.alignComponent(null, this, AVKey.CENTER);

			this.setResizable(true);
		}

		protected AppPanel createAppPanel(Dimension canvasSize) {
			return new AppPanel(canvasSize);
		}

		public Dimension getCanvasSize() {
			return canvasSize;
		}

		public AppPanel getWwjPanel() {
			return wwjPanel;
		}

		public WorldWindowGLCanvas getWwd() {
			return this.wwjPanel.getWwd();
		}

		public StatusBar getStatusBar() {
			return this.wwjPanel.getStatusBar();
		}
	}

	private ApplicationMap() {
		startmap(baseID);
	}

	/**
	 * Only one map should run at a time.If there is no map running it creates one.
	 * 
	 * @return The running map
	 */
	public static synchronized ApplicationMap getMapInstance() {
		if (map == null) {
			map = new ApplicationMap();
		}
		return map;
	}

	public static boolean isMapRunning() {
		if (map != null) {
			return true;
		}
		return false;
	}

	private static AppFrame start(String appName, AppFrame parcelToolUsage) {
		if (Configuration.isMacOS() && appName != null) {
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", appName);
		}

		try {
			final AppFrame frame = parcelToolUsage;
			frame.setTitle(appName);
			frame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					{
						map = null;
						Animation.running = false;
						frame.dispose();
					}
				}
			});
			return frame;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Start a map using a given base id.
	 * 
	 * @param baseid
	 */
	private void startmap(int baseid) {
		System.out.println("Starting Map....");
		DAOFactory df = DAOFactory.getInstance();
		BaseDAO baseDAO = df.getBaseDAO();
		base = new Base();
		try {
			base = baseDAO.getBaseByID(baseid);
		} catch (Exception e) {
			System.out.println("Error starting map");
		}
		// Configuration.setValue(AVKey.GLOBE_CLASS_NAME,
		// EarthFlat.class.getName());

		Configuration.setValue(AVKey.INITIAL_ALTITUDE, 10000);
		Configuration.setValue(AVKey.INITIAL_PITCH, 10);
		Configuration.setValue(AVKey.OFFLINE_MODE, false);
		Position p = Position.fromDegrees(base.getLatitude(), base.getLongitude(), 0);
		Configuration.setValue(AVKey.INITIAL_LATITUDE, p.latitude.getDegrees());
		Configuration.setValue(AVKey.INITIAL_LONGITUDE, p.longitude.getDegrees());
		JFrame f = ApplicationMap.start("Parcel map", new AppFrame());
		f.setVisible(true);

	}

}
