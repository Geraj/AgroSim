package calculations.animation;

import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import appmap.ApplicationMap;
import calculations.ModelAndGraphBuilder;
import calculations.animation.log.AnimationData;
import control.observer.StateListener;
import control.observer.StateMachineEvents;
import control.statemachine.helper.MovingToParcel;
import control.statemachine.helper.WorkingOnParcel;
import core.PathPoints;

import dao.jdbc.DbException;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.AnnotationLayer;
import gov.nasa.worldwind.render.AnnotationAttributes;
import gov.nasa.worldwind.render.GlobeAnnotation;

/**
 * Handle vehicle state changes for simulation animation.
 * 
 * @author Geraj
 *
 */
public class AnimationStateHandler implements StateListener {

	/***/
	private WorldWindow wwd;

	private ModelAndGraphBuilder graph;

	private HashMap<Object, AnnotationLayer> vehicleLayer = new HashMap<>();
	
	private HashMap<Object, LinkedList<Position> > vehiclePointsToTravelLayer = new HashMap<>();
	/**
	 * 
	 * @param wwd
	 * @param baseID
	 */
	public AnimationStateHandler(WorldWindow wwd, int baseID) {
		this.wwd = wwd;
		graph = new ModelAndGraphBuilder(baseID, 3);
	}

	/**
	 * Handle State machine event
	 * 
	 * @param eventType
	 * @param message
	 * @param source
	 */
	public void handleEvent(StateMachineEvents eventType, Object message, Object source) {
		if (StateMachineEvents.TICK.equals(eventType)) {
			for (Object object : vehiclePointsToTravelLayer.keySet()) {
				if (!vehiclePointsToTravelLayer.get(object).isEmpty()) {
					Position position = vehiclePointsToTravelLayer.get(object).removeFirst();
					AnnotationLayer layer = vehicleLayer.get(object);
					layer.removeAllAnnotations();
					wwd.getModel().getLayers().remove(layer);
					if (position != null) {
						StringBuilder annotationMessageBuilder = new StringBuilder();
						annotationMessageBuilder.append(object).append(" ")
								.append(StateMachineEvents.VEHICLE_MOVING_TO_PARCEL);
						layer.addAnnotation(drawLayerOnPosition(annotationMessageBuilder, position));
						wwd.getModel().getLayers().add(layer);
					}
				}
			}
		} else {
			AnnotationLayer layer = null;
			if (vehicleLayer.get(source) != null) {
				layer = vehicleLayer.get(source);
				layer.removeAllAnnotations();
			} else {
				layer = new AnnotationLayer();
				vehicleLayer.put(source, layer);
			}

			StringBuilder annotationMessageBuilder = new StringBuilder();
			Position position = null;
			if (StateMachineEvents.VEHICLE_STARTED.equals(eventType)) {
				vehiclePointsToTravelLayer.remove(source);
				position = Position.fromDegrees(ApplicationMap.base.getLatitude(), ApplicationMap.base.getLongitude());
				annotationMessageBuilder.append(source + eventType.getDescription());
			} else if (StateMachineEvents.VEHICLE_STOPPED.equals(eventType)) {
				vehiclePointsToTravelLayer.remove(source);
			} else if (StateMachineEvents.VEHICLE_WORKING.equals(eventType)) {
				vehiclePointsToTravelLayer.remove(source);
				try {
					WorkingOnParcel messageData = (WorkingOnParcel) message;
					position = AnimationData.parcelMiddlePosition(messageData.getParcelId());
					annotationMessageBuilder.append(source + " " + eventType.getDescription())
							.append(" complete " + String.format("%,.2f", messageData.getComplete()) + " %");
				} catch (DbException e) {
					e.printStackTrace();
				}
			} else if (StateMachineEvents.VEHICLE_MOVING_TO_PARCEL.equals(eventType)) {
				MovingToParcel messageData = (MovingToParcel) message;
				ArrayList<PathPoints> points = AnnimationHelper.createCordinates(messageData.getPathToTravel(), graph);
				LinkedList<Position> pathPositions = new LinkedList<>();
				for (PathPoints point : points) {
					pathPositions.add(Position.fromDegrees(point.getLatitude(), point.getLongitude()));
				}
				position = pathPositions.removeFirst();
				vehiclePointsToTravelLayer.put(source, pathPositions);
				annotationMessageBuilder.append(source + " " + eventType.getDescription() + " " + messageData);
			}

			wwd.getModel().getLayers().remove(layer);
			if (position != null) {
				layer.addAnnotation(drawLayerOnPosition(annotationMessageBuilder, position));
				wwd.getModel().getLayers().add(layer);
			}
		}
	}

	/***
	 * Draw an annotation on position with message
	 * 
	 * @param annotationMessageBuilder
	 * @param position
	 * @return
	 */
	private GlobeAnnotation drawLayerOnPosition(StringBuilder annotationMessageBuilder, Position position) {
		AnnotationAttributes annotationAttribute = new AnnotationAttributes();
		annotationAttribute.setFont(Font.decode("Arial-BOLD-12"));
		annotationAttribute.setDistanceMaxScale(1);
		annotationAttribute.setDistanceMinScale(1);
		GlobeAnnotation gg = new GlobeAnnotation(annotationMessageBuilder.toString(), position, annotationAttribute);
		gg.getAttributes().setVisible(true);
		gg.setAlwaysOnTop(false);
		return gg;
	}

}
