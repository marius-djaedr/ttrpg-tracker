package com.me.ttrpg.tracker.frames.util;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.me.gui.swing.views.GuiFrame;
import com.me.gui.swing.views.GuiLaunchView;

@Component
public class UtilityFrame implements GuiFrame {

	@Autowired
	private GuiLaunchView actionManager;
	@Autowired
	private ApplicationContext context;

	private JPanel topPanel;

	@Override
	public String tabName() {
		return "Utilities";
	}

	@Override
	public double order() {
		return 5.0;
	}

	@Override
	public JPanel buildPanel() {

		final JPanel panel = new JPanel(new GridLayout(0, 1));

		for(final UtilityButton buttonSpecs : context.getBeansOfType(UtilityButton.class).values()) {
			final JButton button = new JButton(buttonSpecs.name());
			button.addActionListener(e -> actionWithRefresh(e, buttonSpecs));
			panel.add(button);
		}

		return panel;
	}

	private void actionWithRefresh(final ActionEvent e, final UtilityButton ub) {
		ub.actionListener(e);
		actionManager.actionDone();
	}
}
