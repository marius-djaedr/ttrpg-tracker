package com.me.ttrpg.tracker;

import com.me.gui.swing.ApplicationEntryPoint;
import com.me.ttrpg.tracker.spring.SpringConfig;

public class Application {
	public static void main(final String[] args) throws Throwable {
		ApplicationEntryPoint.execute(SpringConfig.class, args);
	}
}

/*
 * TODO
 * - save rate
 *   - currently, only saves when exiting. Might want to do on change, just to preserve stuff
 * - testing
 *   - fuck ton everywhere all over
 * - maybe?
 *   - undo concept (see DataManager for first attempt at this)
 */
