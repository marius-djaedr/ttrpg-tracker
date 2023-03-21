package com.me.ttrpg.tracker.aggregators;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.mortbay.io.RuntimeIOException;

import com.me.ttrpg.tracker.dtos.aggregation.AggOutputDto;
import com.me.ttrpg.tracker.utils.AggregatorOutput;

public abstract class AbstractGraphAggregator implements Aggregator {

	@Override
	public void write(final File outputDir) {
		complete().forEach(d -> writeOne(d, outputDir));
	}

	private <T> void writeOne(final AggOutputDto<T> dto, final File outputDir) {
		File file = outputDir;
		if(dto.getNestedFile() != null) {
			file = new File(outputDir, dto.getNestedFile());
			file.mkdirs();
		}
		file = new File(file, dto.getOptions().getTitle() + ".html");
		try {
			AggregatorOutput.writeToFile(file, dto);
		} catch(final IOException e) {
			throw new RuntimeIOException(e);
		}
	}

	protected abstract List<AggOutputDto<?>> complete();
}
