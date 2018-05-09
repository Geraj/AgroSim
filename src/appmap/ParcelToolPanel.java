package appmap;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.util.UnitsFormat;
import gov.nasa.worldwind.util.measure.MeasureTool;
import gov.nasa.worldwind.util.measure.MeasureToolController;

import javax.swing.*;
import javax.swing.border.*;

import org.apache.log4j.Logger;

import control.Control2;

import calculations.PreviousParcels;
import calculations.animation.log.Animation;
//import appmap.ParcelToolUsage.AppFrame;
import core.Parcel;
import core.Path;

import dao.DAOFactory;
import dao.ParcelDAO;
import dao.PathDAO;
import dao.PlantsDAO;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.ArrayList;

/**
 * Class responsable for creating the parceltoolPanel
 * 
 * @author Gergely Meszaros
 * 
 */
public class ParcelToolPanel extends JPanel {
	private static Logger logger = Logger.getLogger(ParcelToolPanel.class);
	private static final long serialVersionUID = 1L;
	public final WorldWindow wwd;
	private MeasureTool measureTool;
	private JComboBox shapeCombo;
	private JComboBox unitsCombo;
	private JComboBox neighbore1Combo;
	private JComboBox neighbore2Combo;
	private JButton newButton;
	private JButton pauseButton;
	private JButton endButton;
	private JButton saveButton;
	private JCheckBox drawParcelTimeOnAnimation;
	private JButton annimateButon;
	private JButton saveneighboreButton;
	private JLabel[] pointLabels;
	private JLabel lengthLabel;
	private JLabel areaLabel;
	private JPanel pathBetweenPanel;
	private JTextArea parcelnameTextarea;
	private JPanel deletePanel;
	private JButton deleteselectedButton;
	private JComboBox deleteshapeCombo;
	private JComboBox deletewhatCombo;
	public JFrame appFrame;
	public JPanel parcelnamePanel;
	public JPanel cultivatePanel;
	public JPanel speedPanel;
	public JLabel annimationtimelable;
	private JTextField frameperminText;

	public ParcelToolPanel(WorldWindow wwdObject,
			MeasureTool measureToolObject, JFrame appFrame) {
		super(new BorderLayout());
		this.appFrame = appFrame;
		this.wwd = wwdObject;
		this.measureTool = measureToolObject;
		this.makePanel(new Dimension(200, 300));

		// Handle measure tool events
		measureTool.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				// Add, remove or change positions
				if (event.getPropertyName().equals(
						MeasureTool.EVENT_POSITION_ADD)
						|| event.getPropertyName().equals(
								MeasureTool.EVENT_POSITION_REMOVE)
						|| event.getPropertyName().equals(
								MeasureTool.EVENT_POSITION_REPLACE))
					fillPointsPanel(); // Update position list when changed

				// The tool was armed / disarmed
				else if (event.getPropertyName()
						.equals(MeasureTool.EVENT_ARMED)) {
					if (measureTool.isArmed()) {
						newButton.setEnabled(false);
						pauseButton.setText("Pause");
						pauseButton.setEnabled(true);
						endButton.setEnabled(true);
						((Component) wwd).setCursor(Cursor
								.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
					} else {
						newButton.setEnabled(true);
						pauseButton.setText("Pause");
						pauseButton.setEnabled(false);
						endButton.setEnabled(false);
						((Component) wwd).setCursor(Cursor.getDefaultCursor());
					}

				}

				// Metric changed - sent after each render frame
				else if (event.getPropertyName().equals(
						MeasureTool.EVENT_METRIC_CHANGED)) {
					updateMetric();
				}

			}
		});
	}

	public MeasureTool getMeasureTool() {
		return this.measureTool;
	}

	private void makePanel(Dimension size) {
		// Shape combo
		JPanel shapePanel = new JPanel(new GridLayout(1, 2, 5, 5));
		shapePanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		shapePanel.add(new JLabel("Shape:"));
		shapeCombo = new JComboBox(
				new String[] { "Polygon", "Distance", "Path" });
		measureTool.setMeasureShapeType(MeasureTool.SHAPE_POLYGON);
		shapeCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String item = (String) ((JComboBox) event.getSource())
						.getSelectedItem();
				if (item.equals("Distance")) {
					measureTool.setMeasureShapeType(MeasureTool.SHAPE_LINE);
					parcelnamePanel.setVisible(false);
				} else if (item.equals("Polygon")) {
					measureTool.setMeasureShapeType(MeasureTool.SHAPE_POLYGON);
				} else if (item.equals("Path")) {
					parcelnamePanel.setVisible(false);
					measureTool.setMeasureShapeType(MeasureTool.SHAPE_PATH);
					measureTool.setFollowTerrain(true);

				}
			}
		});
		shapePanel.add(shapeCombo);
		// Units combo
		JPanel unitsPanel = new JPanel(new GridLayout(1, 2, 5, 5));
		unitsPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		unitsPanel.add(new JLabel("Units:"));
		unitsCombo = new JComboBox(new String[] { "M/M\u00b2", "KM/KM\u00b2",
				"KM/Hectare" });
		unitsCombo.setSelectedItem("KM/KM\u00b2");
		unitsCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String item = (String) ((JComboBox) event.getSource())
						.getSelectedItem();
				if (item.equals("M/M\u00b2")) {
					measureTool.getUnitsFormat().setLengthUnits(
							UnitsFormat.METERS);
					measureTool.getUnitsFormat().setAreaUnits(
							UnitsFormat.SQUARE_METERS);
				} else if (item.equals("KM/KM\u00b2")) {
					measureTool.getUnitsFormat().setLengthUnits(
							UnitsFormat.KILOMETERS);
					measureTool.getUnitsFormat().setAreaUnits(
							UnitsFormat.SQUARE_KILOMETERS);
				} else if (item.equals("KM/Hectare")) {
					measureTool.getUnitsFormat().setLengthUnits(
							UnitsFormat.KILOMETERS);
					measureTool.getUnitsFormat().setAreaUnits(
							UnitsFormat.HECTARE);
				}
			}
		});
		unitsPanel.add(unitsCombo);

		// Action buttons

		JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 5, 5));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

		newButton = new JButton("New");
		newButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				measureTool.clear();
				measureTool.setArmed(true);
				((Component) wwd).setCursor(Cursor
						.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

				if (shapeCombo.getSelectedItem().toString().equals("Polygon")) {
					parcelnamePanel.setVisible(true);
					measureTool.getUnitsFormat().setLengthUnits(
							UnitsFormat.KILOMETERS);
					measureTool.getUnitsFormat().setAreaUnits(
							UnitsFormat.HECTARE);
				}
				if (shapeCombo.getSelectedItem().toString().equals("Distance")) {
					measureTool.getUnitsFormat().setLengthUnits(
							UnitsFormat.METERS);
				}
				endButton.setEnabled(true);
				saveButton.setEnabled(false);
				pathBetweenPanel.setVisible(false);
				deletePanel.setVisible(false);
			}
		});
		buttonPanel.add(newButton);
		newButton.setEnabled(true);

		pauseButton = new JButton("Pause");
		pauseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				measureTool.setArmed(!measureTool.isArmed());
				pauseButton
						.setText(!measureTool.isArmed() ? "Resume" : "Pause");
				pauseButton.setEnabled(true);
				((Component) wwd).setCursor(!measureTool.isArmed() ? Cursor
						.getDefaultCursor() : Cursor
						.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			}
		});
		buttonPanel.add(pauseButton);
		pauseButton.setEnabled(false);

		endButton = new JButton("End");
		endButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				measureTool.setArmed(false);
				saveButton.setEnabled(true);
				endButton.setEnabled(false);
				if (shapeCombo.getSelectedItem().toString().equals("Path")) {
					saveButton.setEnabled(false);
					PreviousParcels previous = new PreviousParcels();
					pathBetweenPanel.removeAll();
					pathBetweenPanel.add(new JLabel("Drawn to:"));
					// pathBetweenPanel.remove(neighbore1Combo);
					neighbore1Combo = previous.comboBoxWithParcelNames(
							ApplicationMap.baseID, 1);
					pathBetweenPanel.add(neighbore1Combo);
					pathBetweenPanel.add(new JLabel("Drawn from:"));
					// pathBetweenPanel.remove(neighbore2Combo);
					neighbore2Combo = previous.comboBoxWithParcelNames(
							ApplicationMap.baseID, 1);
					pathBetweenPanel.add(neighbore2Combo);
					pathBetweenPanel.add(saveneighboreButton);
					pathBetweenPanel.setVisible(true);
				}

			}
		});

		buttonPanel.add(endButton);
		endButton.setEnabled(false);

		saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent actionEvent) {
				DAOFactory daoF = DAOFactory.getInstance();
				ParcelDAO parcelD = daoF.getParcelDao();
				parcelnamePanel.setVisible(false);
				ArrayList<Position> posAL = new ArrayList<Position>();
				posAL = (ArrayList<Position>) measureTool.getPositions();
				measureTool.getArea();
				Parcel p = new Parcel();
				p.setArea(measureTool.getArea() / 10000);
				p.setName(parcelnameTextarea.getText());
				if (p.getName().equals("")) {
					JOptionPane
							.showMessageDialog(appFrame,
									"Parcel could not be saved, becouse no parcel code was inputed!");
				} else {
					try {
						// save the shape
						if (parcelD.getParcelIdByName(p.getName()) == 0) {
							parcelD.insertParcel(p, posAL,
									ApplicationMap.baseID);
							PreviousParcels pP = new PreviousParcels();
							// redraw objects in wwj window
							pP.getAllpreviousParcel((WorldWindowGLCanvas) wwd,
									ApplicationMap.baseID);
							measureTool = new MeasureTool(wwd);
							measureTool
									.setController(new MeasureToolController());
							measureTool
									.setMeasureShapeType(MeasureTool.SHAPE_POLYGON);
							endButton.setEnabled(false);
							logger.info("[F1.9] parcel created with name "
									+ p.getName() + " area " + p.getArea());
						} else {
							JOptionPane.showMessageDialog(appFrame,
									"Parcel with that code allready exists");
						}

					} catch (Exception e) {
						JOptionPane.showMessageDialog(appFrame, e.getMessage());
						e.printStackTrace();
					}
				}
				saveButton.setEnabled(false);
				// posArrayL=(ArrayList<Position>) measureTool.getPositions();
			}
		});
		buttonPanel.add(saveButton);
		saveButton.setEnabled(false);

		JButton deleteButton = new JButton("Delete");
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deletePanel.add(deleteshapeCombo);
				deletePanel.setVisible(true);
				deletePanel.removeAll();
				deletePanel.add(deleteshapeCombo);
				// parcelnamePanel.setVisible(false);
			}

		});
		// buttonPanel.add(deleteButton);

		// Manage Parcel/path panel
		JPanel manageParcelPathPanel = new JPanel(new GridLayout(1, 2, 0, 4));
		manageParcelPathPanel.setBorder(new CompoundBorder(new TitledBorder(
				"Manage"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		manageParcelPathPanel.add(deleteButton);
		JButton cultivateButton = new JButton("Cultivate");
		manageParcelPathPanel.add(cultivateButton);
		cultivateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cultivatePanel.setVisible(true);
				PreviousParcels p = new PreviousParcels();
				cultivatePanel.removeAll();
				final JComboBox parcelscombo = p.comboBoxWithParcelNames(
						ApplicationMap.baseID, 0);
				cultivatePanel.add(new JLabel("Parcel:"));
				cultivatePanel.add(parcelscombo);
				cultivatePanel.add(new JLabel("Plant:"));
				final JComboBox plantscombo = p.comboBoxWithPlantTypes();
				cultivatePanel.add(plantscombo);
				JButton jb = new JButton("Confirm");
				jb.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						String s1 = plantscombo.getSelectedItem().toString();
						String s2 = parcelscombo.getSelectedItem().toString();
						if ((!s1.equals("Select")) && (!s2.equals("Select"))) {
							DAOFactory df = DAOFactory.getInstance();
							ParcelDAO parcelDao = df.getParcelDao();
							PlantsDAO plantsDAO = df.getPlantsDAO();
							try {
								parcelDao.updateParcelPlant(plantsDAO
										.getPlantsIDbyType(s1), parcelDao
										.getParcelIdByName(s2));
							} catch (Exception e) {
								JOptionPane.showMessageDialog(appFrame, e
										.getMessage());
								e.printStackTrace();
							}
							cultivatePanel.removeAll();
							cultivatePanel.setVisible(false);
							logger.info("[F1.9] Cultivate parcel " + s1
									+ " with plant type " + s2);
						} else {
							JOptionPane.showMessageDialog(appFrame,
									"Select parcel and plant!");
						}

					}

				});
				cultivatePanel.add(jb);
			}

		});

		cultivatePanel = new JPanel(new GridLayout(0, 2, 0, 4));
		cultivatePanel.setBorder(new CompoundBorder(new TitledBorder(
				"Cultivate"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		cultivatePanel.setVisible(false);

		// Point list
		JPanel pointPanel = new JPanel(new GridLayout(0, 1, 0, 4));
		pointPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

		this.pointLabels = new JLabel[100];
		for (int i = 0; i < this.pointLabels.length; i++) {
			this.pointLabels[i] = new JLabel("");
			pointPanel.add(this.pointLabels[i]);
		}

		// Put the point panel in a container to prevent scroll panel from
		// stretching the vertical spacing.
		JPanel dummyPanel = new JPanel(new BorderLayout());
		dummyPanel.add(pointPanel, BorderLayout.NORTH);

		// Put the point panel in a scroll bar.
		JScrollPane scrollPane = new JScrollPane(dummyPanel);
		scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		if (size != null)
			scrollPane.setPreferredSize(size);

		// Parcel name panel
		parcelnamePanel = new JPanel(new GridLayout(1, 2, 0, 4));
		parcelnamePanel.setBorder(new CompoundBorder(
				new TitledBorder("Parcel"), BorderFactory.createEmptyBorder(5,
						5, 5, 5)));
		parcelnamePanel.add(new JLabel("Parcel code"));
		parcelnameTextarea = new JTextArea();
		parcelnamePanel.add(parcelnameTextarea);
		parcelnamePanel.setVisible(false);

		// Path between panel
		pathBetweenPanel = new JPanel(new GridLayout(0, 1, 0, 4));
		pathBetweenPanel.setBorder(new CompoundBorder(new TitledBorder(
				"Path between:"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		neighbore1Combo = new JComboBox();
		pathBetweenPanel.add(neighbore1Combo);
		neighbore2Combo = new JComboBox();
		pathBetweenPanel.add(neighbore2Combo);
		saveneighboreButton = new JButton("Save neighbores");
		saveneighboreButton.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent actionEvent) {
				DAOFactory df = DAOFactory.getInstance();
				ParcelDAO parceldao = df.getParcelDao();
				try {
					int id1 = parceldao.getParcelIdByName(neighbore1Combo
							.getSelectedItem().toString());
					int id2 = parceldao.getParcelIdByName(neighbore2Combo
							.getSelectedItem().toString());
					System.out.println(id1 + " " + id2);
					if ((id1 != id2)) {
						PathDAO pathdao = df.getPathDAO();
						Path path = new Path();
						path.setLength(measureTool.getLength());

						path.setFrom(id1);
						path.setTo(id2);
						ArrayList<Position> posAL = new ArrayList<Position>();
						posAL = (ArrayList<Position>) measureTool
								.getPositions();
						pathdao.insertPath(path, posAL, ApplicationMap.baseID);
						PreviousParcels pP = new PreviousParcels();
						pP.getAllpreviousParcel((WorldWindowGLCanvas) wwd,
								ApplicationMap.baseID);
						measureTool = new MeasureTool(wwd);
						measureTool.setController(new MeasureToolController());
						measureTool.setFollowTerrain(true);
						measureTool.setMeasureShapeType(MeasureTool.SHAPE_PATH);
						measureTool.setArmed(false);
						endButton.setEnabled(false);
						pathBetweenPanel.setVisible(false);

						logger.info("[F2.1] create path between "
								+ neighbore1Combo.getSelectedItem().toString()
								+ " and "
								+ neighbore1Combo.getSelectedItem().toString());
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(appFrame, e.getMessage());
					e.printStackTrace();
				}

			}
		});
		pathBetweenPanel.add(saveneighboreButton);
		pathBetweenPanel.setVisible(false);
		// Put the pathBetweenPanel panel in a container to prevent scroll panel
		// from stretching the vertical spacing.
		JPanel dummyPanel1 = new JPanel(new BorderLayout());
		dummyPanel1.add(pathBetweenPanel, BorderLayout.NORTH);
		// Put the pathBetweenPanel in a scroll bar.
		JScrollPane sPane = new JScrollPane(dummyPanel1);
		sPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		if (size != null)
			sPane.setPreferredSize(size);

		// delete panel
		deletePanel = new JPanel(new GridLayout(0, 1, 0, 4));
		deletePanel.setBorder(new CompoundBorder(new TitledBorder("Delete:"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		deleteshapeCombo = new JComboBox(new String[] { "Path", "Parcel" });
		deletewhatCombo = new JComboBox();
		deleteselectedButton = new JButton("OK");
		deleteshapeCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {

				String item = (String) ((JComboBox) event.getSource())
						.getSelectedItem();
				if (item.equals("Parcel")) {
					deletePanel.removeAll();
					deletePanel.add(deleteshapeCombo);
					PreviousParcels p = new PreviousParcels();
					deletewhatCombo = p.comboBoxWithParcelNames(
							ApplicationMap.baseID, 2);
					deletePanel.add(deletewhatCombo);
					deletePanel.add(deleteselectedButton);
				} else if (item.equals("Path")) {
					deletePanel.removeAll();
					deletePanel.add(deleteshapeCombo);
					PreviousParcels p = new PreviousParcels();
					deletewhatCombo = p
							.comboboxWithPathIDs(ApplicationMap.baseID);
					deletePanel.add(deletewhatCombo);
					deletePanel.add(deleteselectedButton);
				}
			}
		});
		deleteselectedButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if (deleteshapeCombo.getSelectedItem().toString().equals(
						"Parcel")) {
					String todeleteName = deletewhatCombo.getSelectedItem()
							.toString();
					try {
						if (!todeleteName.equals("Select")) {
							DAOFactory df = DAOFactory.getInstance();
							ParcelDAO parcelDAO = df.getParcelDao();
							parcelDAO.deleteParcelByName(todeleteName);
							PreviousParcels p = new PreviousParcels();
							p.getAllpreviousParcel((WorldWindowGLCanvas) wwd,
									ApplicationMap.baseID);
							deletePanel.setVisible(false);
							measureTool = new MeasureTool(wwd);
							measureTool
									.setController(new MeasureToolController());
							measureTool
									.setMeasureShapeType(MeasureTool.SHAPE_POLYGON);
							logger.info("[F1.9] delete parcell with name "
									+ todeleteName);
							// deletePanel.removeAll();
						}
					} catch (Exception e) {
						JOptionPane.showMessageDialog(appFrame, e.getMessage());
						e.printStackTrace();
						System.out.println("Error on delete");
					}
				} else {
					DAOFactory df = DAOFactory.getInstance();
					PathDAO pathDAO = df.getPathDAO();
					if (!deletewhatCombo.getSelectedItem().toString().equals(
							"Select")) {
						int pathid = Integer.parseInt(deletewhatCombo
								.getSelectedItem().toString());
						try {
							pathDAO.deletePathWithPathID(pathid);
							PreviousParcels p = new PreviousParcels();
							deletePanel.setVisible(false);
							deletePanel.removeAll();
							p.getAllpreviousParcel((WorldWindowGLCanvas) wwd,
									ApplicationMap.baseID);
							measureTool = new MeasureTool(wwd);
							measureTool
									.setController(new MeasureToolController());
							measureTool
									.setMeasureShapeType(MeasureTool.SHAPE_POLYGON);
							logger.info("[F2.1] delete path with id " + pathid);
						} catch (Exception e) {
							System.out.println("Error on delete");
							e.printStackTrace();
						}
					}
				}

			}

		});
		deletePanel.setVisible(false);

		// Metric
		JPanel metricPanel = new JPanel(new GridLayout(0, 2, 0, 4));
		metricPanel.setBorder(new CompoundBorder(new TitledBorder("Metric"),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		metricPanel.add(new JLabel("Length:"));
		lengthLabel = new JLabel();
		metricPanel.add(lengthLabel);
		metricPanel.add(new JLabel("Area:"));
		areaLabel = new JLabel();
		metricPanel.add(areaLabel);

		// Annimation
		JPanel annimationPanel = new JPanel(new GridLayout(5, 1, 2, 2));
		annimationPanel.setBorder(new CompoundBorder(new TitledBorder(
				"Annimation"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		annimateButon = new JButton("Start");
		drawParcelTimeOnAnimation = new JCheckBox("Show time on parcels");
		drawParcelTimeOnAnimation.setSelected(false);
		annimateButon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (!Animation.running) {
						int precision = Integer.parseInt(frameperminText
								.getText());
						boolean drawparcel = drawParcelTimeOnAnimation
								.isSelected();
						if ((precision >= 10) && (precision < 1000)) {
							Animation animation = new Animation(wwd, precision,
									drawparcel, annimationtimelable);
							Thread t = new Thread(animation);
							Animation.speed = 250;
							speedPanel.setVisible(true);
							t.start();
							logger.info("[F2.4] annimation thread started");
						} else {
							JOptionPane
									.showMessageDialog(appFrame,
											"Frame/Simulation min should be a integer between 10-1000");
						}
					}
				} catch (NumberFormatException e) {
					JOptionPane
							.showMessageDialog(appFrame,
									"Frame/Simulation min should be a integer between 10-1000");
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(appFrame,
							"Error in animation!");
				}
			}
		});
		JButton speedinc = new JButton("+");
		speedinc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println(Animation.speed);
				if (Animation.speed >= 2)
					Animation.speed = Animation.speed / 2;
			}
		});
		JButton speeddec = new JButton("-");
		speeddec.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println(Animation.speed);
				Animation.speed = Animation.speed * 2;
			}
		});

		speedPanel = new JPanel(new FlowLayout());
		speedPanel.setVisible(false);
		speedPanel.add(new JLabel("Speed "));
		speedPanel.add(speedinc);
		speedPanel.add(speeddec);
		annimationPanel.add(annimateButon);

		annimationPanel.add(drawParcelTimeOnAnimation);
		JPanel framePanel = new JPanel(new FlowLayout());
		framePanel.add(new JLabel("Frame/Simulation min"));
		frameperminText = new JTextField("10");
		framePanel.add(frameperminText);
		annimationPanel.add(framePanel);
		annimationPanel.add(speedPanel);
		annimationtimelable = new JLabel("");
		annimationPanel.add(annimationtimelable);

		// Add all the panels to a titled panel
		JPanel outerPanel = new JPanel();
		outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.Y_AXIS));
		outerPanel.setBorder(new CompoundBorder(BorderFactory
				.createEmptyBorder(9, 9, 9, 9), new TitledBorder("Measure")));
		outerPanel.setToolTipText("Parcel tool control");
		outerPanel.add(shapePanel);
		outerPanel.add(buttonPanel);
		outerPanel.add(parcelnamePanel);
		outerPanel.add(manageParcelPathPanel);
		outerPanel.add(cultivatePanel);
		outerPanel.add(deletePanel);
		outerPanel.add(metricPanel);
		outerPanel.add(annimationPanel);
		outerPanel.add(sPane);
		this.add(outerPanel, BorderLayout.NORTH);
	}

	private void fillPointsPanel() {
		int i = 0;
		if (measureTool.getPositions() != null) {
			for (LatLon pos : measureTool.getPositions()) {
				if (i == this.pointLabels.length)
					break;

				String las = String.format("Lat %7.4f\u00B0", pos.getLatitude()
						.getDegrees());
				String los = String.format("Lon %7.4f\u00B0", pos
						.getLongitude().getDegrees());
				pointLabels[i++].setText(las + "  " + los);
			}
		}
		// Clear remaining labels
		for (; i < this.pointLabels.length; i++)
			pointLabels[i].setText("");

	}

	private void updateMetric() {
		// Update length label
		double value = measureTool.getLength();
		String s;
		if (value <= 0)
			s = "na";
		else if (value < 1000)
			s = String.format("%,7.1f m", value);
		else
			s = String.format("%,7.3f km", value / 1000);
		lengthLabel.setText(s);

		// Update area label
		value = measureTool.getArea();
		if (value < 0)
			s = "na";
		else if (value < 1e4)
			s = String.format("%,7.1f m2", value);
		else
			s = String.format("%,7.3f ha", value / 1e4);
		areaLabel.setText(s);
	}
}
