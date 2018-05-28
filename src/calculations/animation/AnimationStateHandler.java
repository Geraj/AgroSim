package calculations.animation;

import java.awt.Font;
import java.util.HashMap;

import appmap.ApplicationMap;
import calculations.animation.log.AnimationData;
import control.observer.StateListener;
import control.observer.StateMachineEvents;
import control.statemachine.MovingToParcel;
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

	private HashMap<Object, AnnotationLayer> vehicleLayer = new HashMap<>();
	/**
	 * 
	 * @param wwd
	 */
	public AnimationStateHandler(WorldWindow wwd) {
		this.wwd = wwd;
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
				position = Position.fromDegrees(ApplicationMap.base.getLatitude(),
						ApplicationMap.base.getLongitude());
				annotationMessageBuilder.append(source + eventType.getDescription());
			} else if (StateMachineEvents.VEHICLE_STOPPED.equals(eventType)) {

			} else if (StateMachineEvents.VEHICLE_WORKING.equals(eventType)) {
				try {
					position = AnimationData.parcelMiddlePosition((int) message);
					annotationMessageBuilder.append(source + eventType.getDescription());
				} catch (DbException e) {
					e.printStackTrace();
				}
			} else if (StateMachineEvents.VEHICLE_MOVING_TO_PARCEL.equals(eventType)) {
				MovingToParcel messageData = (MovingToParcel) message;
				messageData.getFrom();
				messageData.getTo();
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
		GlobeAnnotation gg = new GlobeAnnotation(annotationMessageBuilder.toString(), position,
				annotationAttribute);
		gg.getAttributes().setVisible(true);
		gg.setAlwaysOnTop(false);
		return gg;
	}

}
