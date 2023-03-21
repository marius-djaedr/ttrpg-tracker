package com.me.ttrpg.tracker.frames;

import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.me.gui.swing.LaunchWrapper;
import com.me.gui.swing.utils.GuiUtils;
import com.me.gui.swing.views.GuiFrame;
import com.me.ttrpg.tracker.utils.AggregationLauncher;
import com.me.util.utils.FileUtils;

@Component
public class AggregationFrame implements GuiFrame {
	@Autowired
	private AggregationLauncher aggregationLauncher;
	@Autowired
	private LaunchWrapper launchWrapper;

	private JPanel innerPanel;
	private GridBagConstraints innerC;

	@Override
	public String tabName() {
		return "Aggregation";
	}

	@Override
	public double order() {
		return 4.0;
	}

	@Override
	public JPanel buildPanel() {
		UIManager.put("TaskPane.animate", Boolean.FALSE);

		final GridBagConstraints c = new GridBagConstraints();
		final JPanel topPanel = GuiUtils.commonBuildPanel(c);

		final JButton addButton = new JButton("Run Aggregation");
		addButton.addActionListener(this::runAggregationActionListener);

		c.weighty = 0.;
		topPanel.add(addButton, c);
		c.gridy++;

		c.weighty = 1.;
		topPanel.add(buildAggregatePanel(), c);
		return topPanel;
	}

	private JPanel buildAggregatePanel() {
		final GridBagConstraints outerC = new GridBagConstraints();
		final JPanel outerPanel = GuiUtils.commonBuildPanel(outerC);

		innerC = new GridBagConstraints();
		innerPanel = GuiUtils.commonBuildPanel(innerC);
		fillInnerPanel(FileUtils.getLatestDirectory(new File("src/main/resources/aggregation")));

		final JScrollPane scroll = new JScrollPane(innerPanel);
		outerPanel.add(scroll, outerC);
		return outerPanel;
	}

	private void fillInnerPanel(final File aggregateOutput) {
		innerPanel.removeAll();

		innerC.weightx = 1.;
		innerC.weighty = 1.;
		innerC.gridx = 0;
		innerC.gridy = 0;

		innerC.weighty = 0.;
		innerPanel.add(new JLabel(aggregateOutput.getAbsolutePath()), innerC);
		innerC.weighty = 1.;
		innerC.gridy++;

		final JPanel generalPanel = new JPanel(new GridLayout(0, 4));
		innerPanel.add(generalPanel, innerC);
		innerC.gridy++;

		final JXTaskPaneContainer childrenContainer = new JXTaskPaneContainer();
		innerPanel.add(childrenContainer, innerC);
		innerC.gridy++;

		for(final File file : aggregateOutput.listFiles()) {
			if(file.isDirectory()) {
				final JXTaskPane pane = new JXTaskPane();
				pane.setTitle(file.getName());
				pane.setCollapsed(true);

				for(final File child : file.listFiles()) {
					pane.add(buildFileLink(child));
				}
				childrenContainer.add(pane);
			} else {
				generalPanel.add(buildFileLink(file));
			}
		}
	}

	private JButton buildFileLink(final File file) {

		final JButton label = new JButton(file.getName());
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent e) {
				try {
					Desktop.getDesktop().browse(file.toURI());
				} catch(final IOException e1) {
					//TODO
					e1.printStackTrace();
				}
			}
		});
		return label;
	}

	private void runAggregationActionListener(final ActionEvent e) {
		launchWrapper.executeOnNewThread(() -> {
			final File aggregateOutput = aggregationLauncher.launch();
			fillInnerPanel(aggregateOutput);
		});
	}
}
