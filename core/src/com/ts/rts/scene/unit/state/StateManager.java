package com.ts.rts.scene.unit.state;

import java.util.Deque;
import java.util.LinkedList;

import com.ts.rts.datastructure.geom.Vector2;
import com.ts.rts.scene.unit.PositionPhysicalEntity;
import com.ts.rts.scene.unit.Unit;

/**
 * Manages the states of a unit. States are high level actions the unit must perform such as go somewhere, attack
 * something, defend something or wander around an area.
 * 
 * @author Toni Sagrista
 * 
 */
public class StateManager {

	private Deque<IState> states;
	private Unit unit;

	public StateManager(Unit unit) {
		states = new LinkedList<IState>();
	}

	public IState getCurrentState() {
		return states.peekFirst();
	}

	public void moveToNextState() {
		IState state = states.pollFirst();
		state.removeState();
	}

	public void addStateFirst(IState state) {
		states.addFirst(state);
	}

	public void addStateLast(IState state) {
		states.addLast(state);
	}

	public void goTo(Vector2 target) {
		addStateFirst(new StateGoTo(unit, target));
	}

	public void attack(PositionPhysicalEntity target) {
		addStateFirst(new StateAttack(unit, target));
	}

}
