package org.apollo.game.message.handler.impl;

import java.util.HashMap;
import java.util.Map;

import org.apollo.game.message.handler.MessageHandler;
import org.apollo.game.message.handler.MessageHandlerContext;
import org.apollo.game.message.impl.InventoryItemMessage;
import org.apollo.game.model.Item;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.inter.bank.BankConstants;
import org.apollo.game.model.inv.Inventory;
import org.apollo.game.model.inv.SynchronizationInventoryListener;

/**
 * A {@link MessageHandler} that verifies {@link InventoryItemMessage}s.
 * 
 * @author Chris Fletcher
 * @author Major
 */
public final class ItemVerificationHandler extends MessageHandler<InventoryItemMessage> {

	/**
	 * A supplier for an {@link Inventory}.
	 * 
	 * @author Major
	 */
	@FunctionalInterface
	public static interface InventorySupplier {

		/**
		 * Gets the appropriate {@link Inventory}.
		 * 
		 * @param player The {@link Player} who prompted the verification call.
		 * @return The inventory. Must not be {@code null}.
		 */
		public Inventory getInventory(Player player);

	}

	/**
	 * The map of interface ids to inventories.
	 */
	private static final Map<Integer, InventorySupplier> inventories = new HashMap<>();

	static {
		inventories.put(SynchronizationInventoryListener.INVENTORY_ID, Player::getInventory);
		inventories.put(BankConstants.SIDEBAR_INVENTORY_ID, Player::getInventory);
		inventories.put(SynchronizationInventoryListener.EQUIPMENT_ID, Player::getEquipment);
		inventories.put(BankConstants.BANK_INVENTORY_ID, Player::getBank);
	}

	/**
	 * Adds an {@link Inventory} with the specified interface id to the {@link Map} of supported ones,
	 * <strong>iff</strong> the specified id does <strong>not</strong> already have a mapping.
	 * 
	 * @param id The id of the interface.
	 * @param supplier The {@link InventorySupplier}.
	 */
	public static void addInventory(int id, InventorySupplier supplier) {
		inventories.putIfAbsent(id, supplier);
	}

	@Override
	public void handle(MessageHandlerContext ctx, Player player, InventoryItemMessage message) {
		InventorySupplier supplier = inventories.get(message.getInterfaceId());
		if (supplier == null) {
			ctx.breakHandlerChain();
			return;
		}

		Inventory inventory = supplier.getInventory(player);

		int slot = message.getSlot();
		if (slot < 0 || slot >= inventory.capacity()) {
			ctx.breakHandlerChain();
			return;
		}

		Item item = inventory.get(slot);
		if (item == null || item.getId() != message.getId()) {
			ctx.breakHandlerChain();
		}
	}

}