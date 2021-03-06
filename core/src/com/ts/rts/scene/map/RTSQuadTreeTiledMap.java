package com.ts.rts.scene.map;

import com.ts.rts.RTSGame;
import com.ts.rts.datastructure.mapgen.quadmap.QuadMapGen;
import com.ts.rts.datastructure.quadtree.render.QuadTreeRenderer;
import com.ts.rts.scene.unit.IBoundsObject;

/**
 * Materialization of an {@link IRTSMap} as a grid map.
 * 
 * @author Toni Sagrista
 * 
 */
public class RTSQuadTreeTiledMap extends RTSAbstractMap {

	public RTSQuadTreeTiledMap(RTSGame game, String tiledMapPath) {
		super(game, tiledMapPath);
	}

	@Override
	protected void initializeMapGenAndRenderer() {
		mapGen = new QuadMapGen<IBoundsObject>();
		// Initialize map renderer for debug
		rtsMapRenderer = new QuadTreeRenderer();
	}

}
