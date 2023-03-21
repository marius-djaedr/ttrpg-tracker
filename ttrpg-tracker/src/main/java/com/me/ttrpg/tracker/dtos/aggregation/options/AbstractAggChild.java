package com.me.ttrpg.tracker.dtos.aggregation.options;

public abstract class AbstractAggChild<P> {
	private transient final P parent;

	AbstractAggChild(final P parent) {
		this.parent = parent;
	}

	public P done() {
		return parent;
	}
}
