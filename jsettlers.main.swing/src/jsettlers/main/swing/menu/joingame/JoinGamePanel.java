/*******************************************************************************
 * Copyright (c) 2015
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package jsettlers.main.swing.menu.joingame;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import jsettlers.common.menu.ENetworkMessage;
import jsettlers.common.menu.IChatMessageListener;
import jsettlers.common.menu.IJoinPhaseMultiplayerGameConnector;
import jsettlers.common.menu.IMultiplayerPlayer;
import jsettlers.graphics.localization.Labels;
import jsettlers.logic.map.EMapStartResources;
import jsettlers.logic.map.MapLoader;
import jsettlers.main.swing.JSettlersFrame;
import jsettlers.main.swing.JSettlersSwingUtil;
import jsettlers.main.swing.lookandfeel.ELFStyle;
import jsettlers.main.swing.lookandfeel.components.BackgroundPanel;
import jsettlers.main.swing.menu.joingame.slots.PlayerSlotFactory;
import jsettlers.main.swing.menu.joingame.slots.PlayerTable;

/**
 * @author codingberlin
 */
public class JoinGamePanel extends BackgroundPanel {
	private static final long serialVersionUID = -1186791399814385303L;

	protected final JSettlersFrame settlersFrame;
	protected final JLabel titleLabel = new JLabel();
	private final JPanel contentPanel = new JPanel();
	private final JPanel westPanel = new JPanel();
	private final JPanel mapPanel = new JPanel();
	private final JPanel settingsPanel = new JPanel();
	private final JPanel centerPanel = new JPanel();
	private final JLabel mapNameLabel = new JLabel();
	private final JLabel mapImage = new JLabel();
	private final JLabel numberOfPlayersLabel = new JLabel();
	protected final JComboBox<Integer> numberOfPlayersComboBox = new JComboBox<>();
	private final JLabel peaceTimeLabel = new JLabel();
	protected final JComboBox<EPeaceTime> peaceTimeComboBox = new JComboBox<>();
	private final JLabel startResourcesLabel = new JLabel();
	protected final JComboBox<MapStartResourcesUIWrapper> startResourcesComboBox = new JComboBox<>();
	protected final PlayerTable playerSlotsPanel;
	protected final JButton cancelButton = new JButton();
	protected final JButton startGameButton = new JButton();
	private final JTextField chatInputField = new JTextField();
	private final JTextArea chatArea = new JTextArea();
	private final JButton sendChatMessageButton = new JButton();
	protected final MapLoader mapLoader;
	private final PlayerSlotFactory playerSlotFactory;

	public JoinGamePanel(JSettlersFrame settlersFrame, MapLoader mapLoader, PlayerSlotFactory playerSlotFactory) {
		this.settlersFrame = settlersFrame;
		this.mapLoader = mapLoader;
		this.playerSlotFactory = playerSlotFactory;
		playerSlotsPanel = PlayerTable.create(this.mapLoader, playerSlotFactory);
		createStructure();
		setStyle();
		localize();
		addListener();
		prepareUiFor();
	}

	private void createStructure() {
		add(contentPanel);
		contentPanel.setLayout(new BorderLayout(30, 30));
		JPanel titleLabelWrapper = new JPanel();
		contentPanel.add(titleLabelWrapper, BorderLayout.NORTH);
		titleLabelWrapper.add(titleLabel);
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPanel.add(westPanel, BorderLayout.WEST);
		contentPanel.add(centerPanel, BorderLayout.CENTER);
		westPanel.setLayout(new BorderLayout());
		westPanel.add(mapPanel, BorderLayout.NORTH);
		JPanel settingsPanelWrapper = new JPanel();
		westPanel.add(settingsPanelWrapper, BorderLayout.CENTER);
		settingsPanelWrapper.add(settingsPanel);
		settingsPanel.setLayout(new GridLayout(0, 2, 20, 20));
		mapPanel.setLayout(new BorderLayout());
		JPanel mapNameLabelWrapper = new JPanel();
		mapPanel.add(mapNameLabelWrapper, BorderLayout.NORTH);
		mapNameLabelWrapper.add(mapNameLabel);
		mapNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		mapPanel.add(mapImage, BorderLayout.CENTER);
		mapImage.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		settingsPanel.add(numberOfPlayersLabel);
		settingsPanel.add(numberOfPlayersComboBox);
		settingsPanel.add(startResourcesLabel);
		settingsPanel.add(startResourcesComboBox);
		settingsPanel.add(peaceTimeLabel);
		settingsPanel.add(peaceTimeComboBox);
		centerPanel.setLayout(new BorderLayout(0, 30));
		sendChatMessageButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 15));
		JPanel chatPanel = new JPanel();
		chatPanel.setLayout(new BorderLayout(0, 10));
		JPanel chatInputPanel = new JPanel();
		chatInputPanel.setLayout(new BorderLayout(10, 0));
		chatPanel.add(chatArea, BorderLayout.CENTER);
		chatPanel.add(chatInputPanel, BorderLayout.SOUTH);
		chatInputPanel.add(chatInputField, BorderLayout.CENTER);
		chatInputPanel.add(sendChatMessageButton, BorderLayout.EAST);
		centerPanel.add(chatPanel, BorderLayout.CENTER);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		playerSlotsPanel.setLayout(new GridBagLayout());
		JScrollPane playerSlotPanelWrapper = new JScrollPane(playerSlotsPanel);
		playerSlotsPanel.setBorder(new EmptyBorder(20, 25, 20, 20));
		centerPanel.add(playerSlotPanelWrapper, BorderLayout.NORTH);
		JPanel southPanelWrapper = new JPanel();
		contentPanel.add(southPanelWrapper, BorderLayout.SOUTH);
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new GridLayout(0, 3, 20, 20));
		southPanelWrapper.add(southPanel);
		cancelButton.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 15));
		southPanel.add(cancelButton);
		startGameButton.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 15));
		southPanel.add(startGameButton);
	}

	private void setStyle() {
		mapNameLabel.putClientProperty(ELFStyle.KEY, ELFStyle.LABEL_LONG);
		numberOfPlayersLabel.putClientProperty(ELFStyle.KEY, ELFStyle.LABEL_SHORT);
		startResourcesLabel.putClientProperty(ELFStyle.KEY, ELFStyle.LABEL_SHORT);
		peaceTimeLabel.putClientProperty(ELFStyle.KEY, ELFStyle.LABEL_SHORT);
		titleLabel.putClientProperty(ELFStyle.KEY, ELFStyle.LABEL_HEADER);
		cancelButton.putClientProperty(ELFStyle.KEY, ELFStyle.BUTTON_MENU);
		startGameButton.putClientProperty(ELFStyle.KEY, ELFStyle.BUTTON_MENU);
		sendChatMessageButton.putClientProperty(ELFStyle.KEY, ELFStyle.BUTTON_MENU);
		chatInputField.putClientProperty(ELFStyle.KEY, ELFStyle.TEXT_DEFAULT);
		chatArea.putClientProperty(ELFStyle.KEY, ELFStyle.PANEL_DARK);
		startResourcesComboBox.putClientProperty(ELFStyle.KEY, ELFStyle.COMBOBOX);
		numberOfPlayersComboBox.putClientProperty(ELFStyle.KEY, ELFStyle.COMBOBOX);
		peaceTimeComboBox.putClientProperty(ELFStyle.KEY, ELFStyle.COMBOBOX);
		chatArea.putClientProperty(ELFStyle.KEY, ELFStyle.TEXT_DEFAULT);
		SwingUtilities.updateComponentTreeUI(this);
	}

	private void localize() {
		numberOfPlayersLabel.setText(Labels.getString("join-game-panel-number-of-players"));
		startResourcesLabel.setText(Labels.getString("join-game-panel-start-resources"));
		cancelButton.setText(Labels.getString("join-game-panel-cancel"));
		startGameButton.setText(Labels.getString("join-game-panel-start"));
		peaceTimeLabel.setText(Labels.getString("join-game-panel-peace-time"));
		// slotsHeadlinePlayerNameLabel.setText(Labels.getString("join-game-panel-player-name")); XXX
		// slotsHeadlineCivilisation.setText(Labels.getString("join-game-panel-civilisation"));
		// slotsHeadlineType.setText(Labels.getString("join-game-panel-player-type"));
		// slotsHeadlineMapSlot.setText(Labels.getString("join-game-panel-map-slot"));
		// slotsHeadlineTeam.setText(Labels.getString("join-game-panel-team"));
		sendChatMessageButton.setText(Labels.getString("join-game-panel-send-chat-message"));
	}

	private void addListener() {
		numberOfPlayersComboBox.addActionListener(e -> updateNumberOfPlayerSlots());
		startGameButton.addActionListener(e -> onGameStart());
		cancelButton.addActionListener(e -> onCancel());
	}

	protected void setChatVisible(boolean isVisible) {
		chatArea.setVisible(isVisible);
		chatInputField.setVisible(isVisible);
		sendChatMessageButton.setVisible(isVisible);
		chatArea.setText("");
		chatInputField.setText("");
	}

	protected void initializeChatFor(IJoinPhaseMultiplayerGameConnector joinMultiPlayerMap) {
		joinMultiPlayerMap.setChatListener(new IChatMessageListener() {
			@Override
			public void chatMessageReceived(String authorId, String message) {
				chatArea.append(authorId + ": " + message + "\n");
			}

			@Override
			public void systemMessageReceived(IMultiplayerPlayer author, ENetworkMessage message) {
				chatArea.append(Labels.getString("network-message-" + message.name()) + "\n");
			}
		});
		ActionListener sendChatMessage = e -> {
			String message = chatInputField.getText();
			if (!message.equals("")) {
				joinMultiPlayerMap.sendChatMessage(message);
				chatInputField.setText("");
			}
		};
		Arrays.asList(sendChatMessageButton.getActionListeners()).stream().forEach(sendChatMessageButton::removeActionListener);
		Arrays.asList(chatInputField.getActionListeners()).stream().forEach(chatInputField::removeActionListener);
		sendChatMessageButton.addActionListener(sendChatMessage);
		chatInputField.addActionListener(sendChatMessage);

	}

	// private void onPlayersChanges(ChangingList<? extends IMultiplayerPlayer> changingPlayers, IJoinPhaseMultiplayerGameConnector
	// joinMultiPlayerMap) {
	// SwingUtilities.invokeLater(() -> {
	// List<? extends IMultiplayerPlayer> players = changingPlayers.getItems();
	// String myId = SettingsManager.getInstance().get(SettingsManager.SETTING_UUID);
	// for (int i = 0; i < players.size(); i++) {
	// PlayerSlot playerSlot = playerSlots.get(i);
	// IMultiplayerPlayer player = players.get(i);
	// playerSlot.setPlayerName(player.getName());
	// playerSlot.setReady(player.isReady());
	// if (player.getId().equals(myId)) {
	// playerSlot.setReadyButtonEnabled(true);
	// playerSlot.informGameAboutReady(joinMultiPlayerMap);
	// } else {
	// playerSlot.setReadyButtonEnabled(false);
	// }
	// }
	// for (int i = players.size(); i < playerSlots.size(); i++) {
	// playerSlots.get(i).setTypeComboBox(EPlayerType.AI_VERY_HARD);
	// }
	// setCancelButtonActionListener(e -> {
	// joinMultiPlayerMap.abort();
	// settlersFrame.showMainMenu();
	// });
	// });
	// };

	private void prepareUiFor() {
		mapNameLabel.setText(mapLoader.getMapName());
		mapImage.setIcon(new ImageIcon(JSettlersSwingUtil.createBufferedImageFrom(mapLoader)));
		peaceTimeComboBox.removeAllItems();
		peaceTimeComboBox.addItem(EPeaceTime.WITHOUT);
		startResourcesComboBox.removeAllItems();
		Arrays.asList(EMapStartResources.values()).stream()
				.map(MapStartResourcesUIWrapper::new)
				.forEach(startResourcesComboBox::addItem);
		startResourcesComboBox.setSelectedIndex(EMapStartResources.HIGH_GOODS.value - 1);
		resetNumberOfPlayersComboBox();
		updateNumberOfPlayerSlots();
	}

	// private void buildPlayerSlots() {
	// int maximumNumberOfPlayers = this.mapLoader.getMaxPlayers();
	// playerSlots.clear();
	// for (byte i = 0; i < maximumNumberOfPlayers; i++) {
	// PlayerSlot playerSlot = playerSlotFactory.createPlayerSlot(i, this.mapLoader);
	// playerSlots.add(playerSlot);
	// }
	// for (byte i = 0; i < playerSlots.size(); i++) {
	// playerSlots.get(i).setSlot(i);
	// playerSlots.get(i).setTeam(i);
	// }
	// }

	private void resetNumberOfPlayersComboBox() {
		numberOfPlayersComboBox.removeAllItems();
		for (int i = 1; i < mapLoader.getMaxPlayers() + 1; i++) {
			numberOfPlayersComboBox.addItem(i);
		}
		numberOfPlayersComboBox.setSelectedIndex(mapLoader.getMaxPlayers() - 1);
	}

	private void updateNumberOfPlayerSlots() {
		if (playerSlotFactory == null || numberOfPlayersComboBox.getSelectedItem() == null) {
			return;
		}
		playerSlotsPanel.setPlayerCount((int) numberOfPlayersComboBox.getSelectedItem());
	}

	// playerSlotsPanel.removeAll();
	// addPlayerSlotHeadline();
	// int available = Math.min(playerSlots.size(), (int) numberOfPlayersComboBox.getSelectedItem());
	// for (int i = 0; i < available; i++) {
	// playerSlots.get(i).getListCells();
	// }
	// for (int i = available; i < playerSlots.size(); i++) {
	// playerSlots.get(i).setAvailable(false);
	// }
	// SwingUtilities.updateComponentTreeUI(playerSlotsPanel);
	// SlotToggleGroup slotToggleGroup = new SlotToggleGroup();
	// playerSlots.stream().forEach(slotToggleGroup::add);
	// }

	// private void addPlayerSlotHeadline() {
	// GridBagConstraints constraints = new GridBagConstraints();
	// constraints.gridx = 1;
	// constraints.gridy = 0;
	// constraints.gridwidth = 4;
	// constraints.fill = GridBagConstraints.HORIZONTAL;
	// playerSlotsPanel.add(slotsHeadlinePlayerNameLabel, constraints);
	// constraints.gridx = 5;
	// constraints.gridy = 0;
	// constraints.gridwidth = 2;
	// constraints.fill = GridBagConstraints.HORIZONTAL;
	// playerSlotsPanel.add(slotsHeadlineCivilisation, constraints);
	// constraints = new GridBagConstraints();
	// constraints.gridx = 7;
	// constraints.gridy = 0;
	// constraints.gridwidth = 4;
	// constraints.fill = GridBagConstraints.HORIZONTAL;
	// playerSlotsPanel.add(slotsHeadlineType, constraints);
	// constraints = new GridBagConstraints();
	// constraints.gridx = 11;
	// constraints.gridy = 0;
	// constraints.gridwidth = 1;
	// constraints.fill = GridBagConstraints.HORIZONTAL;
	// playerSlotsPanel.add(slotsHeadlineMapSlot, constraints);
	// constraints = new GridBagConstraints();
	// constraints.gridx = 12;
	// constraints.gridy = 0;
	// constraints.gridwidth = 1;
	// constraints.fill = GridBagConstraints.HORIZONTAL;
	// playerSlotsPanel.add(slotsHeadlineTeam, constraints);
	// }

	protected void onGameStart() {
	}

	protected void onCancel() {
		settlersFrame.showMainMenu();
	}

	private enum EPeaceTime {
		WITHOUT;

		@Override
		public String toString() {
			return Labels.getString("peace-time-" + name());
		}
	}
}
