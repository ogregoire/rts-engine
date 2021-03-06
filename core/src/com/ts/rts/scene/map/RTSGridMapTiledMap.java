package com.ts.rts.scene.map;

import com.ts.rts.RTSGame;
import com.ts.rts.datastructure.grid.render.GridMapRenderer;
import com.ts.rts.datastructure.mapgen.gridmap.GridMapGen;
import com.ts.rts.scene.unit.IBoundsObject;

/**
 * Materialization of an {@link IRTSMap} as a grid map.
 * 
 * @author Toni Sagrista
 * 
 */
public class RTSGridMapTiledMap extends RTSAbstractMap {

	public RTSGridMapTiledMap(RTSGame game, String tiledMapPath) {
		super(game, tiledMapPath);
	}

	@Override
	protected void initializeMapGenAndRenderer() {
		mapGen = new GridMapGen<IBoundsObject>();
		// Initialize map renderer for debug
		rtsMapRenderer = new GridMapRenderer();

	}

}
